package com.cyberspace.cyberpayapp

import android.app.Application
import android.util.Log
import com.cyberspace.cyberpaysdk.CyberpaySdk
import com.cyberspace.cyberpaysdk.enums.Mode

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        CyberpaySdk.initialiseSdk("3e1d7d40b9e043b694c7516148f23c3f", Mode.Live)
        CyberpaySdk.merchantLogo = resources.getDrawable(R.drawable.debit_card)
    }
}