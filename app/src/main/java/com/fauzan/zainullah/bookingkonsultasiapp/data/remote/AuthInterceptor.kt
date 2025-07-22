// file: data/remote/AuthInterceptor.kt
package com.fauzan.zainullah.bookingkonsultasiapp.data.remote

import com.fauzan.zainullah.bookingkonsultasiapp.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // Ambil token dari SessionManager
        SessionManager.getToken()?.let { token ->
            // Tambahkan header Authorization jika token ada
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}
