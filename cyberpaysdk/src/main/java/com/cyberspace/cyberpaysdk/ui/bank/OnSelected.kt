package com.cyberspace.cyberpaysdk.ui.bank

import com.cyberspace.cyberpaysdk.data.bank.remote.response.BankResponse

internal interface OnSelected {
    fun onSelect(bank: BankResponse)
}