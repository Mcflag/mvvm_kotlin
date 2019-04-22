package com.ccooy.app3.ui.login

import androidx.lifecycle.MutableLiveData
import com.ccooy.app3.base.loadings.CommonLoadingState
import com.ccooy.app3.base.viewmodel.BaseViewModel
import com.ccooy.app3.base.viewstate.SimpleViewState
import com.ccooy.app3.data.model.db.LoginEntity
import com.ccooy.app3.data.model.response.LoginUser
import com.ccooy.app3.data.model.vo.Errors
import com.ccooy.app3.ext.functional.Either
import com.ccooy.app3.ext.livedata.toReactiveStream
import com.ccooy.app3.util.net.globalHandleError
import com.uber.autodispose.autoDisposable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import retrofit2.HttpException
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repo: LoginRepository
) : BaseViewModel() {
    val username: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()

    val loadingLayout: MutableLiveData<CommonLoadingState> = MutableLiveData()
    val error: MutableLiveData<Throwable> = MutableLiveData()
    val isShowMessage: MutableLiveData<Boolean> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    val userInfo: MutableLiveData<LoginUser> = MutableLiveData()
    private val autoLogin: MutableLiveData<Boolean> = MutableLiveData()

    init {
        autoLogin.toReactiveStream()
            .filter { it }
            .doOnNext { login() }
            .autoDisposable(this)
            .subscribe()

        error.toReactiveStream()
            .flatMap {
                Flowable.just(
                    when (it) {
                        is Errors.EmptyInputError -> "用户名或密码不能为空"
                        is HttpException ->
                            when (it.code()) {
                                401 -> "用户名或密码错误"
                                else -> "网络错误"
                            }
                        is Errors.EmptyResultsError -> "请输入用户名或密码"
                        else -> "未知错误"
                    }
                )
            }
            .autoDisposable(this)
            .subscribe {
                applyErrorState(true, it)
            }

        initAutoLogin().autoDisposable(this).subscribe()
    }

    private fun initAutoLogin() =
        Single
            .zip(
                repo.prefsUser().firstOrError(),
                repo.prefsAutoLogin(),
                BiFunction { either: Either<Errors, LoginEntity>, autoLogin: Boolean ->
                    autoLogin to either
                }
            )
            .doOnSuccess { pair ->
                pair.second.fold({ error ->
                    applyState(error = error)
                }, { entity ->
                    applyState(
                        username = entity.username,
                        password = entity.password,
                        autoLogin = pair.first
                    )
                })
            }

    fun login() {
        when (username.value.isNullOrEmpty() || password.value.isNullOrEmpty()) {
            true -> applyState(error = Errors.EmptyInputError)
            false -> repo
                .login(username.value!!, password.value!!)
                .compose(globalHandleError())
                .map { either ->
                    either.fold({
                        SimpleViewState.error<LoginUser>(it)
                    }, {
                        SimpleViewState.result(it)
                    })
                }
                .startWith(SimpleViewState.loading())
                .startWith(SimpleViewState.idle())
                .onErrorReturn { SimpleViewState.error(it) }
                .autoDisposable(this)
                .subscribe { state ->
                    when (state) {
                        is SimpleViewState.Refreshing -> applyState(loadingLayout = CommonLoadingState.LOADING)
                        is SimpleViewState.Idle -> applyState()
                        is SimpleViewState.Error -> applyState(
                            loadingLayout = CommonLoadingState.ERROR,
                            error = state.error
                        )
                        is SimpleViewState.Result -> applyState(user = state.result)
                    }
                }
        }
    }

    private fun applyState(
        loadingLayout: CommonLoadingState = CommonLoadingState.IDLE,
        user: LoginUser? = null,
        error: Throwable? = null,
        username: String? = null,
        password: String? = null,
        autoLogin: Boolean = false
    ) {
        this.loadingLayout.postValue(loadingLayout)
        error?.let { this.error.postValue(it) }

        user?.let { this.userInfo.postValue(it) }

        username?.let { this.username.value = it }
        password?.let { this.password.value = it }

        this.autoLogin.postValue(autoLogin)
    }

    private fun applyErrorState(
        isShowMessage: Boolean = false,
        errorMessage: String? = null
    ) {
        this.isShowMessage.postValue(isShowMessage)
        errorMessage?.let { this.errorMessage.value = it }
    }
}