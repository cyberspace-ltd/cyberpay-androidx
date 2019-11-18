package com.cyberspace.cyberpaysdk.ui.bank

import com.cyberspace.cyberpaysdk.data.bank.Bank

internal interface OnSelected {
    fun onSelect(bank: Bank)
}