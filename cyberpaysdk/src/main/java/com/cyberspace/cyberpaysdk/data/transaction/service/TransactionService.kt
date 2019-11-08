package com.cyberspace.cyberpaysdk.data.transaction.service

import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.http.*

interface TransactionService {

    @POST("payments")
    fun beginTransaction(@Body transactionService : Map<String, String>) : Observable<JsonObject>

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
    fun getBank() : Observable<JsonObject>

    /*


    @POST("payments/otp")
    Call<ApiResponse<OtpResponse>> verifyOtp(@Body RequestBody params);

    @POST("payments/bank/otp/{value}")
    Call<ApiResponse<OtpResponse>> verifyBankOtp(@Body RequestBody params, @Path("value") String value);

    @POST("payments/bank")
    Call<ApiResponse<ChargeBankResponse>> chargeBank(@Body RequestBody params);

   @POST("payments/bank/enrol/otp")
    Call<ApiResponse<ChargeBankResponse>> enrolOtp(@Body RequestBody params);

    @GET("banks")
    Call<ApiResponse<List<BankResponse>>> getBank();

*/



}