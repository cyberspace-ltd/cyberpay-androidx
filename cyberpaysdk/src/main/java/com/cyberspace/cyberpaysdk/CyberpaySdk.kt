package com.cyberspace.cyberpaysdk

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.cyberspace.cyberpaysdk.data.transaction.Transaction
import com.cyberspace.cyberpaysdk.data.transaction.repository.TransactionRepository
import com.cyberspace.cyberpaysdk.data.transaction.repository.TransactionRepositoryImpl
import com.cyberspace.cyberpaysdk.enums.Mode
import com.cyberspace.cyberpaysdk.enums.TransactionType
import com.cyberspace.cyberpaysdk.exception.AuthenticationException
import com.cyberspace.cyberpaysdk.exception.SdkNotInitialisedException
import com.cyberspace.cyberpaysdk.model.Card
import com.cyberspace.cyberpaysdk.rx.Scheduler
import com.cyberspace.cyberpaysdk.rx.SchedulerImpl
import com.cyberspace.cyberpaysdk.ui.checkout.CheckoutFragment
import com.cyberspace.cyberpaysdk.ui.checkout.OnCardSubmitted
import com.cyberspace.cyberpaysdk.ui.otp.OtpFragment
import com.cyberspace.cyberpaysdk.ui.otp.OtpSubmitted
import com.cyberspace.cyberpaysdk.ui.pin.PinFragment
import com.cyberspace.cyberpaysdk.ui.pin.PinSubmitted
import com.cyberspace.cyberpaysdk.ui.secure3d.OnFinished
import com.cyberspace.cyberpaysdk.ui.secure3d.Secure3dFragment
import com.cyberspace.cyberpaysdk.ui.widget.ProgressDialog
import com.cyberspace.cyberpaysdk.utils.Constant
import com.cyberspace.cyberpaysdk.utils.SequenceGenerator

 object CyberpaySdk {

        private var key = "*"
        internal var envMode : Mode = Mode.Debug

       private var repository : TransactionRepository = TransactionRepositoryImpl()

        var scheduler : Scheduler = SchedulerImpl()

        fun initialiseSdk(integrationKey : String) {
            this.key = integrationKey
        }

        fun initialiseSdk(integrationKey  : String, mode: Mode) {
            this.key = integrationKey
            this.envMode = mode
        }

     private fun validateKey(){
         if(key.isEmpty()) throw AuthenticationException("Invalid Integration Key Found")
         if(key=="*") throw SdkNotInitialisedException("CyberpaySdk has not been Initialised")
     }

     @SuppressLint("CheckResult")
     private fun verifyCardOtp(context: AppCompatActivity, transaction: Transaction, transactionCallback: TransactionCallback){

         repository.verifyCardOtp(transaction)
             ?.subscribeOn(scheduler.background())
             ?.observeOn(scheduler.ui())
             ?.subscribe(
                 { result ->
                     //Log.e("RESULT", result.data?.status)
                     when(result.data?.status){
                      "Successful" -> transactionCallback.onSuccess(transaction)
                        else -> transactionCallback.onError(transaction, Throwable(result.data?.reason))
                     }
                 },
                 { error ->
                    // Log.e("RESPONSE", error.toString())
                     transactionCallback.onError(transaction, Throwable(Constant.errorGeneric))
                 }
             )

     }

     private fun verifyBankOtp(context: AppCompatActivity, transaction: Transaction, transactionCallback: TransactionCallback){

     }


     private fun processOtp(context: AppCompatActivity, transaction: Transaction, transactionCallback: TransactionCallback){
         val otpFragment = OtpFragment(object : OtpSubmitted {
             override fun onSubmit(otp: String) {
                 // verify otp
                 transaction.otp = otp
                 verifyCardOtp(context,transaction, transactionCallback)

             }
         })

         otpFragment.show(context.supportFragmentManager, otpFragment.tag)
     }

     @SuppressLint("CheckResult")
     private fun processSecure3DPayment(context: AppCompatActivity, transaction: Transaction, transactionCallback: TransactionCallback){
         val secure3dFragment = Secure3dFragment(context, transaction ,object : OnFinished {
             override fun onFinish(transaction: Transaction) {
                 // verify transaction status
                 repository.verifyTransactionByReference(transaction.reference!!)
                     ?.subscribeOn(scheduler.background())
                     ?.observeOn(scheduler.ui())
                     ?.subscribe(
                         { result ->

                             when(result.data?.status){
                                 "Successful", "Success" -> transactionCallback.onSuccess(transaction)
                                 else -> transactionCallback.onError(transaction, Throwable(result.data?.message))
                             }
                         },
                         { error ->
                             transactionCallback.onError(transaction, Throwable(Constant.errorGeneric))
                         }
                 )

             }
         })

         secure3dFragment.show(context.supportFragmentManager, secure3dFragment.tag)
     }

        @SuppressLint("CheckResult")
        private fun chargeCardWithoutPin(context: AppCompatActivity, transaction: Transaction, transactionCallback: TransactionCallback){
            repository.chargeCard(transaction)
                ?.subscribeOn(scheduler.background())
                ?.observeOn(scheduler.ui())
                ?.subscribe (
                    {t ->

                        when(t.data?.status){

                            "Success", "Successful" -> {
                                transactionCallback.onSuccess(transaction)
                            }

                            "Otp" -> processOtp(context, transaction, transactionCallback)
                            "ProvidePin" -> chargeCardWithPin(context,transaction, transactionCallback)

                            "EnrollOtp" -> {
                                // inflate phone number ui
                            }
                            "Secure3D" -> processSecure3DPayment(context,transaction, transactionCallback)
                            "Secure3DMpgs" -> processSecure3DPayment(context,transaction, transactionCallback)
                            else -> transactionCallback.onError(transaction, Throwable(t.data?.message))

                        }
                    },
                    {e ->
                        transactionCallback.onError(transaction, Throwable(Constant.errorGeneric))
                    }
                )
        }

        @SuppressLint("CheckResult")
        private fun chargeCardWithPin(context: AppCompatActivity, transaction: Transaction, transactionCallback: TransactionCallback){
            repository.chargeCard(transaction)
                ?.subscribeOn(scheduler.background())
                ?.observeOn(scheduler.ui())
                ?.subscribe (
                    { result ->

                        when(result.data?.status){
                            "Success", "Successful" -> transactionCallback.onSuccess(transaction)
                            "Otp" -> processOtp(context, transaction, transactionCallback)

                            "EnrollOtp" -> {
                                // inflate phone number ui

                            }
                            "Secure3D" -> processSecure3DPayment(context,transaction, transactionCallback)
                            "Secure3DMpgs" -> processSecure3DPayment(context,transaction, transactionCallback)
                            else -> transactionCallback.onError(transaction, Throwable(result.data?.message))
                        }
                    },
                    { error ->
                        transactionCallback.onError(transaction, Throwable(Constant.errorGeneric))
                    }
                )
        }


         @SuppressLint("CheckResult")
         fun createTransaction(context: AppCompatActivity, transaction : Transaction, transactionCallback: TransactionCallback){
             validateKey()
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
                     transaction.reference = t.data?.transactionReference
                     transaction.charge = t.data?.charge
                    transactionCallback.onSuccess(transaction)

                 },
                     { e ->
                         transactionCallback.onError(transaction, Throwable(Constant.errorGeneric))
                     })
         }

        @SuppressLint("CheckResult")
        fun getPayment(context: AppCompatActivity, transaction : Transaction, transactionCallback: TransactionCallback){
            validateKey()
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
                    transaction.reference = t.data?.transactionReference
                    transaction.charge = t.data?.charge
                    // inflate pin ui
                    val pinFragment = PinFragment(object : PinSubmitted {
                        override fun onSubmit(pin: String) {
                            // verify otp
                            transaction.card?.pin = pin
                            chargeCardWithoutPin(context,transaction, transactionCallback)
                        }
                    })

                    pinFragment.show(context.supportFragmentManager, pinFragment.tag)

                    },
                    { e ->
                        transactionCallback.onError(transaction, Throwable(Constant.errorGeneric))
                    })

        }

     @SuppressLint("CheckResult")
     fun chargeCard(context: AppCompatActivity, transaction : Transaction, transactionCallback: TransactionCallback){
         validateKey()
         transaction.type = TransactionType.Card
         // set transaction
         // inflate pin ui
         val pinFragment = PinFragment(object : PinSubmitted {
             override fun onSubmit(pin: String) {
                 // verify otp
                 transaction.card?.pin = pin
                 chargeCardWithoutPin(context,transaction, transactionCallback)
             }
         })

         pinFragment.show(context.supportFragmentManager, pinFragment.tag)
     }

     fun chargeBank(context: AppCompatActivity, transaction : Transaction, transactionCallback: TransactionCallback){
            transaction.type = TransactionType.Bank

            if(transaction.merchantReference.isEmpty())
                transaction.merchantReference = SequenceGenerator.nextId().toString()

            // set transaction
        }

     fun checkoutTransaction(context: AppCompatActivity, transaction : Transaction, transactionCallback: TransactionCallback){
         // inflate pin ui
         validateKey()
         val progress = ProgressDialog(context)
         val checkoutFragment = CheckoutFragment(transaction, object : OnCardSubmitted {
             override fun onSubmit(dialog: Dialog?, card: Card) {
                 // set transaction with card
                 progress.show("Processing Transaction")
                 transaction.card = card
                 getPayment(context,transaction, object : TransactionCallback() {
                     override fun onSuccess(transaction: Transaction) {
                         progress.dismiss()
                         dialog?.dismiss()
                         transactionCallback.onSuccess(transaction)
                     }

                     override fun onError(transaction: Transaction, throwable: Throwable) {
                         progress.dismiss()
                         transactionCallback.onError(transaction,throwable)
                     }

                     override fun onValidate(transaction: Transaction) {
                         progress.dismiss()
                         transactionCallback.onValidate(transaction)
                     }
                 })
             }
         })

         checkoutFragment.show(context.supportFragmentManager, checkoutFragment.tag)
     }


}