package com.cyberspace.cyberpaysdk.data.bank

internal class Bank {
    var id = 0
    var bankCode = ""
    var bankName = ""
    var isActive : Boolean? = false
    var providerCode : Int? = 0
    var processType : MutableList<Any>? = mutableListOf()

}