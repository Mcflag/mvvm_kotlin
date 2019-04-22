package com.ccooy.app3.ui.splash

import androidx.lifecycle.MutableLiveData
import com.ccooy.app3.base.viewmodel.BaseViewModel
import com.uber.autodispose.autoDisposable
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val repo: SplashRepository
) : BaseViewModel() {

    val picUrl: MutableLiveData<Int> = MutableLiveData()
    val timer: MutableLiveData<Int> = MutableLiveData()
    val complete: MutableLiveData<Boolean> = MutableLiveData()

    init {
        getUrl()
        downCount()
    }

    private fun downCount() {
        Observable.interval(1000, 1000, TimeUnit.MILLISECONDS)
            .take(3)
            .map { 2 - it }
            .autoDisposable(this)
            .subscribe({
                applyState(time = it.toInt())
            }, {

            }, {
                applyState(isComplete = true)
            })
    }

    private fun getUrl() {
        repo.getPicUrl()
            .autoDisposable(this)
            .subscribe { id ->
                applyState(id)
            }
    }

    private fun applyState(
        drawableId: Int? = null,
        time: Int = 3,
        isComplete: Boolean = false
    ) {
        drawableId?.let {
            this.picUrl.postValue(it)
        }
        this.complete.postValue(isComplete)
        if (isComplete) {
            this.timer.postValue(0)
        } else {
            this.timer.postValue(time)
        }
    }
}