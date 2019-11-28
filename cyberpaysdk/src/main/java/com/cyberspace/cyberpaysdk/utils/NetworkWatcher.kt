package com.cyberspace.cyberpaysdk.utils

import android.content.Context
import android.net.ConnectivityManager
import io.reactivex.Observable
import java.lang.Exception

internal class NetworkWatcher {

    fun connected(context: Context): Observable<Boolean>? {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo

        val connected: Boolean = networkInfo != null && networkInfo.isConnected
        return if (connected) Observable.just(true)
        else Observable.error<Boolean>(
           Exception(Constant.errorNetwork)
        )
    }


}