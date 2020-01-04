package com.cyberspace.cyberpaysdk.data.transaction.remote.response

internal class ChargeBank {
    var status: String? = null
    var message: String? = null
    var responseAction: String? = null
    var requiredParameters : List<RequiredParameters>? = null
    var DefaultCollection : String? = ""
}

internal class RequiredParameters {
    var param : String? = ""
    var message : String? = ""
}