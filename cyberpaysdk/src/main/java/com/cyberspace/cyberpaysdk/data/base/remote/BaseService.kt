package com.cyberspace.cyberpaysdk.data.base.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class BaseService  @Inject constructor() : Service {

    private var retrofit: Retrofit? = null
    private val baseUrl = "http://api.acme.international/"

    private fun getRetrofitInstance(): Retrofit? {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(
                    OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .build()
                )
                .build()
        }
        return retrofit
    }

    override fun <T> create(classService: Class<T>): T? {

        return getRetrofitInstance()?.create(classService)
    }
}