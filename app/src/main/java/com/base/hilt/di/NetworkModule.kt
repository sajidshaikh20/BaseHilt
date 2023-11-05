package com.base.hilt.di

import com.base.hilt.BuildConfig
import com.base.hilt.ConfigFiles
import com.base.hilt.network.ApiInterface
import com.base.hilt.network.HttpHandleIntercept
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
class NetworkModule {
    /**
     * Generate Retrofit Client
     */
    @Provides
    @RetrofitStore
    fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val builder = Retrofit.Builder()
        builder.baseUrl(ConfigFiles.DEV_BASE_URL)
        builder.addConverterFactory(JacksonConverterFactory.create())
        builder.client(okHttpClient)
        return builder.build()
    }

    @Provides
    @ViewModelScoped
    fun provideApiInterface(@RetrofitStore retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)


    @Provides
    fun provideHttpHandleIntercept(): HttpHandleIntercept = HttpHandleIntercept()


    /**
     * generate OKhttp client
     */
    @Provides
    fun getOkHttpClient(httpHandleIntercept: HttpHandleIntercept): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) logging.level = HttpLoggingInterceptor.Level.BODY
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(logging)
        builder.readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(httpHandleIntercept)
            .build()

        return builder.build()
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RetrofitStore