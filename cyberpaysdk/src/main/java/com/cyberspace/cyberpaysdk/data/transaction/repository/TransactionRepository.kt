package com.cyberspace.cyberpaysdk.data.transaction.repository

import com.cyberspace.cyberpaysdk.data.base.remote.ApiResponse
import com.cyberspace.cyberpaysdk.data.transaction.Transaction
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.SetTransaction
import com.google.gson.JsonObject
import io.reactivex.Observable

internal interface TransactionRepository {

    fun beginTransaction(transaction : Transaction) : Observable<ApiResponse<SetTransaction>>?
    fun chargeCard (transaction: Transaction) :  Observable<JsonObject>
    fun verifyTransaction(reference: String)
    fun verifyMerchantTransaction(merchantReference: String) : Observable<JsonObject>
    fun verifyOtp (transaction: Transaction) : Observable<JsonObject>
    fun verifyBankOtp (transaction: Transaction) : Observable<JsonObject>

}