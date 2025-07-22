// file: data/remote/AuthInterceptor.kt
package com.fauzan.zainullah.bookingkonsultasiapp.data.remote

import com.fauzan.zainullah.bookingkonsultasiapp.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        SessionManager.getToken()?.let { token ->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}
