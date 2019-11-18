package com.cyberspace.cyberpayapp

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.cyberspace.cyberpaysdk.CyberpaySdk
import com.cyberspace.cyberpaysdk.TransactionCallback
import com.cyberspace.cyberpaysdk.data.transaction.Transaction
import com.cyberspace.cyberpaysdk.model.Card
import com.google.android.material.floatingactionbutton.FloatingActionButton


import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val fab : FloatingActionButton = findViewById(R.id.fab)

        val card = Card()//5061030000000000928
        //5061 0300 0000 0000 928
        /*card.cardNumber = "5061 0300 0000 0000 928"//"4399830000000008"
        card.expiryMonth = 6
        card.expiryYear = 50
        card.cvv = "000"

         */

        val transaction = Transaction()
        //
        transaction.amount = 10000000.0
        transaction.returnUrl = "https://googl"
        //transaction.card = card

        fab.setOnClickListener {
            CyberpaySdk.checkoutTransaction(this, transaction, object : TransactionCallback() {
                override fun onSuccess(transaction: Transaction) {
                    Log.e("RESPONSE", "SUCCESSFUL")
                }

                override fun onError(transaction: Transaction, throwable: Throwable) {
                    Log.e("ERROR", throwable.message)
                }

                override fun onValidate(transaction: Transaction) {

                }
            })
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
