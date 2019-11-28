package com.cyberspace.cyberpaysdk.data.transaction.remote

import com.cyberspace.cyberpaysdk.data.base.remote.ApiResponse
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.*
import io.reactivex.Observable
import retrofit2.http.*


internal interface TransactionService {

    @POST("payments")
    fun beginTransaction(@Body transactionService : MutableMap<String, Any?>) : Observable<ApiResponse<SetTransaction>>

    @POST("payments/card")
    fun chargeCard(@Body transactionService: MutableMap<String, Any?>) : Observable<ApiResponse<ChargeCard>>

    @GET("payments/{reference}")
    fun verifyTransactionByReference(@Path("reference") transactionReference : String) : Observable<ApiResponse<VerifyTransaction>>

    @GET("transactions/transactionsBymerchantRef")
    fun verifyTransactionByMerchantReference(@Query("merchantRef") merchantRef : String) : Observable<ApiResponse<VerifyMerchantTransaction>>

    @POST("payments/otp")
    fun verifyCardOtp(@Body transactionService : MutableMap<String, Any?>) : Observable<ApiResponse<VerifyOtp>>

    @POST("payments/bank/otp")
    fun verifyBankOtp(@Body transactionService: MutableMap<String, Any?>) : Observable<ApiResponse<VerifyOtp>>?

    @POST("payments/bank")
    fun chargeBank(@Body transactionService: MutableMap<String, Any?>) : Observable<ApiResponse<ChargeBank>>

    @POST("payments/bank/enrol/otp")
    fun enrolBankOtp(@Body request: MutableMap<String, Any?>) : Observable<ApiResponse<EnrollOtp>>?

    @POST("payments/card/enrol")
    fun enrolCardOtp(@Body request: MutableMap<String, Any?>): Observable<ApiResponse<EnrollOtp>>?

}