package com.ccooy.app3.base.loadings

import androidx.lifecycle.MutableLiveData

interface ILoadingDelegate {

    fun loadingState(): MutableLiveData<CommonLoadingState>

    fun applyState(state: CommonLoadingState)
}

enum class CommonLoadingState {

    ERROR, EMPTY, LOADING, IDLE

}