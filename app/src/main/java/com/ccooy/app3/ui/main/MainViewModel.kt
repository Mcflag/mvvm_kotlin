package com.ccooy.app3.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.ccooy.app3.base.viewmodel.BaseViewModel
import com.ccooy.app3.base.viewstate.SimpleViewState
import com.ccooy.app3.data.model.response.Repo
import com.ccooy.app3.ext.livedata.toReactiveStream
import com.ccooy.app3.ext.paging.IntPageKeyedData
import com.ccooy.app3.ext.paging.IntPageKeyedDataSource
import com.ccooy.app3.ext.paging.Paging
import com.uber.autodispose.autoDisposable
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repo: MainRepository
) : BaseViewModel() {

    private val events: MutableLiveData<List<Repo>> = MutableLiveData()
    val sort: MutableLiveData<String> = MutableLiveData()
    val refreshing: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<Throwable> = MutableLiveData()
    val pagedList = MutableLiveData<PagedList<Repo>>()

    init {
        Completable.mergeArray(
            refreshing.toReactiveStream()
                .filter { it }
                .doOnNext { initReposList() }
                .ignoreElements(),
            sort.toReactiveStream()
                .distinctUntilChanged()
                .startWith(sortByLetter)
                .doOnNext { refreshing.postValue(true) }
                .ignoreElements()
        ).autoDisposable(this)
            .subscribe()
    }

    private fun initReposList() {
        Paging.buildReactiveStream(intPageKeyedDataSource = IntPageKeyedDataSource(
            loadInitial = {
                queryReposRefreshAction(it.requestedLoadSize).flatMap { state ->
                    when (state) {
                        is SimpleViewState.Result -> Flowable.just(
                            IntPageKeyedData.build(
                                data = state.result,
                                pageIndex = 1,
                                hasAdjacentPageKey = state.result.size == it.requestedLoadSize
                            )
                        )
                        else -> Flowable.empty()
                    }
                }
            },
            loadAfter = {
                queryReposAction(it.key, it.requestedLoadSize).flatMap { state ->
                    when (state) {
                        is SimpleViewState.Result -> Flowable.just(
                            IntPageKeyedData.build(
                                data = state.result,
                                pageIndex = it.key,
                                hasAdjacentPageKey = state.result.size == it.requestedLoadSize
                            )
                        )
                        else -> Flowable.empty()
                    }
                }
            }
        ))
            .doOnNext { pagedList.postValue(it) }
            .autoDisposable(this)
            .subscribe()
    }

    private fun queryReposAction(pageIndex: Int, pageSize: Int): Flowable<SimpleViewState<List<Repo>>> =
        repo.queryRepos("googlesamples", pageIndex, pageSize, sort.value ?: sortByLetter)
            .map { either ->
                either.fold({
                    SimpleViewState.error<List<Repo>>(it)
                }, {
                    SimpleViewState.result(it)
                })
            }
            .onErrorReturn { SimpleViewState.error(it) }

    private fun queryReposRefreshAction(pageSize: Int): Flowable<SimpleViewState<List<Repo>>> =
        queryReposAction(1, pageSize)
            .startWith(SimpleViewState.loading())
            .startWith(SimpleViewState.idle())
            .doOnNext { state ->
                when (state) {
                    is SimpleViewState.Refreshing -> applyState()
                    is SimpleViewState.Idle -> applyState()
                    is SimpleViewState.Error -> applyState(error = state.error)
                    is SimpleViewState.Result -> applyState(events = state.result)
                }
            }
            .doFinally { refreshing.postValue(false) }

    private fun applyState(
        events: List<Repo>? = null,
        error: Throwable? = null
    ) {
        this.error.postValue(error)
        this.events.postValue(events)
    }

    companion object {
        const val sortByCreated: String = "created"
        const val sortByUpdate: String = "updated"
        const val sortByLetter: String = "full_name"
    }
}