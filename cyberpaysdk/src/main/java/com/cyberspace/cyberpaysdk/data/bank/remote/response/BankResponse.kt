package com.cyberspace.cyberpaysdk.data.bank.remote.response

internal class BankResponse {
    var id = 0
    var bankCode = ""
    var bankName = ""
    var isActive : Boolean? = false
    var providerCode : Int?  = 0
    var bankProviders : MutableList<Any>? = mutableListOf()
    var processingType : String? = ""
    var externalRedirectUrl : String? = ""

}