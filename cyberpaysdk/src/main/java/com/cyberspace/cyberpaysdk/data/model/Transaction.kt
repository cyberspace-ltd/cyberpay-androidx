package com.cyberspace.cyberpaysdk.data.model

class Transaction {

    var card : Card? = null
    var reference : String? = null
    var currency = "NGN"
    var returnUrl = ""
    var channel = "None"
    var description = ""
    var amount = 0.0
    private var otp = ""

}