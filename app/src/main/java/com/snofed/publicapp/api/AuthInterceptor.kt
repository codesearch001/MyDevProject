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

        val token = tokenManager.getToken()
        request.addHeader("Authorization", "Bearer $token")


        val locale = Locale.getDefault()
        var language = locale.toLanguageTag()

        if(language.contains("en"))
            language = "en-US"
        else if(language.contains("de"))
            language = "de-DE"
        else if(language.contains("sv"))
            language = "sv-SE"
        else
            language = "sv-SE"

        request.addHeader("Accept-Language", language)

        Log.d("AuthInterceptor", "Authorization header: Bearer $token")
        Log.d("AuthInterceptor", "Accept-Language header: $language")

        return chain.proceed(request.build())
    }
}
