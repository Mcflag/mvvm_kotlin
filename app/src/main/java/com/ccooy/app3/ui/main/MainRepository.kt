package com.ccooy.app3.ui.main

import com.ccooy.app3.base.repository.BaseRepositoryBoth
import com.ccooy.app3.base.repository.ILocalDataSource
import com.ccooy.app3.base.repository.IRemoteDataSource
import com.ccooy.app3.data.model.response.Repo
import com.ccooy.app3.data.model.vo.Errors
import com.ccooy.app3.data.remote.GithubService
import com.ccooy.app3.ext.functional.Either
import com.ccooy.app3.util.net.globalHandleError
import com.ccooy.app3.util.rx.RxSchedulers
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

interface IReposRemoteDataSource : IRemoteDataSource {
    fun queryRepos(
        username: String,
        pageIndex: Int,
        perPage: Int,
        sort: String
    ): Flowable<Either<Errors, List<Repo>>>
}

interface IReposLocalDataSource : ILocalDataSource {
    fun saveReposToLocal(repos: Either<Errors, List<Repo>>): Completable
}

class MainRepository @Inject constructor(
    remote: IReposRemoteDataSource,
    local: IReposLocalDataSource
) : BaseRepositoryBoth<IReposRemoteDataSource, IReposLocalDataSource>(remote, local) {
    fun queryRepos(username: String, pageIndex: Int, perPage: Int, sort: String): Flowable<Either<Errors, List<Repo>>> =
        remoteDataSource.queryRepos(username, pageIndex, perPage, sort)
            .flatMap {
                localDataSource.saveReposToLocal(it).andThen(Flowable.just(it))
            }
}

class ReposRemoteDataSource @Inject constructor(private val githubService: GithubService) : IReposRemoteDataSource {
    override fun queryRepos(
        username: String,
        pageIndex: Int,
        perPage: Int,
        sort: String
    ): Flowable<Either<Errors, List<Repo>>> {
        return githubService.queryRepos(username, pageIndex, perPage, sort)
            .subscribeOn(RxSchedulers.io)
            .map {
                when (it.isEmpty()) {
                    true -> Either.left(Errors.EmptyResultsError)
                    false -> Either.right(it)
                }
            }
            .compose(globalHandleError())
    }
}

class ReposLocalDataSource @Inject constructor() : IReposLocalDataSource {
    override fun saveReposToLocal(repos: Either<Errors, List<Repo>>): Completable {
        return Completable.complete()
    }
}