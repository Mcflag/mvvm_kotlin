package com.ccooy.app3.util.net

import android.util.Base64
import okhttp3.Interceptor
import okhttp3.Response

class BasicAuthInterceptor : Interceptor {

    private var username: String = ""
    private var password: String = ""

    override fun intercept(chain: Interceptor.Chain): Response {
        val basic = "$username:$password".let {
            "basic " + Base64.encodeToString(it.toByteArray(), Base64.NO_WRAP)
        }

        val request = chain.request().let {
            it.newBuilder()
                .header("Authorization", basic)
                .method(it.method(), it.body())
                .build()
        }
        return chain.proceed(request)
    }

    fun login(username: String, password: String) {
        this.username = username
        this.password = password
    }

    fun logout() {
        this.username = ""
        this.password = ""
    }
}