package com.cyberspace.cyberpaysdk.data.transaction.remote.response

import java.text.DecimalFormat

internal class Advice {

    var businessName: String? = ""
    var amount: Double? = 0.0
    var charge: Double? = 0.0
    var customerName: String? = ""
    var customerId: String? = ""
    var status: String? = ""

    val amountToPay: String?
    get() {
        val df = DecimalFormat("###,###.##")
        return df.format((amount!! + charge!!)/100).toString()
    }
}