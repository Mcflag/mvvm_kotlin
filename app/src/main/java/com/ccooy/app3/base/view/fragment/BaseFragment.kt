package com.ccooy.app3.base.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ccooy.app3.BR
import com.ccooy.app3.base.view.IView
import com.ccooy.app3.di.Injectable
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection


abstract class BaseFragment<B : ViewDataBinding> : AutoDisposeFragment(), Injectable, IView {

    private var mRootView: View? = null

    protected lateinit var binding: B

    abstract val layoutId: Int

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)  //  在这里完成
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mRootView = LayoutInflater.from(context).inflate(layoutId, container, false)
        return mRootView!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(view)
        initView()
    }

    open fun initView() {

    }

    private fun initBinding(rootView: View) {
        binding = DataBindingUtil.bind(rootView)!!
        with(binding) {
            setVariable(BR.fragment, this@BaseFragment)
            setLifecycleOwner(this@BaseFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mRootView = null
        binding.unbind()
    }
}
