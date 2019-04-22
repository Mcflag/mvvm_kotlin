package com.ccooy.app3.ui.test

import com.ccooy.app3.R
import com.ccooy.app3.base.view.activity.BaseActivity
import com.ccooy.app3.util.net.BasicAuthInterceptor
import com.ccooy.app3.data.remote.GithubService
import com.ccooy.app3.databinding.ActivityMainBinding
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_test.*
import javax.inject.Inject

class Test3 : BaseActivity<ActivityMainBinding>() {

    @Inject
    lateinit var repo: GithubService

    @Inject
    lateinit var basicAuthInterceptor: BasicAuthInterceptor

    override val layoutId: Int = R.layout.activity_test

    override fun initView() {

        basicAuthInterceptor.login("", "")

        repo.login()
            .subscribeOn(Schedulers.io())
            .flatMap {
                repo.queryRepos("googlesamples", 1, 10, "full_name")
            }
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(scopeProvider)
            .subscribe({
                text.text = it[0].fullName
            }, {
                text.text = it.message
            })
    }
}