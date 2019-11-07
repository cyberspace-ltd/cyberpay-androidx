package com.cyberspace.cyberpaysdk

import android.content.Context
import android.util.Log
import com.cyberspace.cyberpaysdk.data.model.Transaction
import com.cyberspace.cyberpaysdk.enums.Mode

public object CyberpaySdk {

        private var key : String? = null
        private var envMode : Mode = Mode.Debug

        fun initialiseSdk(integrationKey : String) {
            this.key = integrationKey
        }

        fun initialiseSdk(integrationKey  : String, mode: Mode) {
            this.key = integrationKey
            this.envMode = mode
        }

        fun chargeCard(context: Context, transaction : Transaction , transactionCallback: TransactionCallback){

        }







}