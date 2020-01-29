package com.cyberspace.cyberpaysdk.data.transaction.remote

import com.cyberspace.cyberpaysdk.data.base.remote.ApiResponse
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.*
import com.google.gson.JsonObject
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

    @POST("payments/bank/enroll")
    fun enrolBank(@Body request: MutableMap<String, Any?>) : Observable<ApiResponse<EnrollBank>>?

    @POST("payments/bank/finalotp")
    fun finalBankOtp(@Body request: MutableMap<String, Any?>) : Observable<ApiResponse<VerifyOtp>>?

    @POST("payments/bank/mandateotp")
    fun mandateBankOtp(@Body request: MutableMap<String, Any?>) : Observable<ApiResponse<EnrollOtp>>?

    @POST("payments/card/enrol")
    fun enrolCardOtp(@Body request: MutableMap<String, Any?>): Observable<ApiResponse<EnrollOtp>>?

    @GET("payments/{reference}/advice")
    fun getTransactionAdvice(@Path("reference") reference : String,
                             @Query("channelcode") channel : String) : Observable<ApiResponse<Advice>>

    @POST("payments/clienttype")
    fun updateTransactionClientType(@Body request: MutableMap<String, Any?>) : Observable<ApiResponse<EnrollOtp>>

    @POST("payments/{reference}/cancel")
    fun cancelTransaction(@Path("reference") reference : String) : Observable<ApiResponse<Any>>


}