package com.cyberspace.cyberpaysdk.model

import android.util.Log
import com.cyberspace.cyberpaysdk.enums.CardType
import com.cyberspace.cyberpaysdk.utils.validator.CardValidator
import org.json.JSONObject
import java.security.InvalidParameterException

class Card {

    var number : String? = null
    set(value) {
        if(value.isNullOrEmpty()) throw InvalidParameterException("Invalid Card Number Found")
        val result = CardValidator.isValid(value)
        if(!result.isValid) throw InvalidParameterException("Invalid Card Number Found")

        this.cardType = result.cardType
        field  = result.cardNo
    }

    var name = ""
    var email = ""
    var address = ""
    var last4Digits = ""
    var phoneNumber = ""

    var cvv : String? = null
    set(value) {
        if(value != null) {
            if(value.length > 3 || value.length <3) throw InvalidParameterException("Invalid Card Cvv Found")
        }
        else throw InvalidParameterException("Invalid Card Cvv Found")

        field = value
    }

    var expiryMonth : Int = 0
    set(value) {
        if(value > 12 || value<1) throw InvalidParameterException("Invalid Card Expiry Month")
        field = value
    }

    var expiryYear : Int = 0
        set(value) {
            if(value > 99 || value<19 ) throw InvalidParameterException("Invalid Card Expiry Year")
            field = value
        }

    var cardType : CardType? = null

    internal var pin : String? = null

    fun toJson () : JSONObject {
        //val param = mutableMapOf<String, Any?>()
        val param= HashMap<String, Any?>()
        param["Name"] = ""
        param["ExpiryMonth"] = expiryMonth
        param["ExpiryYear"] = expiryYear
        param["CardNumber"] = number
        param["CVV"] = cvv
        param["CardPin"] = pin

        return JSONObject(param)
    }

}