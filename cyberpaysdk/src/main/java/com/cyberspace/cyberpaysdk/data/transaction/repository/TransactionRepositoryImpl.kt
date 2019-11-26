package com.cyberspace.cyberpaysdk.data.transaction.repository

import com.cyberspace.cyberpaysdk.data.base.remote.ApiResponse
import com.cyberspace.cyberpaysdk.data.base.remote.BaseService
import com.cyberspace.cyberpaysdk.data.base.remote.Service
import com.cyberspace.cyberpaysdk.model.Transaction
import com.cyberspace.cyberpaysdk.data.transaction.remote.TransactionService
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.*
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.CardTransaction
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.VerifyOtp
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.SetTransaction
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.VerifyMerchantTransaction
import io.reactivex.Observable
import java.lang.NullPointerException


internal class TransactionRepositoryImpl : TransactionRepository{

    private var service : Service = BaseService()

    override fun beginTransaction(transaction: Transaction): Observable<ApiResponse<SetTransaction>>? {
        if(transaction.customerEmail.isEmpty())throw  NullPointerException("Customer Email is required")
      //  if(transaction.customerEmail.contains("@"))
        val param  = mutableMapOf<String, Any?>()
        param["currency"] = transaction.currency
        param["merchantRef"] = transaction.merchantReference
        param["amount"] = transaction.amount
        param["description"] = transaction.description
        param["integrationKey"] = transaction.key
        param["returnUrl"] = transaction.returnUrl
        param["customerEmail"] = transaction.customerEmail
        param["clientType"] = transaction.clientType
        param["splits"] = transaction.splits

        return service.create(TransactionService::class.java)?.beginTransaction(param)
    }

    override fun chargeCard(transaction: Transaction): Observable<ApiResponse<CardTransaction>>? {

        val param = mutableMapOf<String, Any?>()
        param["Name"] = ""
        param["ExpiryMonth"] = transaction.card?.expiryMonth
        param["ExpiryYear"] = transaction.card?.expiryYear
        param["CardNumber"] = transaction.card?.number
        param["CVV"] = transaction.card?.cvv
        param["Reference"] = transaction.reference
        param["CardPin"] = transaction.card?.pin

        return service.create(TransactionService::class.java)?.chargeCard(param)
    }

    override fun verifyTransactionByReference(reference: String): Observable<ApiResponse<VerifyTransaction>>? {
      return service.create(TransactionService::class.java)?.verifyTransactionByReference(reference)
    }

    override fun verifyTransactionByMerchantReference(merchantReference: String): Observable<ApiResponse<VerifyMerchantTransaction>>? {
        return service.create(TransactionService::class.java)?.verifyTransactionByMerchantReference(merchantReference)
    }

    override fun verifyCardOtp(transaction: Transaction): Observable<ApiResponse<VerifyOtp>>? {

        val param = mutableMapOf<String, Any?>()
        param["otp"] = transaction.otp
        param["reference"] = transaction.reference
        param["card"] = transaction.card?.toJson()

        return service.create(TransactionService::class.java)?.verifyCardOtp(param)
    }

    override fun verifyBankOtp(transaction: Transaction): Observable<ApiResponse<VerifyOtp>>? {
        val param = mutableMapOf<String, Any?>()
        param["otp"] = transaction.otp
        param["reference"] = transaction.reference

        return service.create(TransactionService::class.java)?.verifyBankOtp(param)

    }

    override fun chargeBank(transaction: Transaction): Observable<ApiResponse<ChargeBank>>? {
        val param = mutableMapOf<String, Any?>()
        param["BankCode"] = transaction.bankCode
        param["AccountNumber"] = transaction.accountNumber
        param["Reference"] = transaction.reference
        param["AccountName"] = transaction.accountName
        param["dateOfBirth"] = transaction.dateOfBirth
        param["bvn"] = transaction.bvn

        return service.create(TransactionService::class.java)?.chargeBank(param)
    }

    override fun enrollBankOtp(transaction: Transaction): Observable<ApiResponse<EnrollOtp>>? {
        val param = mutableMapOf<String, Any?>()
        param["reference"] = transaction.reference
        param["otp"] = transaction.otp

        return service.create(TransactionService::class.java)?.enrolBankOtp(param)
    }

    override fun enrollCardOtp(transaction: Transaction): Observable<ApiResponse<EnrollOtp>>? {
        val param = mutableMapOf<String, Any?>()
        param["reference"] = transaction.reference
        param["registeredPhoneNumber"] = transaction.card?.phoneNumber
        param["cardModel"] = transaction.card?.toJson()

        return service.create(TransactionService::class.java)?.enrolCardOtp(param)

    }
}