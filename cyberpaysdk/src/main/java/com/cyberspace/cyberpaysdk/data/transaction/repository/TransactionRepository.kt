package com.cyberspace.cyberpaysdk.data.transaction.repository

import com.cyberspace.cyberpaysdk.data.base.remote.ApiResponse
import com.cyberspace.cyberpaysdk.model.Transaction
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.*
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.ChargeCard
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.VerifyOtp
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.SetTransaction
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.VerifyMerchantTransaction
import com.google.gson.JsonObject
import io.reactivex.Observable

internal interface TransactionRepository {

    fun beginTransaction(transaction : Transaction) : Observable<ApiResponse<SetTransaction>>?
    fun chargeCard (transaction: Transaction) :  Observable<ApiResponse<ChargeCard>>?
    fun verifyTransactionByReference(reference: String) : Observable<ApiResponse<VerifyTransaction>>?
    fun verifyTransactionByMerchantReference(merchantReference: String) : Observable<ApiResponse<VerifyMerchantTransaction>>?
    fun verifyCardOtp (transaction: Transaction) : Observable<ApiResponse<VerifyOtp>>?
    fun verifyBankOtp (transaction: Transaction) : Observable<ApiResponse<VerifyOtp>>?
    fun chargeBank (transaction: Transaction) : Observable<ApiResponse<ChargeBank>>?
    fun enrollBankOtp(transaction : Transaction) : Observable<ApiResponse<EnrollOtp>>?
    fun enrollCardOtp(transaction : Transaction) : Observable<ApiResponse<EnrollOtp>>?
    fun enrolBank(transaction: Transaction) : Observable<ApiResponse<EnrollBank>>?
    fun finalBankOtp(transaction: Transaction) : Observable<ApiResponse<VerifyOtp>>?
    fun mandateBankOtp(transaction: Transaction) : Observable<ApiResponse<EnrollOtp>>?
    fun getCardTransactionAdvice(transaction: Transaction) : Observable<Advice>?
    fun getBankTransactionAdvice(transaction: Transaction) : Observable<Advice>?
    fun updateTransactionClientType(transaction: Transaction) : Observable<ApiResponse<EnrollOtp>>?
    fun cancelTransaction(transaction: Transaction) : Observable<ApiResponse<Any>>?

}