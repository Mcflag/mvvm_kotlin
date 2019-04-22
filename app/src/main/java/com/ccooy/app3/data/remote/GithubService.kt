package com.ccooy.app3.data.remote

import com.ccooy.app3.data.model.response.LoginUser
import com.ccooy.app3.data.model.response.ReceivedEvent
import com.ccooy.app3.data.model.response.Repo
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {

    @GET("user")
    fun login(): Flowable<LoginUser>

    @GET("users/{username}/repos?")
    fun queryRepos(
        @Path("username") username: String,
        @Query("page") pageIndex: Int,
        @Query("per_page") perPage: Int,
        @Query("sort") sort: String
    ): Flowable<List<Repo>>

    @GET("users/{username}/received_events?")
    fun queryReceivedEvents(
        @Path("username") username: String,
        @Query("page") pageIndex: Int,
        @Query("per_page") perPage: Int
    ): Flowable<List<ReceivedEvent>>
}