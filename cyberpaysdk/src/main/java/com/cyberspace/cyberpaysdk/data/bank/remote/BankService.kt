package com.cyberspace.cyberpaysdk.data.bank.remote

import com.cyberspace.cyberpaysdk.data.bank.remote.response.AccountResponse
import com.cyberspace.cyberpaysdk.data.bank.remote.response.BankResponse
import com.cyberspace.cyberpaysdk.data.base.remote.ApiResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

internal interface BankService {

    @GET("banks")
    fun banks () : Observable<ApiResponse<MutableList<BankResponse>>>

    @GET("banks/all")
    fun getAllBanks () : Observable<ApiResponse<MutableList<BankResponse>>>

    @POST("payments/bankaccount/name")
    fun getAccountName (
        @Header("ApiKey") apiKey: String,
        @Body data : MutableMap<String, Any?>) : Observable<ApiResponse<AccountResponse>>

}