package com.cyberspace.cyberpaysdk.data.transaction.repository

import com.cyberspace.cyberpaysdk.data.base.remote.ApiResponse
import com.cyberspace.cyberpaysdk.model.Transaction
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.*
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.CardTransaction
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.OtpResponse
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.SetTransaction
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.VerifyMerchantTransaction
import com.google.gson.JsonObject
import io.reactivex.Observable

internal interface TransactionRepository {

    fun beginTransaction(transaction : Transaction) : Observable<ApiResponse<SetTransaction>>?
    fun chargeCard (transaction: Transaction) :  Observable<ApiResponse<CardTransaction>>?
    fun verifyTransactionByReference(reference: String) : Observable<ApiResponse<VerifyTransaction>>?
    fun verifyTransactionByMerchantReference(merchantReference: String) : Observable<ApiResponse<VerifyMerchantTransaction>>?
    fun verifyCardOtp (transaction: Transaction) : Observable<ApiResponse<OtpResponse>>?
    fun verifyBankOtp (transaction: Transaction) : Observable<ApiResponse<OtpResponse>>?
    fun chargeBank (transaction: Transaction) : Observable<ApiResponse<ChargeBank>>?

}