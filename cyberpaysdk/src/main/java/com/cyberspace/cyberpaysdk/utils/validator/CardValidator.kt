package com.cyberspace.cyberpaysdk.utils.validator

import com.cyberspace.cyberpaysdk.enums.CardType

object CardValidator {

    /**
     * Checks if the field is a valid credit card number.
     * @param card The card number to validate.
     * @return Whether the card number is valid.
     */

    fun isValid(cardIn: String): CardResult {
        val card = cardIn.replace("[^0-9]+".toRegex(), "") // remove all non-numerics
        if (card == null || card.length < 13 || card.length > 19) {
            return CardResult (card, false)
        }

        if (!luhnCheck(card)) {
            return CardResult (card, false)
        }

        val cc =
            CardType.gleanCompany(card) ?:  return CardResult (card, false)

        return CardResult (card, cc)
    }

    /**
     * Checks for a valid credit card number.
     * @param cardNumber Credit Card Number.
     * @return Whether the card number passes the luhnCheck.
     */
    internal fun luhnCheck(cardNumber: String): Boolean {
        // number must be validated as 0..9 numeric first!!
        val digits = cardNumber.length
        val oddOrEven = digits and 1
        var sum: Long = 0
        for (count in 0 until digits) {
            var digit = 0
            try {
                digit = Integer.parseInt(cardNumber[count] + "")
            } catch (e: NumberFormatException) {
                return false
            }

            if (count and 1 xor oddOrEven == 0) { // not
                digit *= 2
                if (digit > 9) {
                    digit -= 9
                }
            }
            sum += digit.toLong()
        }

        return if (sum == 0L) false else sum % 10 == 0L
    }
}
    
