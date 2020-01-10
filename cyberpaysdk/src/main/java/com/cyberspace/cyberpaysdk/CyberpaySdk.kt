package com.cyberspace.cyberpaysdk

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.drawable.Drawable
import androidx.fragment.app.FragmentActivity
import com.cyberspace.cyberpaysdk.data.bank.remote.response.BankResponse
import com.cyberspace.cyberpaysdk.model.Transaction
import com.cyberspace.cyberpaysdk.data.transaction.repository.TransactionRepository
import com.cyberspace.cyberpaysdk.data.transaction.repository.TransactionRepositoryImpl
import com.cyberspace.cyberpaysdk.enums.Mode
import com.cyberspace.cyberpaysdk.enums.TransactionType
import com.cyberspace.cyberpaysdk.exception.AuthenticationException
import com.cyberspace.cyberpaysdk.exception.SdkNotInitialisedException
import com.cyberspace.cyberpaysdk.exception.TransactionNotFoundException
import com.cyberspace.cyberpaysdk.model.BankAccount
import com.cyberspace.cyberpaysdk.model.Card
import com.cyberspace.cyberpaysdk.rx.Scheduler
import com.cyberspace.cyberpaysdk.rx.SchedulerImpl
import com.cyberspace.cyberpaysdk.ui.checkout.CheckoutFragment
import com.cyberspace.cyberpaysdk.ui.checkout.OnCheckoutSubmitted
import com.cyberspace.cyberpaysdk.ui.dob.DobFragment
import com.cyberspace.cyberpaysdk.ui.enroll_otp.EnrollOtpFragment
import com.cyberspace.cyberpaysdk.ui.enroll_otp.OnSubmitted
import com.cyberspace.cyberpaysdk.ui.enroll_otp_bank.EnrollOtpBankFragment
import com.cyberspace.cyberpaysdk.ui.otp.OtpFragment
import com.cyberspace.cyberpaysdk.ui.otp.OtpSubmitted
import com.cyberspace.cyberpaysdk.ui.pin.PinFragment
import com.cyberspace.cyberpaysdk.ui.pin.PinSubmitted
import com.cyberspace.cyberpaysdk.ui.secure3d.OnFinished
import com.cyberspace.cyberpaysdk.ui.secure3d.Secure3dFragment
import com.cyberspace.cyberpaysdk.ui.widget.ProgressDialog
import com.cyberspace.cyberpaysdk.utils.Constant
import com.cyberspace.cyberpaysdk.utils.SequenceGenerator
import java.security.InvalidParameterException

object CyberpaySdk {

    internal var key = "*"
    internal var envMode : Mode = Mode.Debug

    var merchantLogo : Drawable? = null
    private var autoGenerateMerchantReference = false


   //these dependencies can be injected -> work for another time
   private var repository : TransactionRepository = TransactionRepositoryImpl()

    //these dependencies can be injected -> work for another time
    private var scheduler : Scheduler = SchedulerImpl()

    fun initialiseSdk(integrationKey : String) : CyberpaySdk{
        this.key = integrationKey
        validateKey()
        return this
    }

    fun initialiseSdk(integrationKey  : String, mode: Mode) : CyberpaySdk  {
        this.key = integrationKey
        this.envMode = mode
        validateKey()
        return this
    }

     private fun validateKey(){
         if(key.isEmpty()) throw AuthenticationException("Invalid Integration Key Found")
         if(key=="*") throw SdkNotInitialisedException("CyberpaySdk has not been Initialised")
     }


     @SuppressLint("CheckResult")
     private fun verifyCardOtp(context: FragmentActivity, transaction: Transaction, transactionCallback: TransactionCallback){

         repository.verifyCardOtp(transaction)
             ?.subscribeOn(scheduler.background())
             ?.observeOn(scheduler.ui())
             ?.subscribe(
                 { result ->
                     transaction.message = result.data?.message!!
                     when(result.data?.status){
                      "Successful", "Success" -> transactionCallback.onSuccess(transaction)
                        else -> transactionCallback.onError(transaction, Throwable(result.data?.reason))
                     }
                 },
                 {
                         throwable -> transactionCallback.onError(transaction, throwable)
                 }
             )

     }

     @SuppressLint("CheckResult")
     private fun verifyBankOtp(context: FragmentActivity, transaction: Transaction, transactionCallback: TransactionCallback){
         repository.verifyBankOtp(transaction)
             ?.subscribeOn(scheduler.background())
             ?.observeOn(scheduler.ui())
             ?.subscribe(
                 { result ->
                     transaction.message = result.data?.message!!
                     when(result.data?.status){
                         "Successful", "Success" -> transactionCallback.onSuccess(transaction)
                         else -> transactionCallback.onError(transaction, Throwable(result.data?.reason))
                     }
                 },
                 {
                         throwable -> transactionCallback.onError(transaction, throwable)
                 }
             )
     }


     private fun processCardOtp(context: FragmentActivity, transaction: Transaction, transactionCallback: TransactionCallback){
         val otpFragment = OtpFragment(transaction, object : OtpSubmitted {
             override fun onSubmit(otp: String) {
                 // verify otp
                 transaction.otp = otp
                 verifyCardOtp(context,transaction, transactionCallback)

             }
         })

         otpFragment.show(context.supportFragmentManager, otpFragment.tag)
     }

     private fun processBankOtp(context: FragmentActivity, transaction: Transaction, transactionCallback: TransactionCallback){
         val otpFragment = OtpFragment(transaction, object : OtpSubmitted {
             override fun onSubmit(otp: String) {
                 // verify otp
                 transaction.otp = otp
                 verifyBankOtp(context,transaction, transactionCallback)

             }
         })

         otpFragment.show(context.supportFragmentManager, otpFragment.tag)
     }

    private fun processEnrollCardOtp(context: FragmentActivity, transaction: Transaction, transactionCallback: TransactionCallback) {

        val enrollFragment = EnrollOtpFragment( object : OnSubmitted {
            override fun onSubmit(number: String) {
                // verify otp
                transaction.card?.phoneNumber = number
                enrollCardOtp(context,transaction, transactionCallback)
            }
        })

        enrollFragment.show(context.supportFragmentManager, enrollFragment.tag)
    }

     @SuppressLint("CheckResult")
     private fun processSecure3DPayment(context: FragmentActivity, transaction: Transaction, transactionCallback: TransactionCallback){
         val secure3dFragment = Secure3dFragment(transaction ,object : OnFinished {
             override fun onFinish(transaction: Transaction) {
                 // verify transaction status
                 repository.verifyTransactionByReference(transaction.reference)
                     ?.subscribeOn(scheduler.background())
                     ?.observeOn(scheduler.ui())
                     ?.subscribe(
                         { result ->
                             transaction.message = result.data?.message!!
                             when(result.data?.status){
                                 "Successful", "Success" -> transactionCallback.onSuccess(transaction)
                                 else -> transactionCallback.onError(transaction, Throwable(result.data?.message))
                             }
                         },
                         {
                                 error -> transactionCallback.onError(transaction, error)
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
        private fun chargeCardWithoutPin(context: FragmentActivity, transaction: Transaction, transactionCallback: TransactionCallback){
            transaction.type = TransactionType.Card
            repository.chargeCard(transaction)
                ?.subscribeOn(scheduler.background())
                ?.observeOn(scheduler.ui())
                ?.subscribe (
                    {t ->
                       // var result = t
                        transaction.message = t.data?.message!!
                        when(t.data?.status){

                            "Success", "Successful" -> {
                                transactionCallback.onSuccess(transaction)
                            }

                            "Otp" -> processCardOtp(context, transaction, transactionCallback)
                            "ProvidePin" -> {

                                // inflate pin ui
                                val pinFragment = PinFragment(transaction, object : PinSubmitted {
                                    override fun onSubmit(pin: String) {
                                        // verify otp
                                        transaction.card?.pin = pin
                                        chargeCardWithPin(context,transaction, transactionCallback)
                                    }
                                })

                                pinFragment.show(context.supportFragmentManager, pinFragment.tag)

                            }

                            "EnrollOtp" -> processEnrollCardOtp(context, transaction, transactionCallback)

                            "Secure3D" ,
                            "Secure3DMpgs" ,
                            "ProcessACS" -> {
                                transaction.returnUrl = t.data!!.redirectUrl
                                println(transaction.returnUrl )
                                processSecure3DPayment(context, transaction, transactionCallback)
                            }
                            else -> transactionCallback.onError(transaction, Throwable(t.data?.message))

                        }

                    },
                    {
                            throwable -> transactionCallback.onError(transaction, throwable)
                    }
                )
        }

        @SuppressLint("CheckResult")
        private fun chargeCardWithPin(context: FragmentActivity, transaction: Transaction, transactionCallback: TransactionCallback){
            repository.chargeCard(transaction)
                ?.subscribeOn(scheduler.background())
                ?.observeOn(scheduler.ui())
                ?.subscribe (
                    { result ->
                        transaction.message = result.data?.message!!
                        when(result.data?.status){
                            "Success", "Successful" -> transactionCallback.onSuccess(transaction)
                            "Otp" -> processCardOtp(context, transaction, transactionCallback)

                            "EnrollOtp" -> processEnrollCardOtp(context, transaction, transactionCallback)
                            "Secure3D",
                            "Secure3DMpgs" -> {
                                transaction.returnUrl = result.data!!.redirectUrl
                                processSecure3DPayment(context,transaction, transactionCallback)
                            }
                            else -> transactionCallback.onError(transaction, Throwable(result.data?.message))
                        }


                    },
                    {
                            throwable -> transactionCallback.onError(transaction, throwable)
                    }
                )
        }

         @SuppressLint("CheckResult")
         fun createTransaction(context: FragmentActivity, transaction : Transaction, transactionCallback: TransactionCallback){
             validateKey()
             //transaction.type = TransactionType.Card
             transaction.key = key

             // set transaction
             if(transaction.merchantReference.isEmpty()  && !autoGenerateMerchantReference)
                 autoGenerateMerchantReference = true

             if(autoGenerateMerchantReference) transaction.merchantReference = SequenceGenerator.hash()
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
                 {
                         throwable -> transactionCallback.onError(transaction, throwable)
                 }
                 )
         }

        @SuppressLint("CheckResult")
        fun getPayment(context: FragmentActivity, transaction : Transaction, transactionCallback: TransactionCallback){

            transaction.type = TransactionType.Card
            createTransaction(context, transaction, object : TransactionCallback() {
                override fun onSuccess(transaction: Transaction) {
                    processPayment(context, transaction, transactionCallback)
                }

                override fun onError(transaction: Transaction, throwable: Throwable) {
                    transactionCallback.onError(transaction, throwable)
                }

                override fun onValidate(transaction: Transaction) {
                    transactionCallback.onValidate(transaction)
                }
            })
        }

    private fun processPayment(context: FragmentActivity, transaction : Transaction, transactionCallback: TransactionCallback){
        if(transaction.card == null) throw InvalidParameterException("Card Not Found")
        // inflate pin ui
         when(transaction.card?.type?.name) {

             "VERVE" -> {
                 val pinFragment = PinFragment(transaction, object : PinSubmitted {
                     override fun onSubmit(pin: String) {
                         // verify otp
                         transaction.card?.pin = pin
                         chargeCardWithoutPin(context,transaction, transactionCallback)
                     }
                 })

                 pinFragment.show(context.supportFragmentManager, pinFragment.tag)
             }
             else -> {
                 chargeCardWithoutPin(context,transaction, transactionCallback)
             }
         }

     }

     @SuppressLint("CheckResult")
     fun chargeCard(context: FragmentActivity, transaction : Transaction, transactionCallback: TransactionCallback){
         validateKey()
         transaction.type = TransactionType.Card
         // set transaction
         processPayment(context, transaction, transactionCallback)

     }

     @SuppressLint("CheckResult")
     private fun  enrollCardOtp(context: FragmentActivity, transaction : Transaction, transactionCallback: TransactionCallback){

         repository.enrollCardOtp(transaction)
             ?.subscribeOn(scheduler.background())
             ?.observeOn(scheduler.ui())
             ?.subscribe(
                 { result ->
                     transaction.message = result.data?.message!!
                     when(result.data?.status) {
                         "Successful", "Success" -> transactionCallback.onSuccess(transaction)
                         "Otp" -> processCardOtp(context, transaction, transactionCallback)
                         "ProvidePin" -> chargeCardWithPin(context,transaction, transactionCallback)
                         else -> transactionCallback.onError(transaction, Throwable(result.message))
                     }
                 },
                 {
                         throwable -> transactionCallback.onError(transaction, throwable)
                 }
             )
     }

     @SuppressLint("CheckResult")
     private fun  enrollBankOtp(context: FragmentActivity, transaction : Transaction, transactionCallback: TransactionCallback){
         repository.enrollBankOtp(transaction)
             ?.subscribeOn(scheduler.background())
             ?.observeOn(scheduler.ui())
             ?.subscribe(
                 { result ->
                     transaction.message = result.data?.message!!
                     when(result.data?.status) {
                         "Successful", "Success" -> transactionCallback.onSuccess(transaction)
                         "Otp" -> processBankOtp(context, transaction, transactionCallback)
                         else -> transactionCallback.onError(transaction, Throwable(result.message))
                     }
                 },
                 {
                         throwable -> transactionCallback.onError(transaction, throwable)
                 }
             )
     }

    @SuppressLint("CheckResult")
    private fun enrollBank(context: FragmentActivity, transaction : Transaction, transactionCallback: TransactionCallback){
        repository.enrolBank(transaction)
            ?.subscribeOn(scheduler.background())
            ?.observeOn(scheduler.ui())
            ?.subscribe (
                {
                        t ->
                    transaction.message = t.data?.message!!
                    when(t.data?.status) {

                        "Success", "Successful" -> transactionCallback.onSuccess(transaction)

                        "MandateOtp" -> {

                            val otpFragment = EnrollOtpBankFragment(transaction, object : com.cyberspace.cyberpaysdk.ui.enroll_otp_bank.OnSubmitted {
                                override fun onSubmit(otp: String) {
                                    // verify otp
                                    transaction.otp = otp
                                    processMandateBankOtp(context, transaction, transactionCallback)
                                }
                            })

                            otpFragment.show(context.supportFragmentManager, otpFragment.tag)
                            //
                        }

                        else -> transactionCallback.onError(transaction, Throwable(t.data?.message))

                    }
                },
                {
                        throwable -> transactionCallback.onError(transaction, throwable)
                }
            )
    }

    @SuppressLint("CheckResult")
    private fun processBankFinalOtp(context: FragmentActivity, transaction : Transaction, transactionCallback: TransactionCallback){
        repository.finalBankOtp(transaction)
            ?.subscribeOn(scheduler.background())
            ?.observeOn(scheduler.ui())
            ?.subscribe (
                { t ->
                    when (t.data?.status) {

                        "Success", "Successful" -> transactionCallback.onSuccess(transaction)

                        else -> transactionCallback.onError(transaction, Throwable(t.data?.message))
                    }

                },
                {
                        throwable -> transactionCallback.onError(transaction, throwable)
                }
            )
    }

    @SuppressLint("CheckResult")
    private fun processMandateBankOtp(context: FragmentActivity, transaction : Transaction, transactionCallback: TransactionCallback){
        repository.mandateBankOtp(transaction)
            ?.subscribeOn(scheduler.background())
            ?.observeOn(scheduler.ui())
            ?.subscribe (
                { t ->
                    when (t.data?.status) {

                        "Success", "Successful" -> transactionCallback.onSuccess(transaction)
                        "FinalOtp" -> {

                            val otpFragment = OtpFragment(transaction, object : OtpSubmitted {
                                override fun onSubmit(otp: String) {
                                    // verify otp
                                    transaction.otp = otp
                                    processBankFinalOtp(context, transaction, transactionCallback)
                                }
                            })

                            otpFragment.show(context.supportFragmentManager, otpFragment.tag)

                        }
                        else -> transactionCallback.onError(transaction, Throwable(t.data?.message))
                    }

                },
                {
                        throwable -> transactionCallback.onError(transaction, throwable)
                }
            )
    }

     @SuppressLint("CheckResult")
     private fun chargeBank(context: FragmentActivity, transaction : Transaction, transactionCallback: TransactionCallback){
            transaction.type = TransactionType.Bank
            validateKey()

         repository.chargeBank(transaction)
             ?.subscribeOn(scheduler.background())
             ?.observeOn(scheduler.ui())
             ?.subscribe (
                 {t ->

                     transaction.message = t.data?.message!!
                     when(t.data?.status){

                         "Success", "Successful" -> {
                             transactionCallback.onSuccess(transaction)
                         }
                         "OtherDetails" -> {

                            when(t.data?.requiredParameters!![0].param){
                                "dob" -> {

                                    val fragment = DobFragment(t.data?.requiredParameters!![0].message!!, object : com.cyberspace.cyberpaysdk.ui.dob.OnSubmitListener {
                                        override fun onSubmit(dob: String) {
                                            transaction.bankAccount?.dateOfBirth = dob
                                             enrollBank(context,transaction, transactionCallback)
                                        }
                                    })

                                    fragment.show(context.supportFragmentManager, fragment.tag)
                                }

                                "bvn" -> {

                                }

                                else -> {

                                }
                            }
                         }
                         "FinalOtp" -> {

                             val otpFragment = OtpFragment(transaction, object : OtpSubmitted {
                                 override fun onSubmit(otp: String) {
                                     // verify otp
                                     transaction.otp = otp
                                     processBankFinalOtp(context, transaction, transactionCallback)
                                 }
                             })

                             otpFragment.show(context.supportFragmentManager, otpFragment.tag)

                         }
                         "EnrollOtp" ->  {
                             // inflate phone ui
                             val otpFragment = EnrollOtpBankFragment(transaction, object : com.cyberspace.cyberpaysdk.ui.enroll_otp_bank.OnSubmitted {
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
                 {
                         throwable -> transactionCallback.onError(transaction, throwable)
                 }
             )
            // set transaction
        }

    fun completeCheckoutTransaction(context: FragmentActivity, transaction : Transaction, transactionCallback: TransactionCallback){
        if(transaction.reference.isEmpty()) {
            throw TransactionNotFoundException("Transaction reference not found. Kindly set transaction before calling this method")
        }
        transaction.key = key
        if(transaction.merchantReference.isEmpty()  && !autoGenerateMerchantReference)
            autoGenerateMerchantReference = true

        if(autoGenerateMerchantReference) transaction.merchantReference = SequenceGenerator.hash()

        val progress = ProgressDialog(context)
        val checkoutFragment = CheckoutFragment(transaction, object : OnCheckoutSubmitted {
            override fun onCardSubmit(dialog: Dialog?, card: Card) {
                // set transaction with card
                progress.show("Processing Transaction")
                transaction.card = card
                processPayment(context,transaction, object : TransactionCallback() {
                    override fun onSuccess(transaction: Transaction) {
                        progress.dismiss()
                        dialog?.dismiss()
                        transactionCallback.onSuccess(transaction)
                    }

                    override fun onError(transaction: Transaction, throwable: Throwable) {
                        progress.dismiss()
                        if(!autoGenerateMerchantReference) dialog?.dismiss()
                        transactionCallback.onError(transaction,throwable)
                    }

                    override fun onValidate(transaction: Transaction) {
                        progress.dismiss()
                        transactionCallback.onValidate(transaction)
                    }
                })
            }

            override fun onRedirect(dialog: Dialog?, bank: BankResponse) {
                progress.dismiss()
                transaction.returnUrl = String.format("%s?reference=%s", bank.externalRedirectUrl, transaction.reference)
                processSecure3DPayment(context, transaction, object : TransactionCallback() {
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

            override fun onBankSubmit(dialog: Dialog?, bankAccount: BankAccount) {
                transaction.bankAccount = bankAccount
                progress.show()
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
        })

        checkoutFragment.show(context.supportFragmentManager, checkoutFragment.tag)
    }

     fun checkoutTransaction(context: FragmentActivity, transaction : Transaction, transactionCallback: TransactionCallback){
         validateKey()
         val progress = ProgressDialog(context)
         val checkoutFragment = CheckoutFragment(transaction, object : OnCheckoutSubmitted {
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
                         if(!autoGenerateMerchantReference) dialog?.dismiss()
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
                         processSecure3DPayment(context, transaction, object : TransactionCallback() {
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

             override fun onBankSubmit(dialog: Dialog?, bankAccount: BankAccount) {
                 transaction.bankAccount = bankAccount
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