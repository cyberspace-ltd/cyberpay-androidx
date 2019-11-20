package com.cyberspace.cyberpaysdk.ui.secure3d

import com.cyberspace.cyberpaysdk.model.Transaction

internal interface OnFinished {
    fun onFinish(transaction: Transaction)
}