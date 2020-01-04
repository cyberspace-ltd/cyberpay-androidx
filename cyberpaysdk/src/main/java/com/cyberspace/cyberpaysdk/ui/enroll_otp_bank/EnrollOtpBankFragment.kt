package com.cyberspace.cyberpaysdk.ui.enroll_otp_bank

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.cyberspace.cyberpaysdk.R
import com.cyberspace.cyberpaysdk.model.Transaction
import com.cyberspace.cyberpaysdk.ui.widget.PinPad
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

internal  class EnrollOtpBankFragment (var transaction: Transaction, var listener: OnSubmitted) : BottomSheetDialogFragment() {

    private lateinit var pinPad : PinPad

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        //Set the custom view
        val view = LayoutInflater.from(context).inflate(R.layout.otp_bank, null)
        dialog.setContentView(view)

        dialog.setOnShowListener {
            // For AndroidX use: com.google.android.material.R.id.design_bottom_sheet
            val bottomSheet =
                dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as? FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        // dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        this.isCancelable = false
        pinPad = view.findViewById(R.id.pad)
        pinPad.desc = transaction.message

        pinPad.onSubmitted = object : PinPad.Submitted {
            override fun onSubmit(pin: String) {
                listener.onSubmit(pin)
                dialog.dismiss()
            }
        }
    }

}