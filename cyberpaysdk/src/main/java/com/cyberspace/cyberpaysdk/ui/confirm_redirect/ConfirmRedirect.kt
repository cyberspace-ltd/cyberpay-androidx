package com.cyberspace.cyberpaysdk.ui.confirm_redirect

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.cyberspace.cyberpaysdk.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

internal class ConfirmRedirect (var desc: String, var listener: OnConfirmed) : BottomSheetDialogFragment() {

    private lateinit var close : View
    private lateinit var bt_continue : View
    private lateinit var message: TextView

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        //Set the custom view
            val view = LayoutInflater.from(context).inflate(R.layout.comfirm_redirect, null)
            dialog.setContentView(view)

            dialog.setOnShowListener {
                // For AndroidX use: com.google.android.material.R.id.design_bottom_sheet
                val bottomSheet =
                    dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as? FrameLayout
                val behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

            close = view.findViewById(R.id.close)
            bt_continue = view.findViewById(R.id.bt_continue)
            message = view.findViewById(R.id.message)

            message.text = desc

            close.setOnClickListener {
                listener.onCancel()
                dismiss()
            }
            bt_continue.setOnClickListener {
                listener.onConfirm()
                dismiss()
            }


        }

    }