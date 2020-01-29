package com.cyberspace.cyberpaysdk.ui.checkout

import android.app.Dialog
import com.cyberspace.cyberpaysdk.data.bank.remote.response.BankResponse
import com.cyberspace.cyberpaysdk.model.BankAccount
import com.cyberspace.cyberpaysdk.model.Card
import com.cyberspace.cyberpaysdk.model.Transaction

internal  interface CheckoutListener {
    fun onCardSubmit(dialog: Dialog? ,card : Card)
    fun onRedirect(dialog: Dialog?, bank: BankResponse)
    fun onBankSubmit(dialog: Dialog?, bankAccount: BankAccount)
    fun onCancel(transaction: Transaction)
}