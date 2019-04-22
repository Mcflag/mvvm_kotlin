package com.ccooy.app3.base.view.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ccooy.app3.BR
import com.ccooy.app3.base.view.IView
import com.ccooy.app3.di.Injectable
import dagger.android.AndroidInjection

abstract class BaseActivity<B : ViewDataBinding> : AutoDisposeActivity(), Injectable, IView {

    protected lateinit var binding: B

    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        initBinding()
        initView()
    }

    open fun initView() {

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, layoutId)
        with(binding) {
            setVariable(BR.activity, this@BaseActivity)
            setLifecycleOwner(this@BaseActivity)
        }
    }
}