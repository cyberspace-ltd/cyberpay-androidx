package com.cyberspace.cyberpaysdk.data.transaction.remote

import com.cyberspace.cyberpaysdk.data.base.remote.ApiResponse
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.*
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.CardTransaction
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.OtpResponse
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.SetTransaction
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.VerifyMerchantTransaction
import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.http.*

internal interface TransactionService {

    @POST("payments")
    fun beginTransaction(@Body transactionService : MutableMap<String, Any?>) : Observable<ApiResponse<SetTransaction>>

    @POST("payments/card")
    fun chargeCard(@Body transactionService: MutableMap<String, Any?>) : Observable<ApiResponse<CardTransaction>>

    @GET("payments/{transactionReference}")
    fun verifyTransactionByReference(@Path("transactionReference") transactionReference : String) : Observable<ApiResponse<VerifyTransaction>>

    @GET("transactions/transactionsBymerchantRef")
    fun verifyTransactionByMerchantReference(@Query("merchantRef") merchantRef : String) : Observable<ApiResponse<VerifyMerchantTransaction>>

    @POST("payments/otp")
    fun verifyCardOtp(@Body transactionService : MutableMap<String, Any?>) : Observable<ApiResponse<OtpResponse>>

    @POST("payments/bank/otp")
    fun verifyBankOtp(@Body transactionService: MutableMap<String, Any?>) : Observable<ApiResponse<OtpResponse>>?

    @POST("payments/bank")
    fun chargeBank(@Body transactionService: MutableMap<String, Any?>) : Observable<ApiResponse<ChargeBank>>

    @POST("payments/bank/enrol/otp")
    fun enrolOtp(@Body transactionService: Map<String, String>) : Observable<JsonObject>

    @GET("banks")
    fun getBanks() : Observable<JsonObject>

}