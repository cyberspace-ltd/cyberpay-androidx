package com.cyberspace.cyberpaysdk.data.model

import com.cyberspace.cyberpaysdk.enums.CardType
import com.cyberspace.cyberpaysdk.utils.validator.CardResult
import com.cyberspace.cyberpaysdk.utils.validator.CardValidator
import java.security.InvalidParameterException

class Card {

    var cardNumber : String? = null
    set(value) {
        if(value.isNullOrEmpty()) throw InvalidParameterException("Invalid Card Number Found")
        val result = CardValidator.isValid(value)
        if(!result.isValid) throw InvalidParameterException("Invalid Card Number Found")

        this.cardType = result.cardType
        field  = value
    }

    var cardName : String? = null
    var cardCvv : String? = null
    var cardEmail : String? = null

    var expiryMonth : Int = 0
    set(value) {
        if(value > 12 || value<1) throw InvalidParameterException("Invalid Card Expiry Month Found")
        field = value
    }

    var expiryYear : Int = 0
        set(value) {
            if(value > 99 || value<1 ) throw InvalidParameterException("Invalid Card Expiry Year Found")
            field = value
        }

    var cardType : CardType? = null

    private fun validateCard() : Boolean {
        return true
    }



}