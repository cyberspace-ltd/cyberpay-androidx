package com.cyberspace.cyberpaysdk.model

import com.cyberspace.cyberpaysdk.enums.CardType
import com.cyberspace.cyberpaysdk.utils.validator.CardValidator
import org.json.JSONObject
import java.security.InvalidParameterException

class Card {

    var cardNumber : String? = null
        set(value) {

            if(value.isNullOrEmpty()) throw InvalidParameterException("Invalid Card Number Found")
            val result = CardValidator.isValid(value)
            if(!result.isValid) throw InvalidParameterException("Invalid Card Number Found")

//            var last4Digits = ""

            this.otherInfo = result.cardType
        field  = result.cardNo
//        last4Digits = field?.substring(field?.length!! - 4, (field?.length!!))!!
    }

//    var expiry = ""
//     get() {
//          return String.format("%02d/%02d",expiryMonth, expiryYear)
//     }
//    private set

    var name = ""

    var cvv : String? = null
    set(value) {
        if(value != null) {
            if(value.length > 3 || value.length < 3) throw InvalidParameterException("Invalid Card Cvv Found")
        }
        else throw InvalidParameterException("Invalid Card Cvv Found")

        field = value
    }

    var expiryMonth : Int = 0
    set(value) {
        if(value > 12 || value < 1) throw InvalidParameterException("Invalid Card Expiry Month")
        field = value
    }

    var expiryYear : Int = 0
        set(value) {
            if(value > 99 || value < 19 ) throw InvalidParameterException("Invalid Card Expiry Year")
            field = value
        }

    var otherInfo : CardType? = null

    internal var cardPin : String? = null

    fun toJson () : JSONObject {
        //val param = mutableMapOf<String, Any?>()
        val param= HashMap<String, Any?>()
        param["Name"] = ""
        param["ExpiryMonth"] = expiryMonth
        param["ExpiryYear"] = expiryYear
        param["CardNumber"] = cardNumber
        param["CVV"] = cvv
        param["CardPin"] = cardPin

        return JSONObject(param)
    }

}