package com.cyberspace.cyberpaysdk.enums

enum class CardType  constructor(private val regex: String, val issuerName: String) {

    VISA("^4[0-9]{12}(?:[0-9]{3})?$", "VISA"),
    MASTERCARD("^5[1-5][0-9]{14}$", "MASTER"),
    AMEX("^3[47][0-9]{13}$", "AMEX"),
    DINERS("^3(?:0[0-5]|[68][0-9])[0-9]{11}$", "DINERS"),
    DISCOVER("^6(?:011|5[0-9]{2})[0-9]{12}$", "DISCOVER"),
    JCB("^(?:2131|1800|35\\d{3})\\d{11}$", "JCB");

    fun matches(card: String): Boolean {
        return card.matches(this.regex.toRegex())
    }

    companion object {

        /**
         * get an enum from a card number
         * @param card
         * @return
         */
        fun gleanCompany(card: String): CardType? {
            for (cc in CardType.values()) {
                if (cc.matches(card)) {
                    return cc
                }
            }
            return null
        }

        /**
         * get an enum from an issuerName
         * @param issuerName
         * @return
         */
        fun gleanCompanyByIssuerName(issuerName: String): CardType? {
            for (cc in CardType.values()) {
                if (cc.issuerName == issuerName) {
                    return cc
                }
            }
            return null
        }
    }
}
