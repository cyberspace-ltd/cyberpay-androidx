package com.cyberspace.cyberpaysdk.data.transaction

import com.cyberspace.cyberpaysdk.enums.TransactionType
import com.cyberspace.cyberpaysdk.model.Card
import com.cyberspace.cyberpaysdk.model.Split

class Transaction {

    var card : Card? = null
    var transactionReference : String? = null
    internal set

    var merchantReference = ""
    var currency = "NGN"
    var returnUrl = "url"
    var channel = "None"
    var description = ""
    var amount = 0.0
    internal var otp = ""
    var type : TransactionType? = null
    var splits : Array<Split>? = null
    internal var key = ""
    var charge : Double? = 0.0

}