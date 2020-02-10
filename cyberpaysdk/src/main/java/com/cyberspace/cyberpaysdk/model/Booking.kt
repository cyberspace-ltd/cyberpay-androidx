package com.cyberspace.cyberpaysdk.model

import java.io.Serializable
import java.text.DecimalFormat

class Booking : Serializable {
    var description = "Cyberpay Transaction"
    var amount = 0.0
    var customerEmail = ""
    var customerName = ""
    var phoneNumber = ""
    var splits : List<Split>? = null


    val amountToPay : String?
    get(){
        val df = DecimalFormat("###,##.##")
        return df.format((amount)/100).toString()
    }
}