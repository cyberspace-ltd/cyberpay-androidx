package com.cyberspace.cyberpayapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.cyberspace.cyberpaysdk.CyberpaySdk
import com.cyberspace.cyberpaysdk.TransactionCallback
import com.cyberspace.cyberpaysdk.model.Transaction
import com.cyberspace.cyberpaysdk.model.Card
import com.google.android.material.floatingactionbutton.FloatingActionButton


import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.security.InvalidParameterException
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setSupportActionBar(toolbar)

        var cardNumber : EditText = findViewById(R.id.card_number)
        var cardType : ImageView = findViewById(R.id.card_type)
        var progressBar : ProgressBar

        var expiry : EditText = findViewById(R.id.expiry)
        var cvv : EditText = findViewById(R.id.cvv)
        var logo : ImageView = findViewById(R.id.logo)

        var transac = Transaction()

        var isCardNumberError : Boolean = true
        var isCardCvvError : Boolean = true
        var isCardExpiryError : Boolean = true
        val card = Card()

        transac.amount = 100000.0

        cardNumber.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p: Editable?) {
            }

            override fun beforeTextChanged(p: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    card.number = p.toString()
                    when (card.type?.issuerName){
                        "MASTER" -> cardType.setImageResource(R.drawable.master_card)
                        "VISA" -> cardType.setImageResource(R.drawable.visa_card)
                        "VERVE" -> cardType.setImageResource(R.drawable.verve_card)

                    }
                    isCardNumberError = false

                    expiry.requestFocus()

                }catch (e : Exception){
                    if(p.toString().length > 15) cardNumber.error = "Invalid Card Number"
                    cardType.setImageResource(0)
                    isCardNumberError = true
                }
            }

        })

        cvv.addTextChangedListener(object  : TextWatcher {
            override fun afterTextChanged(p: Editable?) {
            }

            override fun beforeTextChanged(p: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    card.cvv = p.toString()
                    isCardCvvError = false
                    cvv.clearFocus()
                    cvv.isFocusableInTouchMode = true
                }catch (error : java.lang.Exception){
                    cvv.error = "Invalid Card CVV"
                    isCardCvvError = true

                }
            }

        })

        expiry.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p: Editable?) {
            }

            override fun beforeTextChanged(p: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    isCardExpiryError = false
                    var exp = p.toString().split("/")
                    card.expiryMonth = exp[0].toInt()
                    card.expiryYear = exp[1].toInt()

                    cvv.requestFocus()
                }
                catch (ex : InvalidParameterException){
                    expiry.error = ex.message
                    isCardExpiryError = true
                }
                catch (error : java.lang.Exception){
                    isCardExpiryError = true
                }
            }

        })

       val pay : TextView = findViewById(R.id.pay)

        val df = DecimalFormat("###,###.##")
        pay.text = String.format("%s%s", pay.text, df.format((transac.amount/ 100)).toString())

        //transaction.merchantReference = "78ijdjhh4hj494hjrkrkr"
        //transaction.bvn = "12345678909"
        //transaction.card = card

        pay.setOnClickListener {

            if(!isCardCvvError && !isCardExpiryError && !isCardNumberError){

            }

//            val card = Card()//5061030000000000928
//            //5061 0300 0000 0000 928
//            card.number = "5399 8300 0000 0008"//"4000 0000 0000 0622"//"4399830000000008"
//            card.expiryMonth = 5 //1
//            card.expiryYear = 30 //20
//            card.cvv = "000"

            val trans = Transaction()
            //
            trans.amount = 10000.0
            trans.customerEmail = "test@test.com"
            //trans.reference = "JAG000001170120592436"

            //trans.merchantReference = "JJHJRJOI39OHNKDJIUE" // Optional, will be auto generate by SDK if not present
            trans.description = "description"
           // trans.dateOfBirth = "120988"
            trans.card = card
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


            CyberpaySdk.completeCheckoutTransaction(this, trans, callback )
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

}
