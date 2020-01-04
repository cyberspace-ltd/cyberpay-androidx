package com.cyberspace.cyberpaysdk.data.base.remote

import com.cyberspace.cyberpaysdk.CyberpaySdk
import com.cyberspace.cyberpaysdk.enums.Mode
import com.cyberspace.cyberpaysdk.utils.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class ApiClient : Service {

    private var retrofit: Retrofit? = null
    private val urlDebug = "https://payment-api.staging.cyberpay.ng/api/v1/"
    private val urlLive = "https://payment-api.cyberpay.ng/api/v1/"
    private var loggingInterceptor = HttpLoggingInterceptor()

    private fun getRetrofitInstance(): Retrofit? {

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(when(CyberpaySdk.envMode){
                    Mode.Debug -> urlDebug
                    Mode.Live -> urlLive
                })
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor)
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
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