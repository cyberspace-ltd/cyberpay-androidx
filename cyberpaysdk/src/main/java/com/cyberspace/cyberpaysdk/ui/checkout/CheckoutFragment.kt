package com.cyberspace.cyberpaysdk.ui.checkout

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.cyberspace.cyberpaysdk.CyberpaySdk
import com.cyberspace.cyberpaysdk.R
import com.cyberspace.cyberpaysdk.data.bank.remote.response.AccountResponse
import com.cyberspace.cyberpaysdk.data.bank.remote.response.BankResponse
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.Advice
import com.cyberspace.cyberpaysdk.enums.Mode
import com.cyberspace.cyberpaysdk.enums.PaymentOption
import com.cyberspace.cyberpaysdk.model.BankAccount
import com.cyberspace.cyberpaysdk.model.Card
import com.cyberspace.cyberpaysdk.model.Transaction
import com.cyberspace.cyberpaysdk.ui.bank.BankFragment
import com.cyberspace.cyberpaysdk.ui.bank.OnSelected
import com.cyberspace.cyberpaysdk.ui.confirm_redirect.ConfirmRedirect
import com.cyberspace.cyberpaysdk.ui.confirm_redirect.OnConfirmed
import com.cyberspace.cyberpaysdk.ui.widget.ProgressDialog
import com.cyberspace.cyberpaysdk.utils.DelayedTextWatcher
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding.widget.RxTextView
import java.security.InvalidParameterException
import java.text.DecimalFormat


internal class CheckoutFragment constructor(var transaction: Transaction,
                                            var listener: CheckoutListener) : BottomSheetDialogFragment(), CheckoutContract.View{

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

    private lateinit var verify_layout: View
    private lateinit var cardPay: LinearLayout
    private lateinit var bankPay: LinearLayout
    private lateinit var bankLayout: LinearLayout
    private lateinit var cardLayout: LinearLayout
    private lateinit var account_loading : View
    private lateinit var bank_loading : View
    private lateinit var verified: View
    private lateinit var accountName : TextView

    private lateinit var bankList: MutableList<BankResponse>

    private var bankAccount = BankAccount()

    //indicators
    private lateinit var cardIndicator: View
    private lateinit var bankIndicator: View

    private lateinit var pay : TextView

    private var canContinue = false

    lateinit var viewPresenter: CheckoutContract.Presenter

    private val card = Card()

    private var mAdvice: Advice? = null
    private lateinit var progress: ProgressDialog

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

        progress = ProgressDialog(requireContext())
        verify_layout = view.findViewById(R.id.verify_layout)

        verified = view.findViewById(R.id.verified)
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

        pay.text = String.format("%s%s",pay.text, transaction.amountToPay)

        testView = view.findViewById(R.id.text_banner)

        if(CyberpaySdk.envMode == Mode.Debug) testView.visibility = View.VISIBLE
        else testView.visibility = View.GONE

        bankName = view.findViewById(R.id.bank_name)
        accountNumber = view.findViewById(R.id.account_number)
        accountName = view.findViewById(R.id.account_name)
        close = view.findViewById(R.id.close)

         viewPresenter = CheckoutPresenter()

        accountNumber.addTextChangedListener(DelayedTextWatcher(
            object : DelayedTextWatcher.DelayedTextWatcherListener {
                override fun onTimeout(text: CharSequence?) {
                    if(text.toString().length == 10){
                        requireActivity().hideKeyboard(view)
                        accountNumber.isEnabled = false
                        canContinue = true
                        //accountName.text = requireActivity().resources.getString(R.string.verify)
                        verify_layout.visibility = View.VISIBLE
                        onDisablePay()
                        pay.text = getString(R.string.verify)
                        verified.visibility = View.GONE
                        viewPresenter.getAccountName(bankAccount.bank?.bankCode!!, text.toString())
                    }
                }
            }
        ))

        bankName.setOnClickListener {
            // inflate banks list
           try {
               val bankFragment = BankFragment(bankList, object : OnSelected {
                   override fun onSelect(bank: BankResponse) {
                       bankAccount.bank = bank
                       bankName.setText(bank.bankName)
                       when (bank.processingType){
                           "External" -> confirmRedirect(bank)
                       }

                       accountNumber.isEnabled = true
                   }
               })

               bankFragment.show(requireActivity().supportFragmentManager, bankFragment.tag)
           } catch (error : java.lang.Exception){}
        }

        close.setOnClickListener {
           progress.show("Cancelling Transaction...")
            viewPresenter.cancelTransaction(transaction)
        }

        bankPay.setOnClickListener {
            viewPresenter.bankPay()
        }

        cardPay.setOnClickListener {
            viewPresenter.cardPay()
        }

        this.isCancelable = false

        attachPresenter(viewPresenter)

        RxTextView.textChanges(cardNumber).subscribe{
            try{
                card.number = it.toString()
                when(card.type?.issuerName){
                    "MASTER" -> cardType.setImageResource(R.drawable.master_card)
                    "VISA" -> cardType.setImageResource(R.drawable.visa_card)
                    "VERVE" -> cardType.setImageResource(R.drawable.verve_card)
                }

                isCardNumberError = false
                expiry.requestFocus()
            }catch (e : Exception){
                if(it.toString().length > 15) cardNumber.error = "Invalid Card Number"
                cardType.setImageResource(0)
                isCardNumberError = true
            }
        }

       RxTextView.textChanges(cvv).subscribe {
               try {
                   card.cvv = it.toString()
                   isCardCvvError = false
                   requireActivity().hideKeyboard(view)
               }catch (error : java.lang.Exception){
                   cvv.error = "Invalid Card CVV"
                   isCardCvvError = true
               }
           }


        RxTextView.textChanges(expiry).subscribe{
            try{
                val exp = it.toString().split("/")
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

               if(accountNumber.text.toString().isEmpty() && bankAccount.bank?.processingType.equals("Internal")) {
                   accountNumber.error = "This is required"
                   err = true
               } else if(bankAccount.bank?.processingType.equals("Internal") && accountNumber.text.toString().length!=10){
                   accountNumber.error = "Invalid account number"
                   err = true
               }

              if(err)  return@setOnClickListener

               when(bankAccount.bank?.processingType){
                   "External" -> confirmRedirect(bankAccount.bank!!)
                   "Internal" -> listener.onBankSubmit(dialog, bankAccount)
               }

           }
        }
    }

    override fun onAccountName(account: AccountResponse) {
        verified.visibility = View.VISIBLE
        accountNumber.isEnabled = true

        bankAccount.accountNumber = accountNumber.text.toString()
        bankAccount.accountName = account.accountName

        account.accountName.split(' ').map {
            accountName.text = "${accountName.text} ${it.toLowerCase().capitalize()}"
        }

        verify_layout.visibility = View.GONE
        pay.text = String.format("Pay ₦%s",transaction.amountToPay)
        onEnablePay()
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
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

        confirmFragment.show(requireActivity().supportFragmentManager, confirmFragment.tag)
    }

    override fun onError(message: String) {
        accountNumber.isEnabled = true
        accountNumber.error = "Verify Error"
        verified.visibility = View.GONE
        accountName.text = ""
        pay.text = String.format("Pay ₦%s",transaction.amountToPay)
        verify_layout.visibility = View.GONE
    }

    override fun onBankPay() {
        cardIndicator.setBackgroundResource(R.color.white)
        bankIndicator.setBackgroundResource(R.color.primaryColorDark)
        bankLayout.visibility = View.VISIBLE
        cardLayout.visibility = View.GONE
        viewPresenter.loadBanks()
        viewPresenter.getBankTransactionAdvice(transaction)
        onDisablePay()
    }

    override fun onCardPay() {
        bankIndicator.setBackgroundResource(R.color.white)
        cardIndicator.setBackgroundResource(R.color.primaryColorDark)
        bankLayout.visibility = View.GONE
        cardLayout.visibility = View.VISIBLE
        viewPresenter.getCardTransactionAdvice(transaction)
        onDisablePay()
    }

    override fun onCancelTransaction(transaction: Transaction) {
       progress.dismiss()
        dismiss()
        listener.onCancel(transaction)
    }

    override fun onCancelTransactionError(transaction: Transaction) {
        progress.dismiss()
    }

    override fun attachPresenter(presenter: Any) {
        viewPresenter  = presenter as CheckoutContract.Presenter
    }

    override fun onUpdateAdvice(advice: Advice) {
        pay.text = String.format("Pay ₦%s",advice.amountToPay)
        transaction.amount = advice.amount!!
        transaction.charge = advice.charge!!
        onEnablePay()
    }

    override fun onDisablePay() {
        pay.isEnabled = false
        pay.alpha = 0.3f
    }

    override fun onEnablePay() {
        pay.isEnabled = true
        pay.alpha = 1f
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