package com.cyberspace.cyberpaysdk

import com.cyberspace.cyberpaysdk.data.model.Transaction

abstract class TransactionCallback {

    abstract fun onSuccess(transaction : Transaction)
    abstract fun onError(transaction : Transaction, throwable: Throwable)
    abstract fun onValidate(transaction: Transaction)

}