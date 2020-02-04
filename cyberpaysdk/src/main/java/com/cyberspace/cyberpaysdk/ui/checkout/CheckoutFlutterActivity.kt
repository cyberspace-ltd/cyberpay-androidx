package com.cyberspace.cyberpaysdk.ui.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.cyberspace.cyberpaysdk.CyberpaySdk
import com.cyberspace.cyberpaysdk.R
import com.cyberspace.cyberpaysdk.TransactionCallback
import com.cyberspace.cyberpaysdk.model.Booking
import com.cyberspace.cyberpaysdk.model.Card
import com.cyberspace.cyberpaysdk.model.Transaction
import com.cyberspace.cyberpaysdk.utils.fonts.Bold

class CheckoutFlutterActivity : AppCompatActivity() {

    val trans = Transaction()
    private lateinit var customer_email_value : TextView
    private lateinit var customer_name_value : TextView
    private lateinit var customer_phone_value : TextView

    private  lateinit var button : Bold

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_flutter)

        customer_email_value = findViewById(R.id.customer_email_value)
        customer_name_value = findViewById(R.id.customer_name_value)
        customer_phone_value = findViewById(R.id.customer_phone_value)
        button = findViewById(R.id.make_payment)

        val intent = intent
        if(intent != null){
            val booking = intent.getSerializableExtra("safejourney") as Booking

            val card = Card()

            trans.amount = booking.amount
            trans.customerEmail = booking.customerEmail

//        trans.merchantReference = "JJHJRJOI39OHNKDJIUE" // Optional, will be auto generate by SDK if not present
            trans.description = booking.description
            // trans.dateOfBirth = "120988"
            trans.card = card

            button.text = String.format("%s%s", button.text, trans.amountToPay)
            customer_name_value.text= String.format("%s", booking.customerName)
            customer_email_value.text= String.format("%s", booking.customerEmail)
            customer_phone_value.text= String.format("%s", booking.phoneNumber)

        }
    }

    fun proceed(view: View) {

        val callback = object : TransactionCallback() {
            override fun onSuccess(transaction: Transaction) {
                Log.e("RESPONSE", "SUCCESSFUL")
            }

            override fun onError(transaction: Transaction, throwable: Throwable) {
                Log.e("ERROR", throwable.message!!)
            }

            override fun onValidate(transaction: Transaction) {
                //
            }
        }


        CyberpaySdk.checkoutTransaction(this, trans, callback )

    }
}
