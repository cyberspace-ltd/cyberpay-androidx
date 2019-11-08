package com.cyberspace.cyberpaysdk.data.transaction

import com.cyberspace.cyberpaysdk.model.Card
import com.cyberspace.cyberpaysdk.model.Split

class Transaction {

    var card : Card? = null
    var transactionReference : String? = null
    internal set

    var merchantReference : String? = null
    var currency = "NGN"
    var returnUrl = ""
    var channel = "None"
    var description = ""
    var amount = 0.0
    internal var otp = ""

    var splits : Array<Split>? = null


}