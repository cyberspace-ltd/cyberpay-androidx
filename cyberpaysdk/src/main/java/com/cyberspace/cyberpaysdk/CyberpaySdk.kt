package com.cyberspace.cyberpaysdk

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.cyberspace.cyberpaysdk.data.base.remote.ApiResponse
import com.cyberspace.cyberpaysdk.data.transaction.Transaction
import com.cyberspace.cyberpaysdk.data.transaction.repository.TransactionRepository
import com.cyberspace.cyberpaysdk.data.transaction.repository.TransactionRepositoryImpl
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.SetTransaction
import com.cyberspace.cyberpaysdk.enums.Mode
import com.cyberspace.cyberpaysdk.enums.TransactionType
import com.cyberspace.cyberpaysdk.rx.Scheduler
import com.cyberspace.cyberpaysdk.rx.SchedulerImpl
import com.cyberspace.cyberpaysdk.utils.SequenceGenerator
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

 object CyberpaySdk {

        private var key = ""
        private var envMode : Mode = Mode.Debug

       private var repository : TransactionRepository = TransactionRepositoryImpl()

        var scheduler : Scheduler = SchedulerImpl()

        fun initialiseSdk(integrationKey : String) {
            this.key = integrationKey
        }

        fun initialiseSdk(integrationKey  : String, mode: Mode) {
            this.key = integrationKey
            this.envMode = mode
        }

        @SuppressLint("CheckResult")
        private fun chargeCardWithoutPin(context: Context, transaction: Transaction, transactionCallback: TransactionCallback){
            repository.chargeCard(transaction)
                ?.subscribeOn(scheduler.background())
                ?.observeOn(scheduler.ui())
                ?.subscribe (
                    {t ->

                        when(t.data?.status){

                            "Success", "Successful" -> {
                                transactionCallback.onSuccess(transaction)
                            }

                            "Otp" -> {
                            // inflate opt ui
                            }
                            "ProvidePin" -> {
                                // inflate pin ui
                            }
                            "EnrollOtp" -> {
                                // inflate phone number ui
                            }
                            "Secure3D" -> {
                                // to be decided
                            }
                            "Secure3DMpgs" -> {
                                // to be decided
                            }

                        }
                    },
                    {e ->
                       //  Log.e("RESPONSE", e.toString())
                    }
                )
        }

        private fun chargeCardWithPin(context: Context, transaction: Transaction, transactionCallback: TransactionCallback){

        }


        @SuppressLint("CheckResult")
        fun chargeCard(context: Context, transaction : Transaction, transactionCallback: TransactionCallback){
            transaction.type = TransactionType.Card
            transaction.key = key

            // set transaction
            if(transaction.merchantReference.isEmpty())
                transaction.merchantReference = SequenceGenerator.hash()

            // set transaction
            repository.beginTransaction(transaction)
                ?.subscribeOn(scheduler.background())
                ?.observeOn(scheduler.ui())
                ?.subscribe({
                    t ->
                    transaction.transactionReference = t.data?.transactionReference
                    transaction.charge = t.data?.charge
                    
                    // charge card without pinpad
                    chargeCardWithoutPin(context, transaction, object : TransactionCallback() {
                        override fun onSuccess(transaction: Transaction) {
                            transactionCallback.onSuccess(transaction)
                        }

                        override fun onError(transaction: Transaction, throwable: Throwable) {

                        }

                        override fun onValidate(transaction: Transaction) {

                        }
                    })

                    },
                    { e ->

                    })

        }

        internal fun beginTransaction(context: Context, transaction: Transaction, transactionCallback: TransactionCallback){

            if(transaction.merchantReference.isEmpty())
                transaction.merchantReference = SequenceGenerator.nextId().toString()

            // set transaction

        }

        fun chargeBank(context: Context, transaction : Transaction, transactionCallback: TransactionCallback){
            transaction.type = TransactionType.Bank

            if(transaction.merchantReference.isEmpty())
                transaction.merchantReference = SequenceGenerator.nextId().toString()

            // set transaction
        }


}