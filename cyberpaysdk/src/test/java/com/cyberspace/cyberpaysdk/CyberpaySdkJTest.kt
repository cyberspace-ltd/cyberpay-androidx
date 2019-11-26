package com.cyberspace.cyberpaysdk

import com.cyberspace.cyberpaysdk.model.Card
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CyberpaySdkJTest {

    /* This is to test for validation. It's expected to fail */
    @Test
    fun validateInitialiseSdk(){
        CyberpaySdk.initialiseSdk("")
    }

    /*
        This is to test for card number validation.
        It's expected to failed
        */
    @Test
    fun validateCardNumber(){
        val card = Card()
        // insert an invalid card number
        card.cardNumber = "4678 9586 5584 9967"
    }

    /*
       This is to test for card expiry validation.
       It's expected to fail
       */
    @Test
    fun validateCardExpiryMonth(){
        val card = Card()
        // insert an invalid expiry month
        // this checks for months 1..12
        card.expiryMonth = 13
    }

    /*
       This is to test for card expiry validation.
       It's expected to fail
       */
    @Test
    fun validateCardExpiryYear(){
        val card = Card()
        // insert an invalid expiry year
        // this model checks for year from 19+ (from year of last sdk update)
        card.expiryYear = 12
    }



    // tests expected to pass
    // Test for master card detection
    //@MASTER CARD @5453 0100 0006 4261
    @Test
    fun isMasterCard(){
        val card = Card()
        // insert an valid master card number
        card.cardNumber = "5453 0100 0006 4261"
        assertThat(card.cardType?.name).isEqualTo("MASTERCARD")
        assertThat(card.cardType?.issuerName).isEqualTo("MASTER")
    }

    // tests expected to pass
    //@VISA CARD @4000 0000 0000 0622
    @Test
    fun isVisaCard(){
        val card = Card()
        // insert an valid visa card number
        card.cardNumber = "4000 0000 0000 0622"
        assertThat(card.cardType?.name).isEqualTo("VISA")
        assertThat(card.cardType?.issuerName).isEqualTo("VISA")
    }


    // tests expected to pass
    //@VERVE CARD
    // tests expected to pass
    //@VERVE CARD @5061 0300 0000 0000 928
    @Test
    fun isVerveCard(){
        val card = Card()
        // insert an valid verve card number
        card.cardNumber = "5061 0300 0000 0000 928"
        assertThat(card.cardType?.name).isEqualTo("VERVE")
        assertThat(card.cardType?.issuerName).isEqualTo("VERVE")
    }

    // tests expected to pass
    //@AMEX CARD



}