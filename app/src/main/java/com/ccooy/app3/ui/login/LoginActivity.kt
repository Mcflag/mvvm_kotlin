package com.ccooy.app3.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ccooy.app3.R
import com.ccooy.app3.base.loadings.CommonLoadingViewModel
import com.ccooy.app3.base.view.activity.BaseActivity
import com.ccooy.app3.databinding.ActivityLoginBinding
import com.ccooy.app3.ext.livedata.toReactiveStream
import com.ccooy.app3.ui.main.MainActivity
import com.uber.autodispose.autoDisposable
import javax.inject.Inject

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override val layoutId: Int = R.layout.activity_login

    @Inject
    lateinit var loadingViewModel: CommonLoadingViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: LoginViewModel

    override fun initView() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
        viewModel.userInfo
            .toReactiveStream()
            .doOnNext { toMain() }
            .autoDisposable(scopeProvider)
            .subscribe()

        viewModel.loadingLayout
            .toReactiveStream()
            .doOnNext { loadingViewModel.applyState(it) }
            .autoDisposable(scopeProvider)
            .subscribe()
    }

    fun login() = viewModel.login()

    fun toMain() = MainActivity.launch(this)

    companion object {
        fun launch(activity: AppCompatActivity) =
            activity.apply {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
    }
}