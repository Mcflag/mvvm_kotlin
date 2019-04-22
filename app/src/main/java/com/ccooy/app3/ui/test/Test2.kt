package com.ccooy.app3.ui.test

import com.ccooy.app3.R
import com.ccooy.app3.base.view.activity.BaseActivity
import com.ccooy.app3.data.local.prefs.PreferencesRepository
import com.ccooy.app3.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_test.*
import javax.inject.Inject

class Test2 : BaseActivity<ActivityMainBinding>() {

    @Inject
    lateinit var repo: PreferencesRepository

    override val layoutId: Int = R.layout.activity_test

    override fun initView() {
        repo.username = "aaaaa"
        repo.password = "bkaskdkawfljqwerk"

        text.text = "${repo.username}: ${repo.password}"
    }
}