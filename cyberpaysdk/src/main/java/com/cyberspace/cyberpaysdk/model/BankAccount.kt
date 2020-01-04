package com.cyberspace.cyberpaysdk.model

import com.cyberspace.cyberpaysdk.data.bank.remote.response.BankResponse

internal class BankAccount {
    var bank: BankResponse? = null
    var accountNumber: String? = ""
    var accountName: String? = ""
    var dateOfBirth : String? = ""
    var bvn : String? =  ""
}