package com.cyberspace.cyberpaysdk.ui.checkout

import android.annotation.SuppressLint
import com.cyberspace.cyberpaysdk.data.bank.repository.BankRepository
import com.cyberspace.cyberpaysdk.data.bank.repository.BankRepositoryImpl
import com.cyberspace.cyberpaysdk.enums.PaymentOption
import com.cyberspace.cyberpaysdk.rx.Scheduler
import com.cyberspace.cyberpaysdk.rx.SchedulerImpl

internal class CheckoutPresenter : CheckoutContract.Presenter {

    var mView : CheckoutContract.View? = null
    private lateinit var repository: BankRepository
    private lateinit var scheduler : Scheduler
    private var isLoading = false
    var paymentOption = PaymentOption.Card

    @SuppressLint("CheckResult")
    override fun loadBanks() {

        if(isLoading) return
        mView?.onLoad()

        repository = BankRepositoryImpl()
        scheduler = SchedulerImpl()
        isLoading = true

        repository.getBanks()
            ?.subscribeOn(scheduler.background())
            ?.observeOn(scheduler.ui())
            ?.subscribe(
                {result ->
                    isLoading = false
                    mView?.onLoadComplete(result)
                },
                {error ->
                    isLoading = false
                    mView?.onError(error.message!!)
                }
            )

    }

    override fun bankPay() {
        paymentOption = PaymentOption.Bank
        mView?.onBankPay()
    }

    override fun cardPay() {
        paymentOption = PaymentOption.Card
        mView?.onCardPay()
    }

    override fun start() {

    }

    override fun attachView(view: CheckoutContract.View) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }

    override fun destroy() {

    }

    override fun getView(): CheckoutContract.View? {
        return mView
    }
}