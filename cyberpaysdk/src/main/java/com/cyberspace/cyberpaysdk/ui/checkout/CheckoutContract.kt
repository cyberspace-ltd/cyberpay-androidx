package com.cyberspace.cyberpaysdk.ui.checkout

import com.cyberspace.cyberpaysdk.data.bank.remote.response.AccountResponse
import com.cyberspace.cyberpaysdk.data.bank.remote.response.BankResponse
import com.example.kotlin.ui.base.BasePresenter
import com.example.kotlin.ui.base.BaseView

internal interface CheckoutContract {

    interface Presenter : BasePresenter<View>{
        fun loadBanks()
        fun bankPay()
        fun cardPay()
        fun getAccountName(bankCode: String, account: String)
    }

    interface View : BaseView {
       fun onError(message: String)
        fun onBankPay()
        fun onCardPay()
        fun onLoad()
        fun onLoadComplete(banks: MutableList<BankResponse>)
        fun onAccountName(account: AccountResponse)
    }

}