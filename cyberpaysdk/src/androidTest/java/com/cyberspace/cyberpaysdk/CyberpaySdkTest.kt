package com.cyberspace.cyberpaysdk

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.test.platform.app.InstrumentationRegistry
import com.cyberspace.cyberpaysdk.model.Transaction
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class CyberpaySdkTest {

    @Before
    fun setUp() {

    }

    @Test
    fun initialiseSdk() {

    }

    @Test
    fun createTransaction(){

    }


    @Test
    fun chargeCard() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        var transaction: Transaction = Transaction()


    }

    @Test
    fun checkoutTransaction(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val transaction: Transaction = Transaction()
        transaction. amount = 100000.0 // amount in kobo

        CyberpaySdk.checkoutTransaction(appContext as AppCompatActivity, transaction, object : TransactionCallback() {
            override fun onSuccess(transaction: Transaction) {

            }

            override fun onError(transaction: Transaction, throwable: Throwable) {

            }

            override fun onValidate(transaction: Transaction) {

            }
        })

    }


}