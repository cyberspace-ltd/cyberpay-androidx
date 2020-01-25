package com.cyberspace.cyberpaysdk.data.base.remote

internal class ApiResponse<T> {

    var message : String? = null
    var succeeded  = false
    var data : T? = null
    var code : String? = null
}