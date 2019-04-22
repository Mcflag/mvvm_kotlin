package com.ccooy.app3.ui.splash

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ccooy.app3.R
import com.ccooy.app3.base.view.activity.BaseActivity
import com.ccooy.app3.databinding.ActivitySplashBinding
import com.ccooy.app3.ext.livedata.toReactiveStream
import com.ccooy.app3.ui.login.LoginActivity
import com.ccooy.app3.ui.main.MainActivity
import com.uber.autodispose.autoDisposable
import javax.inject.Inject

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override val layoutId: Int = R.layout.activity_splash

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: SplashViewModel

    @Inject
    lateinit var repo: SplashRepository

    override fun initView() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SplashViewModel::class.java)
        viewModel.complete
            .toReactiveStream()
            .doOnNext {
                if (it) toLogin()
            }
            .autoDisposable(scopeProvider)
            .subscribe()
    }

    fun toLogin() = LoginActivity.launch(this)

    companion object {
        private const val TAG = "SplashActivity"
    }
}