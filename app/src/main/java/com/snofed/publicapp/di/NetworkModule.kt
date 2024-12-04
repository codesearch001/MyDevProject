package com.snofed.publicapp.di

import com.snofed.publicapp.api.AuthInterceptor
import com.snofed.publicapp.api.ClientAPI
import com.snofed.publicapp.api.FeedBackUserAPI
import com.snofed.publicapp.api.MembershipApi
import com.snofed.publicapp.api.UserAPI
import com.snofed.publicapp.utils.Constants
import com.snofed.publicapp.utils.ServiceUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    @Named("UserApiBaseUrl")
    fun providesRetrofit(): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(ServiceUtil.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    @Named("ClientApiBaseUrl")
    fun providesClientRetrofit(): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(ServiceUtil.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }


    @Singleton
    @Provides
    @Named("FeedBackApiBaseUrl")
    fun provideFeedBackRetrofit(): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(ServiceUtil.FEED_BACK_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    @Named("MemberShipApiBaseUrl")
    fun provideMembershipRetrofit(): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(ServiceUtil.MEMBERSHIP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }



    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: AuthInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(loggingInterceptor)
            .callTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }
        /*return OkHttpClient.Builder().addInterceptor(interceptor)
            .addInterceptor(loggingInterceptor) // Add logging interceptor
            .connectTimeout(30, TimeUnit.SECONDS) // Set custom timeouts if needed
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }*/


    @Singleton
    @Provides
    @Named("UserAPI")
    fun providesUserAPI(@Named("UserApiBaseUrl")retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): UserAPI {
        return retrofitBuilder.client(okHttpClient).build().create(UserAPI::class.java)
    }
    @Singleton
    @Provides
    @Named("ClientAPI")
    fun providesClientAPI(@Named("ClientApiBaseUrl")retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): ClientAPI {
        return retrofitBuilder.client(okHttpClient).build().create(ClientAPI::class.java)
    }

    @Singleton
    @Provides
    @Named("FeedBackUserAPI")
    fun providesFeedBackUserAPI(@Named("FeedBackApiBaseUrl") retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): FeedBackUserAPI {
        return retrofitBuilder.client(okHttpClient).build().create(FeedBackUserAPI::class.java)
    }

    @Singleton
    @Provides
    @Named("MembershipApi")
    fun providesMembershipUserAPI(@Named("MemberShipApiBaseUrl") retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): MembershipApi {
        return retrofitBuilder.client(okHttpClient).build().create(MembershipApi::class.java)
    }

    /*@Singleton
    @Provides
    @Named("FeedBackUserAPI")
    fun providesFeedBackUserAPI(@Named("FeedBackApiBaseUrl") baseUrl: String, okHttpClient: OkHttpClient): FeedBackUserAPI {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FeedBackUserAPI::class.java)
    }*/



    /* @Singleton
     @Provides
     @Named("NoteAPI")
     fun providesNoteAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): NoteAPI {
         return retrofitBuilder.client(okHttpClient).build().create(NoteAPI::class.java)
     }*/


}