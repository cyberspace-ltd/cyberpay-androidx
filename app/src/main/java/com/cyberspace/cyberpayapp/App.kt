package com.cyberspace.cyberpayapp

import android.app.Application
import android.util.Log
import com.cyberspace.cyberpaysdk.CyberpaySdk
import com.cyberspace.cyberpaysdk.enums.Mode

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        //CyberpaySdk.initialiseSdk("d5355204f9cf495f853c8f8d26ada19b", Mode.Debug)//3e1d7d40b9e043b694c7516148f23c3f
        CyberpaySdk.initialiseSdk("3e1d7d40b9e043b694c7516148f23c3f", Mode.Live)
        CyberpaySdk.merchantLogo = resources.getDrawable(R.drawable.debit_card)
    }
}