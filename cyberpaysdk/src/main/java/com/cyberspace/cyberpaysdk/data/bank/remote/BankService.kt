package com.cyberspace.cyberpaysdk.data.bank.remote

import com.cyberspace.cyberpaysdk.data.bank.Bank
import com.cyberspace.cyberpaysdk.data.base.remote.ApiResponse
import io.reactivex.Observable
import retrofit2.http.GET

internal interface BankService {

    @GET("banks")
    fun banks () : Observable<ApiResponse<MutableList<Bank>>>

    @GET("banks/all")
    fun getAllBanks () : Observable<ApiResponse<MutableList<Bank>>>

}