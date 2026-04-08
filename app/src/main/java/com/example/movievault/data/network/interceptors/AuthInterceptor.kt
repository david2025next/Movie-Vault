package com.example.movievault.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor (
    private val token : String
) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestWithToken = originalRequest.newBuilder()
            .apply {
                header("Authorization", "Bearer $token")
            }
            .build()
        return chain.proceed(requestWithToken)
    }
}