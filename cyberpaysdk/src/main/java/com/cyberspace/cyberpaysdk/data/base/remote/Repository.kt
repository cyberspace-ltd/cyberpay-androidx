package com.cyberspace.cyberpaysdk.data.base.remote

import io.reactivex.Observable

internal abstract class Repository<T> {

    protected var mService: T? = null
    private var serviceClass: Class<T>? = null
    val apiClient = ApiClient()

    fun build() : Repository<T> {
        mService = apiClient.create(serviceClass!!)
        return this
    }

    fun call (request : Observable<ApiResponse<Any>>) : Observable<ApiResponse<Any>> {
       return request.onErrorResumeNext { throwable : Throwable ->
            Observable.error(ErrorHandler.getError(throwable))
        }
    }


}