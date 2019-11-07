package com.cyberspace.cyberpaysdk

import com.cyberspace.cyberpaysdk.enums.Mode
import lombok.Setter


public class CyberpaySdk private constructor(){

    companion object {

        private var key : String? = null
        private var envMode : Mode = Mode.Debug

        fun initialiseSdk(integrationKey : String) {
            this.key = integrationKey
        }

        fun initialiseSdk(integrationKey  : String, mode: Mode) {
            this.key = integrationKey
            this.envMode = mode
        }

    }





}