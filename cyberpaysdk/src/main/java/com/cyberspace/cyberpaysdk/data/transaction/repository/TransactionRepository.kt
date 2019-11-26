package com.cyberspace.cyberpaysdk.data.transaction.repository

import com.cyberspace.cyberpaysdk.data.base.remote.ApiResponse
import com.cyberspace.cyberpaysdk.model.Transaction
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.*
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.CardTransaction
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.VerifyOtp
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.SetTransaction
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.VerifyMerchantTransaction
import io.reactivex.Observable

internal interface TransactionRepository {

    fun beginTransaction(transaction : Transaction) : Observable<ApiResponse<SetTransaction>>?
    fun chargeCard (transaction: Transaction) :  Observable<ApiResponse<CardTransaction>>?
    fun verifyTransactionByReference(reference: String) : Observable<ApiResponse<VerifyTransaction>>?
    fun verifyTransactionByMerchantReference(merchantReference: String) : Observable<ApiResponse<VerifyMerchantTransaction>>?
    fun verifyCardOtp (transaction: Transaction) : Observable<ApiResponse<VerifyOtp>>?
    fun verifyBankOtp (transaction: Transaction) : Observable<ApiResponse<VerifyOtp>>?
    fun chargeBank (transaction: Transaction) : Observable<ApiResponse<ChargeBank>>?
    fun enrollBankOtp(transaction : Transaction) : Observable<ApiResponse<EnrollOtp>>?
    fun enrollCardOtp(transaction : Transaction) : Observable<ApiResponse<EnrollOtp>>?

}