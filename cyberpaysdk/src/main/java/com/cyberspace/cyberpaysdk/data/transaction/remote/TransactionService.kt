package com.cyberspace.cyberpaysdk.data.transaction.remote

import com.cyberspace.cyberpaysdk.data.base.remote.ApiResponse
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.SetTransaction
import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.http.*

internal interface TransactionService {

    @POST("payments")
    fun beginTransaction(@Body transactionService : MutableMap<String, Any?>) : Observable<ApiResponse<SetTransaction>>

    @POST("payments/card")
    fun chargeCard(@Body transactionService: Map<String, String>) : Observable<JsonObject>

    @GET("payments/{transactionReference}")
    fun verifyTransaction(@Path("transactionReference") transactionReference : String) : Observable<JsonObject>

    @GET("transactions/transactionsBymerchantRef")
    fun verifyMerchantTransaction(@Query("merchantRef") merchantRef : String) : Observable<JsonObject>

    @POST("payments/otp")
    fun verifyOtp(@Body transactionService : Map<String , String>) : Observable<JsonObject>

    @POST("payments/bank/otp/{value}")
    fun verifyBankOtp(@Body transactionService: Map<String, String>, @Path("value") value : String) : Observable<JsonObject>

    @POST("payments/bank")
    fun chargeBank(@Body transactionService: Map<String, String>) : Observable<JsonObject>

    @POST("payments/bank/enrol/otp")
    fun enrolOtp(@Body transactionService: Map<String, String>) : Observable<JsonObject>

    @GET("banks")
    fun getBanks() : Observable<JsonObject>

}