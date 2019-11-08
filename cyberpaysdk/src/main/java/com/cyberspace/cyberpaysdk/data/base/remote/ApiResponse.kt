package com.cyberspace.cyberpaysdk.data.base.remote

class ApiResponse<T> {

    private var message : String? = null
    private var succeeded  = false
    private var data : T? = null
}