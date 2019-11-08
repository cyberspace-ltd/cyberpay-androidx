package com.cyberspace.cyberpaysdk.utils.validator

import com.cyberspace.cyberpaysdk.enums.CardType

internal class CardResult {

    var isValid = false
    var cardType: CardType? = null
    var cardNo: String? = null

    constructor(cardNo: String, status : Boolean) {
        this.cardNo = cardNo
        this.isValid = status
    }

    constructor(cardNo: String, cardType: CardType) {
        this.cardNo = cardNo
        this.isValid = true
        this.cardType = cardType
    }
}
