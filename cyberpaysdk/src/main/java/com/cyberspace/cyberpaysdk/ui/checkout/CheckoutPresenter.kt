package com.cyberspace.cyberpaysdk.ui.checkout

import android.annotation.SuppressLint
import com.cyberspace.cyberpaysdk.CyberpaySdk
import com.cyberspace.cyberpaysdk.data.bank.repository.BankRepository
import com.cyberspace.cyberpaysdk.data.bank.repository.BankRepositoryImpl
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.Advice
import com.cyberspace.cyberpaysdk.data.transaction.repository.TransactionRepository
import com.cyberspace.cyberpaysdk.data.transaction.repository.TransactionRepositoryImpl
import com.cyberspace.cyberpaysdk.enums.PaymentOption
import com.cyberspace.cyberpaysdk.model.Transaction
import com.cyberspace.cyberpaysdk.rx.Scheduler
import com.cyberspace.cyberpaysdk.rx.SchedulerImpl

internal class CheckoutPresenter : CheckoutContract.Presenter {

    private var mView : CheckoutContract.View? = null
    private var repository: BankRepository = BankRepositoryImpl()
    private var scheduler : Scheduler = SchedulerImpl()
    private var isLoading = false
    var paymentOption = PaymentOption.Card
    private val transactionRepository: TransactionRepository = TransactionRepositoryImpl()

    @SuppressLint("CheckResult")
    override fun getCardTransactionAdvice(transaction: Transaction) {
        transactionRepository.getCardTransactionAdvice(transaction)
            ?.subscribeOn(scheduler.background())
            ?.observeOn(scheduler.ui())
            ?.subscribe(
                {
                    mView?.onUpdateAdvice(it.data!!)
                },
                {
                    disablePay()
                }
            )
    }

    @SuppressLint("CheckResult")
    override fun cancelTransaction(transaction: Transaction) {
    transactionRepository.cancelTransaction(transaction)
        ?.subscribeOn(scheduler.background())
        ?.observeOn(scheduler.ui())
        ?.subscribe(
            {
                transaction.message = it?.message!!
                mView?.onCancelTransaction(transaction)
            },
            {
                mView?.onCancelTransactionError(transaction)
            }
        )

    }

    @SuppressLint("CheckResult")
    override fun getBankTransactionAdvice(transaction: Transaction) {
        transactionRepository.getBankTransactionAdvice(transaction)
       ?.subscribeOn(scheduler.background())
       ?.observeOn(scheduler.ui())
       ?.subscribe(
           {
              mView?.onUpdateAdvice(it.data!!)
           },
           {
               disablePay()
               println(it)
           }
       )
    }

    override fun disablePay() {
        mView?.onDisablePay()
    }

    override fun enablePay() {
        mView?.onEnablePay()
    }

    @SuppressLint("CheckResult")
    override fun loadBanks() {

        if(isLoading) return
        mView?.onLoad()

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

    @SuppressLint("CheckResult")
    override fun getAccountName(bankCode: String, account: String) {
        repository.getAccountName(bankCode, account)
            ?.subscribeOn(scheduler.background())
            ?.observeOn(scheduler.ui())
            ?.subscribe(
                {
                    mView?.onAccountName(it.data!!)
                },
                {
                    mView?.onError(it.message!!)
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