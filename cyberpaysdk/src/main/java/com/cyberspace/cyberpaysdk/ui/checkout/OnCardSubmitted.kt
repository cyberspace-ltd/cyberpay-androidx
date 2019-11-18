package com.cyberspace.cyberpaysdk.ui.checkout

import android.app.Dialog
import com.cyberspace.cyberpaysdk.model.Card

internal  interface OnCardSubmitted {
    fun onSubmit(dialog: Dialog? ,card : Card)
}