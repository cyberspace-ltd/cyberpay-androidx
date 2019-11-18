package com.cyberspace.cyberpaysdk.ui.secure3d

import com.cyberspace.cyberpaysdk.data.transaction.Transaction

internal interface OnFinished {
    fun onFinish(transaction: Transaction)
}