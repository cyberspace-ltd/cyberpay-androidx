package com.cyberspace.cyberpayapp

import android.app.Application
import android.util.Log
import com.cyberspace.cyberpaysdk.CyberpaySdk
import com.cyberspace.cyberpaysdk.enums.Mode

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        CyberpaySdk.initialiseSdk("Your Integration Key", Mode.Debug)
        CyberpaySdk.merchantLogo = resources.getDrawable(R.drawable.debit_card)
    }
}