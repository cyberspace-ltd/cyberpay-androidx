package com.cyberspace.cyberpaysdk

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import com.cyberspace.cyberpaysdk.data.bank.remote.response.BankResponse
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.ChargeCard
import com.cyberspace.cyberpaysdk.model.Transaction
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
import com.cyberspace.cyberpaysdk.ui.checkout.OnCheckoutSubmitted
import com.cyberspace.cyberpaysdk.ui.enroll_otp.EnrollOtpFragment
import com.cyberspace.cyberpaysdk.ui.enroll_otp.OnSubmitted
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

       //these dependencies can be injected -> work for another time
       private var repository : TransactionRepository = TransactionRepositoryImpl()

        //these dependencies can be injected -> work for another time
        private var scheduler : Scheduler = SchedulerImpl()

        fun initialiseSdk(integrationKey : String) {
            this.key = integrationKey
            validateKey()
        }

        fun initialiseSdk(integrationKey  : String, mode: Mode)  {
            this.key = integrationKey
            this.envMode = mode
            validateKey()
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
                      "Successful", "Success" -> transactionCallback.onSuccess(transaction)
                        else -> transactionCallback.onError(transaction, Throwable(result.data?.reason))
                     }
                 },
                 { error ->
                    // Log.e("RESPONSE", error.toString())
                     transactionCallback.onError(transaction, Throwable(Constant.errorGeneric))
                 }
             )

     }

     @SuppressLint("CheckResult")
     private fun verifyBankOtp(context: AppCompatActivity, transaction: Transaction, transactionCallback: TransactionCallback){
         repository.verifyBankOtp(transaction)
             ?.subscribeOn(scheduler.background())
             ?.observeOn(scheduler.ui())
             ?.subscribe(
                 { result ->
                     //Log.e("RESULT", result.data?.status)
                     when(result.data?.status){
                         "Successful", "Success" -> transactionCallback.onSuccess(transaction)
                         else -> transactionCallback.onError(transaction, Throwable(result.data?.reason))
                     }
                 },
                 { error ->
                     // Log.e("RESPONSE", error.toString())
                     transactionCallback.onError(transaction, Throwable(Constant.errorGeneric))
                 }
             )
     }


     private fun processCardOtp(context: AppCompatActivity, transaction: Transaction, transactionCallback: TransactionCallback){
         val otpFragment = OtpFragment(object : OtpSubmitted {
             override fun onSubmit(otp: String) {
                 // verify otp
                 transaction.otp = otp
                 verifyCardOtp(context,transaction, transactionCallback)

             }
         })

         otpFragment.show(context.supportFragmentManager, otpFragment.tag)
     }

     private fun processBankOtp(context: AppCompatActivity, transaction: Transaction, transactionCallback: TransactionCallback){
         val otpFragment = OtpFragment(object : OtpSubmitted {
             override fun onSubmit(otp: String) {
                 // verify otp
                 transaction.otp = otp
                 verifyBankOtp(context,transaction, transactionCallback)

             }
         })

         otpFragment.show(context.supportFragmentManager, otpFragment.tag)
     }

     @SuppressLint("CheckResult")
     private fun processSecure3DPayment(context: AppCompatActivity, transaction: Transaction, data: ChargeCard?, transactionCallback: TransactionCallback){
         val secure3dFragment = Secure3dFragment(context, transaction ,data, object : OnFinished {
             override fun onFinish(transaction: Transaction) {
                 // verify transaction status
                 repository.verifyTransactionByReference(transaction.reference)
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
                           //  var er = error
                             transactionCallback.onError(transaction, Throwable(Constant.errorGeneric))
                         }
                 )

             }

             override fun onCancel() {
                 transactionCallback.onError(transaction, Throwable(Constant.errorGeneric))
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

                            "Otp" -> processCardOtp(context, transaction, transactionCallback)
                            "ProvidePin" -> chargeCardWithPin(context,transaction, transactionCallback)

                            "EnrollOtp" -> {
                                // inflate phone number ui
                                val enrollFragment = EnrollOtpFragment(context, object : OnSubmitted {
                                    override fun onSubmit(number: String) {
                                        // verify otp
                                        transaction.card?.phoneNumber = number
                                        enrollCardOtp(context,transaction, transactionCallback)
                                    }
                                })

                                enrollFragment.show(context.supportFragmentManager, enrollFragment.tag)
                            }
                            "Secure3D" -> processSecure3DPayment(context,transaction,null, transactionCallback)
                            "Secure3DMpgs" -> processSecure3DPayment(context,transaction,null, transactionCallback)
                            "ProcessACS" -> processSecure3DPayment(context, transaction, t.data, transactionCallback)
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
                            "Otp" -> processCardOtp(context, transaction, transactionCallback)

                            "EnrollOtp" -> {
                                // inflate phone number ui
                                val enrollFragment = EnrollOtpFragment(context, object : OnSubmitted {
                                    override fun onSubmit(number: String) {
                                        // verify otp
                                        transaction.card?.phoneNumber = number
                                        enrollCardOtp(context,transaction, transactionCallback)
                                    }
                                })

                                enrollFragment.show(context.supportFragmentManager, enrollFragment.tag)
                            }
                            "Secure3D" -> processSecure3DPayment(context,transaction, null,transactionCallback)
                            "Secure3DMpgs" -> processSecure3DPayment(context,transaction,null, transactionCallback)
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
             //if(transaction.merchantReference.isEmpty())
                 transaction.merchantReference = SequenceGenerator.hash()
             // set transaction
             repository.beginTransaction(transaction)
                 ?.subscribeOn(scheduler.background())
                 ?.observeOn(scheduler.ui())
                 ?.subscribe({
                         t ->
                     transaction.reference = t.data?.transactionReference!!
                     transaction.charge = t.data?.charge
                     transactionCallback.onSuccess(transaction)

                 },
                     { e ->
                     //    Log.e("ERROR01_SERVER", e.message)
                         transactionCallback.onError(transaction, Throwable(Constant.errorGeneric))
                     })
         }

        @SuppressLint("CheckResult")
        fun getPayment(context: AppCompatActivity, transaction : Transaction, transactionCallback: TransactionCallback){

            transaction.type = TransactionType.Card
            createTransaction(context, transaction, object : TransactionCallback() {
                override fun onSuccess(transaction: Transaction) {
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

                override fun onError(transaction: Transaction, throwable: Throwable) {
                    transactionCallback.onError(transaction, throwable)
                }

                override fun onValidate(transaction: Transaction) {
                    transactionCallback.onValidate(transaction)
                }
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

     @SuppressLint("CheckResult")
     private fun  enrollCardOtp(context: AppCompatActivity, transaction : Transaction, transactionCallback: TransactionCallback){

         repository.enrollCardOtp(transaction)
             ?.subscribeOn(scheduler.background())
             ?.observeOn(scheduler.ui())
             ?.subscribe(
                 { result ->
                     when(result.data?.status) {
                         "Successful", "Success" -> transactionCallback.onSuccess(transaction)
                         "Otp" -> processCardOtp(context, transaction, transactionCallback)
                         "ProvidePin" -> chargeCardWithPin(context,transaction, transactionCallback)
                         else -> transactionCallback.onError(transaction, Throwable(result.message))
                     }
                 },
                 {
                     error -> transactionCallback.onError(transaction, Throwable(Constant.errorGeneric))
                 }
             )
     }

     @SuppressLint("CheckResult")
     private fun  enrollBankOtp(context: AppCompatActivity, transaction : Transaction, transactionCallback: TransactionCallback){
         repository.enrollBankOtp(transaction)
             ?.subscribeOn(scheduler.background())
             ?.observeOn(scheduler.ui())
             ?.subscribe(
                 { result ->
                     when(result.data?.status) {
                         "Successful", "Success" -> transactionCallback.onSuccess(transaction)
                         "Otp" -> processBankOtp(context, transaction, transactionCallback)
                         else -> transactionCallback.onError(transaction, Throwable(result.message))
                     }
                 },
                 {
                         error -> transactionCallback.onError(transaction, Throwable(Constant.errorGeneric))
                 }
             )
     }

     @SuppressLint("CheckResult")
     private fun chargeBank(context: AppCompatActivity, transaction : Transaction, transactionCallback: TransactionCallback){
            transaction.type = TransactionType.Bank
            validateKey()

         repository.chargeBank(transaction)
             ?.subscribeOn(scheduler.background())
             ?.observeOn(scheduler.ui())
             ?.subscribe (
                 {t ->

                     when(t.data?.status){

                         "Success", "Successful" -> {
                             transactionCallback.onSuccess(transaction)
                         }
                         "EnrollOtp" ->  {
                             // inflate phone ui
                             val otpFragment = OtpFragment(object : OtpSubmitted {
                                 override fun onSubmit(otp: String) {
                                     // verify otp
                                     transaction.otp = otp
                                     enrollBankOtp(context,transaction, transactionCallback)
                                 }
                             })

                             otpFragment.show(context.supportFragmentManager, otpFragment.tag)
                         }

                         "Otp" -> processBankOtp(context, transaction, transactionCallback)
                         else -> transactionCallback.onError(transaction, Throwable(t.data?.message))

                     }
                 },
                 {e ->
                    // Log.e("ERROR _SERVER", e.message)
                     transactionCallback.onError(transaction, Throwable(Constant.errorGeneric))
                 }
             )
            // set transaction
        }

     fun checkoutTransaction(context: AppCompatActivity, transaction : Transaction, transactionCallback: TransactionCallback){

         validateKey()
         val progress = ProgressDialog(context)
         val checkoutFragment = CheckoutFragment(context,transaction, object : OnCheckoutSubmitted {
             override fun onCardSubmit(dialog: Dialog?, card: Card) {
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
                        // dialog?.dismiss()
                         transactionCallback.onError(transaction,throwable)
                     }

                     override fun onValidate(transaction: Transaction) {
                         progress.dismiss()
                         //dialog?.dismiss()
                         transactionCallback.onValidate(transaction)
                     }
                 })
             }

             override fun onRedirect(dialog: Dialog?, bank: BankResponse) {
                // dialog?.dismiss()
                 progress.show()
                 createTransaction(context, transaction, object : TransactionCallback() {
                     override fun onSuccess(transaction: Transaction) {
                         progress.dismiss()
                         transaction.returnUrl = String.format("%s?reference=%s", bank.externalRedirectUrl, transaction.reference)
                         processSecure3DPayment(context, transaction, null, object : TransactionCallback() {
                             override fun onSuccess(transaction: Transaction) {
                                 dialog?.dismiss()
                                 transactionCallback.onSuccess(transaction)
                             }

                             override fun onError(transaction: Transaction, throwable: Throwable) {
                                 progress.dismiss()
                                 transactionCallback.onError(transaction, throwable)
                             }

                             override fun onValidate(transaction: Transaction) {
                                 progress.dismiss()
                                 transactionCallback.onValidate(transaction)
                             }
                         })
                     }

                     override fun onError(transaction: Transaction, throwable: Throwable) {
                         progress.dismiss()
                        transactionCallback.onError(transaction, throwable)
                     }

                     override fun onValidate(transaction: Transaction) {
                         progress.dismiss()
                        transactionCallback.onValidate(transaction)
                     }
                 })

             }

             override fun onBankSubmit(dialog: Dialog?, bank: BankResponse) {
                 progress.show()
                 createTransaction(context, transaction, object : TransactionCallback() {
                     override fun onSuccess(transaction: Transaction) {
                         chargeBank(context, transaction, object : TransactionCallback() {
                             override fun onSuccess(transaction: Transaction) {
                                 progress.dismiss()
                                 dialog?.dismiss()
                                 transactionCallback.onSuccess(transaction)
                             }

                             override fun onError(transaction: Transaction, throwable: Throwable) {
                                 progress.dismiss()
                                 transactionCallback.onError(transaction, throwable)
                             }

                             override fun onValidate(transaction: Transaction) {
                                 progress.dismiss()
                                 transactionCallback.onValidate(transaction)
                             }
                         })
                     }

                     override fun onError(transaction: Transaction, throwable: Throwable) {
                         progress.dismiss()
                         transactionCallback.onError(transaction, throwable)
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