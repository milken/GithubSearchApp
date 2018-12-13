package com.example.milken.githubsearchapp.di

import com.example.milken.githubsearchapp.utils.ErrorParser
import com.example.milken.githubsearchapp.utils.RxUtil
import com.example.milken.githubsearchapp.utils.SchedulerProvider
import com.example.milken.githubsearchapp.utils.SchedulerProviderImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import okhttp3.logging.HttpLoggingInterceptor
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@DebugOpenClass
@Module
class NetModule {

    @Provides
    @Singleton
    fun getRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun getsMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Provides
    @Singleton
    fun getOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor {
                val originalRequest = it.request()
                val updatedRequest = originalRequest.newBuilder()
                    .addHeader("Accept", "application/vnd.github.v3+json")
                    .addHeader("User-Agent", "https://api.github.com/meta")
                    .method(originalRequest.method(), originalRequest.body())
                    .build()

                it.proceed(updatedRequest)
            }
            .build()
    }

    @Provides
    @Singleton
    fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun getSchedulerProvider(): SchedulerProvider {
        return SchedulerProviderImpl()
    }

    @Provides
    fun getCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @Provides
    @Singleton
    fun getRxUtil(schedulerProvider: SchedulerProvider): RxUtil {
        return RxUtil(schedulerProvider.io())
    }

    @Provides
    @Singleton
    fun getErrorParser(): ErrorParser {
        return ErrorParser()
    }
}