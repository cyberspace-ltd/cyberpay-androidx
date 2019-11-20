package com.cyberspace.cyberpaysdk.data.bank.repository

import com.cyberspace.cyberpaysdk.data.bank.remote.response.AccountResponse
import com.cyberspace.cyberpaysdk.data.bank.remote.response.BankResponse
import com.cyberspace.cyberpaysdk.data.base.remote.ApiResponse
import io.reactivex.Observable

internal interface BankRepository {

    fun getBanks() : Observable<MutableList<BankResponse>>?
    fun getAllBanks() : Observable<ApiResponse<MutableList<BankResponse>>>?
    fun getAccountName(bankCode: String, accountNo: String) : Observable<ApiResponse<AccountResponse>>?
}