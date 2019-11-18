package com.cyberspace.cyberpaysdk.data.bank.repository

import com.cyberspace.cyberpaysdk.data.bank.Bank
import com.cyberspace.cyberpaysdk.data.base.remote.ApiResponse
import io.reactivex.Observable

internal interface BankRepository {

    fun getBanks() : Observable<MutableList<Bank>>?
    fun getAllBanks() : Observable<ApiResponse<MutableList<Bank>>>?


}