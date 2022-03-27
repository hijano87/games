package com.hijano.games.data

import com.hijano.games.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader("Authorization", "Bearer ${BuildConfig.BEARER_TOKEN}")
        requestBuilder.addHeader("Client-ID", BuildConfig.CLIENT_ID)
        return chain.proceed(requestBuilder.build())
    }
}
