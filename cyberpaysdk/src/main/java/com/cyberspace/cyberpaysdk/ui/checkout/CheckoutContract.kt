package com.cyberspace.cyberpaysdk.ui.checkout

import com.cyberspace.cyberpaysdk.data.bank.remote.response.AccountResponse
import com.cyberspace.cyberpaysdk.data.bank.remote.response.BankResponse
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.Advice
import com.cyberspace.cyberpaysdk.model.Transaction
import com.example.kotlin.ui.base.BasePresenter
import com.example.kotlin.ui.base.BaseView

internal interface CheckoutContract {

        interface Presenter : BasePresenter<View>{
            fun loadBanks()
            fun bankPay()
            fun cardPay()
            fun getAccountName(bankCode: String, account: String)
            fun getCardTransactionAdvice(transaction: Transaction)
            fun getBankTransactionAdvice(transaction: Transaction)
            fun cancelTransaction(transaction: Transaction)
            fun disablePay()
            fun enablePay()
        }

        interface View : BaseView {
            fun onError(message: String)
            fun onBankPay()
            fun onCardPay()
            fun onLoad()
            fun onLoadComplete(banks: MutableList<BankResponse>)
            fun onAccountName(account: AccountResponse)
            fun onUpdateAdvice(advice: Advice)
            fun onCancelTransaction(transaction: Transaction)
            fun onCancelTransactionError(transaction: Transaction)
            fun onDisablePay()
            fun onEnablePay()
        }

}