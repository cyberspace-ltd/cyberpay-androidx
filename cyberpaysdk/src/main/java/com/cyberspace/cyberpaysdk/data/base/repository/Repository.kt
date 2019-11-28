package com.cyberspace.cyberpaysdk.data.base.repository

import com.cyberspace.cyberpaysdk.data.base.remote.ApiClient
import com.cyberspace.cyberpaysdk.data.base.remote.Service
import com.cyberspace.cyberpaysdk.utils.NetworkWatcher
import io.reactivex.Observable

internal class Repository<T> { 
    
    private val watcher = NetworkWatcher()
    private  var service : T? = null
    private  var client : Class<T>? = null
    private var apiClient : Service? = null



    fun  build() : Repository<T> {
        apiClient = ApiClient()
        service = apiClient?.create(client!!)
        return  this
    }

}