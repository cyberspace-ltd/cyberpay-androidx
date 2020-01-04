package com.cyberspace.cyberpaysdk.model

import com.cyberspace.cyberpaysdk.enums.TransactionType
import java.security.InvalidParameterException

class Transaction {

    var card : Card? = null
    var reference  = ""
    internal set

    var merchantReference = ""
    var currency = "NGN"
    var returnUrl = "url"
    var channel = "None"
    var description = "Cyberpay Transaction"
    var amount = 0.0
    internal var otp = ""
    var type : TransactionType? = null
    var splits : Array<Split>? = null
    internal var key = ""
    var charge : Double? = 0.0
    var customerEmail = ""
    set(value) {
        if(!value.contains("@")) throw InvalidParameterException("Invalid Customer Email Found")
        field = value
    }

    internal var bankAccount: BankAccount? = null

    internal val clientType = "Mobile"
    var message = ""

}