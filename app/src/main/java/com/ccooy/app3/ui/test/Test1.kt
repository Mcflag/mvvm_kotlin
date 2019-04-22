package com.ccooy.app3.ui.test

import com.ccooy.app3.R
import com.ccooy.app3.base.view.activity.BaseActivity
import com.ccooy.app3.data.local.db.repo.LoginDatabaseRepository
import com.ccooy.app3.data.model.db.LoginEntity
import com.ccooy.app3.databinding.ActivityMainBinding
import com.uber.autodispose.autoDisposable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_test.*
import javax.inject.Inject

class Test1 : BaseActivity<ActivityMainBinding>() {

    @Inject
    lateinit var repo: LoginDatabaseRepository

    override val layoutId: Int = R.layout.activity_test

    override fun initView() {
        repo.insertUser(LoginEntity(1, "aaaa", "ccsbsdfaadsf"))
            .subscribeOn(Schedulers.io())
            .flatMap {
                repo.findUserByName("aaaa")
            }
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(scopeProvider)
            .subscribe {
                text.text = it.password
            }
    }
}