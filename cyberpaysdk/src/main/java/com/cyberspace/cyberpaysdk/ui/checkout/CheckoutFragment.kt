package com.cyberspace.cyberpaysdk.ui.checkout

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.cyberspace.cyberpaysdk.CyberpaySdk
import com.cyberspace.cyberpaysdk.R
import com.cyberspace.cyberpaysdk.data.bank.remote.response.BankResponse
import com.cyberspace.cyberpaysdk.enums.Mode
import com.cyberspace.cyberpaysdk.enums.PaymentOption
import com.cyberspace.cyberpaysdk.model.Card
import com.cyberspace.cyberpaysdk.model.Transaction
import com.cyberspace.cyberpaysdk.ui.bank.BankFragment
import com.cyberspace.cyberpaysdk.ui.bank.OnSelected
import com.cyberspace.cyberpaysdk.ui.confirm_redirect.ConfirmRedirect
import com.cyberspace.cyberpaysdk.ui.confirm_redirect.OnConfirmed
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.security.InvalidParameterException
import java.text.DecimalFormat


internal class CheckoutFragment constructor(var context: AppCompatActivity,
                                            var transaction: Transaction,
                                            var listener: OnCheckoutSubmitted) : BottomSheetDialogFragment(), CheckoutContract.View{

    lateinit var cardNumber : EditText
    lateinit var expiry : EditText
    lateinit var cvv : EditText

    lateinit var bankName : EditText
    lateinit var accountNumber : EditText
    lateinit var cardType : ImageView
    lateinit var logo : ImageView

    lateinit var close : View
    lateinit var testView : View
    lateinit var amount: TextView

    var isCardNumberError : Boolean = true
    var isCardCvvError : Boolean = true
    var isCardExpiryError : Boolean = true

    private lateinit var cardPay: LinearLayout
    private lateinit var bankPay: LinearLayout
    private lateinit var bankLayout: LinearLayout
    private lateinit var cardLayout: LinearLayout
    private lateinit var account_loading : View
    private lateinit var bank_loading : View

    private lateinit var bankList: MutableList<BankResponse>

    private var mBank: BankResponse? =  null

    //indicators
    private lateinit var cardIndicator: View
    private lateinit var bankIndicator: View

    private lateinit var pay : TextView

    private lateinit var viewPresenter: CheckoutContract.Presenter

    private val card = Card()

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        //Set the custom view
        val view = LayoutInflater.from(context).inflate(R.layout.checkout, null)
        dialog.setContentView(view)

        dialog.setOnShowListener {
            // For AndroidX use: com.google.android.material.R.id.design_bottom_sheet
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as? FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        //dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        account_loading = view.findViewById(R.id.account_loading)
        bank_loading = view.findViewById(R.id.bank_loading)

        cardPay = view.findViewById(R.id.card_pay)
        bankPay = view.findViewById(R.id.bank_pay)

        bankLayout = view.findViewById(R.id.bank_layout)
        cardLayout = view.findViewById(R.id.card_layout)

        cardIndicator = view.findViewById(R.id.card_indicator)
        bankIndicator = view.findViewById(R.id.bank_indicator)

        cardNumber = view.findViewById(R.id.card_number)
        expiry = view.findViewById(R.id.expiry)
        cvv = view.findViewById(R.id.cvv)
        cardType = view.findViewById(R.id.card_type)
        logo = view.findViewById(R.id.logo)

        if(CyberpaySdk.merchantLogo!=null)
            logo.setImageDrawable(CyberpaySdk.merchantLogo)

        pay = view.findViewById(R.id.pay)
        amount = view.findViewById(R.id.amount)


        val df = DecimalFormat("###,###.##")
        pay.text = String.format("%s%s",pay.text, df.format((transaction.amount/100)).toString())

        testView = view.findViewById(R.id.text_banner)

        if(CyberpaySdk.envMode == Mode.Debug) testView.visibility = View.VISIBLE
        else testView.visibility = View.GONE

        bankName = view.findViewById(R.id.bank_name)
        accountNumber = view.findViewById(R.id.account_number)
        close = view.findViewById(R.id.close)

        bankName.setOnClickListener {
            // inflate banks list
           try {
               val bankFragment = BankFragment(context , bankList, object : OnSelected {
                   override fun onSelect(bank: BankResponse) {
                       mBank = bank
                       bankName.setText(bank.bankName)
                       when (bank.processingType){
                           "External" -> confirmRedirect(bank)
                       }
                   }
               })

               bankFragment.show(context.supportFragmentManager, bankFragment.tag)
           } catch (error : java.lang.Exception){}
        }

        close.setOnClickListener {
            dialog.dismiss()
        }

        bankPay.setOnClickListener {
            viewPresenter.bankPay()
        }

        cardPay.setOnClickListener {
            viewPresenter.cardPay()
        }

        this.isCancelable = false
        viewPresenter = CheckoutPresenter()
        attachPresenter(viewPresenter)

//        cardNumber.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
////            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
////                try{
////                    card.number = s.toString()
////                    when(card.type?.issuerName){
////                        "MASTER" -> cardType.setImageResource(R.drawable.master_card)
////                        "VISA" -> cardType.setImageResource(R.drawable.visa_card)
////                        "VERVE" -> cardType.setImageResource(R.drawable.verve_card)
////                    }
////
////                    isCardNumberError = false
////                    expiry.requestFocus()
////
////                }catch (e : Exception){
////                    if(s.toString().length > 15) cardNumber.error = "Invalid Card Number"
////                    cardType.setImageResource(0)
////                    isCardNumberError = true
////                    Log.e("ERROR",e.message)
////                }
////            }
//        })

        cvv.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                try {
                    card.cvv = s.toString()
                    isCardCvvError = false

                    // clear soft keyboard

                    cvv.clearFocus()
                    context.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

                    //val vw = context.currentFocus
//                    vw?.let { v ->
//                        val im = context.getSystemService(Context.INPUT_METHOD_SERVICE)  as? InputMethodManager
//                        im?.hideSoftInputFromWindow(v.windowToken, 0)
//                    }


                }catch (error : java.lang.Exception){
                    cvv.error = "Invalid Card CVV"
                    isCardCvvError = true
                }

            }
        })

        expiry.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    try{
                        val exp = s.toString().split("/")
                        card.expiryMonth = exp[0].toInt()
                        card.expiryYear = exp[1].toInt()

                        cvv.requestFocus()

                        isCardExpiryError = false

                    }
                    catch (ex : InvalidParameterException){
                        expiry.error = ex.message
                        isCardExpiryError = true
                    }
                    catch (ex: java.lang.Exception) {
                        isCardExpiryError = true
                    }
            }
        })

        pay.setOnClickListener {
            //check for card option
           if((viewPresenter as CheckoutPresenter).paymentOption == PaymentOption.Card){
               if(!isCardCvvError && !isCardExpiryError && !isCardNumberError){
                   listener.onCardSubmit(this.dialog,card)
               }

               else {
                   if(isCardNumberError) cardNumber.error = "This is required"
                   if(isCardExpiryError) expiry.error = "This is required"
                   if(isCardCvvError) cvv.error = "This is required"
               }
           }
            else {
               var err = false
               if(bankName.text.toString().isEmpty()) {
                   bankName.error = "This is required"
                   err = true
               }

               if(accountNumber.text.toString().isEmpty() && mBank?.processingType.equals("Internal")) {
                   accountNumber.error = "This is required"
                   err = true
               }

              if(err)  return@setOnClickListener

               when(mBank?.processingType){
                   "External" -> confirmRedirect(mBank!!)
                   "Internal" -> listener.onBankSubmit(dialog, mBank!!)
               }

           }
        }
    }

    fun confirmRedirect(bank: BankResponse){
        val confirmFragment = ConfirmRedirect(getString(R.string.redirect_bank), object : OnConfirmed {
            override fun onConfirm() {
                bankName.setText("")
                listener.onRedirect(dialog, bank)
            }

            override fun onCancel() {
                bankName.setText("")
            }
        })

        confirmFragment.show(context.supportFragmentManager, confirmFragment.tag)
    }

    override fun onError(message: String) {

    }

    override fun onBankPay() {
        cardIndicator.setBackgroundResource(R.color.white)
        bankIndicator.setBackgroundResource(R.color.primaryColorDark)
        bankLayout.visibility = View.VISIBLE
        cardLayout.visibility = View.GONE
        viewPresenter.loadBanks()
    }

    override fun onCardPay() {
        bankIndicator.setBackgroundResource(R.color.white)
        cardIndicator.setBackgroundResource(R.color.primaryColorDark)
        bankLayout.visibility = View.GONE
        cardLayout.visibility = View.VISIBLE
    }

    override fun attachPresenter(presenter: Any) {
        viewPresenter  = presenter as CheckoutContract.Presenter
    }

    override fun close() {

    }

    override fun onLoad() {
        bankName.hint = "Loading..."
        bank_loading.visibility = View.VISIBLE
    }

    override fun onLoadComplete(banks: MutableList<BankResponse>) {
      bankList = banks
        bankName.hint = "Select Bank"
        bank_loading.visibility = View.GONE
    }

    override fun dismiss() {
        super.dismiss()
        viewPresenter.detachView()
    }

    override fun onResume() {
        super.onResume()
        viewPresenter.attachView(this)
    }


}