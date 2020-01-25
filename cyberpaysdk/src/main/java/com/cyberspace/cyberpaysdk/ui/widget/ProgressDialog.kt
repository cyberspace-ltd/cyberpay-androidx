package com.cyberspace.cyberpaysdk.ui.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.TextView
import com.cyberspace.cyberpaysdk.R

internal class ProgressDialog constructor(context: Context) {

     private var dialog = Dialog(context, R.style.dialog)
     private lateinit var txtMessage : TextView

    fun show(message : String ){
        dialog.setContentView(R.layout.progress_dialog)
        txtMessage = dialog.findViewById(R.id.message)
        txtMessage.text = message
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    fun show(){
        dialog.setContentView(R.layout.progress_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    fun dismiss(){
        dialog.dismiss()
    }


}