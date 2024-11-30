package com.snofed.publicapp.api

import android.util.Log
import com.snofed.publicapp.R
import com.snofed.publicapp.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale
import javax.inject.Inject

/*class AuthInterceptor @Inject constructor() : Interceptor {

    @Inject
    lateinit var tokenManager: TokenManager

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        val token = tokenManager.getToken()
        request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }
}*/

class AuthInterceptor @Inject constructor() : Interceptor {

    @Inject
    lateinit var tokenManager: TokenManager
    val locale = Locale.getDefault().language
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        // Get the token and add it to the Authorization header
        val token = tokenManager.getToken()
        request.addHeader("Authorization", "Bearer $token")


        // Get the current locale (e.g., en-US, fr-FR)
        val locale = Locale.getDefault()
        val language = locale.toLanguageTag() // This gives the format "en-US", "fr-FR", etc.

        // Add the Accept-Language header dynamically based on the system's locale
        request.addHeader("Accept-Language", language)

        // Log the headers to verify they are being added correctly
        Log.d("AuthInterceptor", "Authorization header: Bearer $token")
        Log.d("AuthInterceptor", "Accept-Language header: $language")

        return chain.proceed(request.build())
    }
}
