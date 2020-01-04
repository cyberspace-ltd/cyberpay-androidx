package com.cyberspace.cyberpaysdk.data.transaction.repository

import com.cyberspace.cyberpaysdk.data.base.remote.ApiClient
import com.cyberspace.cyberpaysdk.data.base.remote.ApiResponse
import com.cyberspace.cyberpaysdk.data.base.remote.ErrorHandler
import com.cyberspace.cyberpaysdk.data.base.remote.Service
import com.cyberspace.cyberpaysdk.model.Transaction
import com.cyberspace.cyberpaysdk.data.transaction.remote.TransactionService
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.*
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.ChargeCard
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.VerifyOtp
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.SetTransaction
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.VerifyMerchantTransaction
import io.reactivex.Observable
import java.lang.NullPointerException


internal class TransactionRepositoryImpl : TransactionRepository{

    private var service : Service = ApiClient()

    override fun beginTransaction(transaction: Transaction): Observable<ApiResponse<SetTransaction>>? {
        if(transaction.customerEmail.isEmpty())throw  NullPointerException("Customer Email Is Required")
        if(!transaction.customerEmail.contains("@")) throw  IllegalArgumentException("Invalid Customer Email Address Found")
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
            ?.onErrorResumeNext { throwable : Throwable ->
               Observable.error(ErrorHandler.getError(throwable))
            }
    }

    override fun enrolBank(transaction: Transaction): Observable<ApiResponse<EnrollBank>>? {
        val param = mutableMapOf<String, Any?>()
        param["bankCode"] = transaction.bankAccount?.bank?.bankCode
        param["accountNumber"] = transaction.bankAccount?.accountNumber
        param["bvn"] = transaction.bankAccount?.bvn
        param["accountName"] = transaction.bankAccount?.accountName
        param["Reference"] = transaction.reference
        param["dateOfBirth"] = transaction.bankAccount?.dateOfBirth

        return service.create(TransactionService::class.java)?.enrolBank(param)
            ?.onErrorResumeNext { throwable : Throwable ->
                Observable.error(ErrorHandler.getError(throwable))
            }
    }

    override fun chargeCard(transaction: Transaction): Observable<ApiResponse<ChargeCard>>? {

        val param = mutableMapOf<String, Any?>()
        param["Expiry"] = transaction.card?.expiry
        param["ExpiryMonth"] = transaction.card?.expiryMonth
        param["ExpiryYear"] =  transaction.card?.expiryYear
        param["CardNumber"] = transaction.card?.number
        param["CVV"] = transaction.card?.cvv
        param["Reference"] = transaction.reference
        if(!transaction.card?.pin.isNullOrEmpty()) param["CardPin"] = transaction.card?.pin

        return service.create(TransactionService::class.java)?.chargeCard(param)
            ?.onErrorResumeNext { throwable : Throwable ->
                Observable.error(ErrorHandler.getError(throwable))
            }

    }

    override fun verifyTransactionByReference(reference: String): Observable<ApiResponse<VerifyTransaction>>? {
      return service.create(TransactionService::class.java)?.verifyTransactionByReference(reference)
          ?.onErrorResumeNext { throwable : Throwable ->
              Observable.error(ErrorHandler.getError(throwable))
          }
    }

    override fun verifyTransactionByMerchantReference(merchantReference: String): Observable<ApiResponse<VerifyMerchantTransaction>>? {
        return service.create(TransactionService::class.java)?.verifyTransactionByMerchantReference(merchantReference)
    }

    override fun verifyCardOtp(transaction: Transaction): Observable<ApiResponse<VerifyOtp>>? {

        val param = mutableMapOf<String, Any?>()
        param["otp"] = transaction.otp
        param["reference"] = transaction.reference
       // param["card"] = transaction.card?.toJson()

        return service.create(TransactionService::class.java)?.verifyCardOtp(param)
            ?.onErrorResumeNext { throwable : Throwable ->
                Observable.error(ErrorHandler.getError(throwable))
            }
    }

    override fun verifyBankOtp(transaction: Transaction): Observable<ApiResponse<VerifyOtp>>? {
        val param = mutableMapOf<String, Any?>()
        param["otp"] = transaction.otp
        param["reference"] = transaction.reference

        return service.create(TransactionService::class.java)?.verifyBankOtp(param)
            ?.onErrorResumeNext { throwable : Throwable ->
                Observable.error(ErrorHandler.getError(throwable))
            }

    }

    override fun chargeBank(transaction: Transaction): Observable<ApiResponse<ChargeBank>>? {
        val param = mutableMapOf<String, Any?>()
        param["BankCode"] = transaction.bankAccount?.bank?.bankCode
        param["AccountNumber"] = transaction.bankAccount?.accountNumber
        param["Reference"] = transaction.reference
        param["AccountName"] = transaction.bankAccount?.accountName
        param["dateOfBirth"] = transaction.bankAccount?.dateOfBirth
        param["bvn"] = transaction.bankAccount?.bvn

        return service.create(TransactionService::class.java)?.chargeBank(param)
            ?.onErrorResumeNext { throwable : Throwable ->
                Observable.error(ErrorHandler.getError(throwable))
            }
    }

    override fun finalBankOtp(transaction: Transaction): Observable<ApiResponse<VerifyOtp>>? {
        val param = mutableMapOf<String, Any?>()
        param["reference"] = transaction.reference
        param["otp"] = transaction.otp
        return service.create(TransactionService::class.java)?.finalBankOtp(param)
            ?.onErrorResumeNext { throwable : Throwable ->
                Observable.error(ErrorHandler.getError(throwable))
            }
    }

    override fun mandateBankOtp(transaction: Transaction): Observable<ApiResponse<EnrollOtp>>? {
        val param = mutableMapOf<String, Any?>()
        param["reference"] = transaction.reference
        param["otp"] = transaction.otp
        return service.create(TransactionService::class.java)?.mandateBankOtp(param)
            ?.onErrorResumeNext { throwable : Throwable ->
                Observable.error(ErrorHandler.getError(throwable))
            }
    }

    override fun enrollBankOtp(transaction: Transaction): Observable<ApiResponse<EnrollOtp>>? {
        val param = mutableMapOf<String, Any?>()
        param["reference"] = transaction.reference
        param["otp"] = transaction.otp
        return service.create(TransactionService::class.java)?.enrolBankOtp(param)
            ?.onErrorResumeNext { throwable : Throwable ->
                Observable.error(ErrorHandler.getError(throwable))
            }
    }

    override fun enrollCardOtp(transaction: Transaction): Observable<ApiResponse<EnrollOtp>>? {
        val param = mutableMapOf<String, Any?>()
        param["reference"] = transaction.reference
        param["registeredPhoneNumber"] = transaction.card?.phoneNumber
        param["cardModel"] = transaction.card?.toJson()

        return service.create(TransactionService::class.java)?.enrolCardOtp(param)
            ?.onErrorResumeNext { throwable : Throwable ->
                Observable.error(ErrorHandler.getError(throwable))
            }

    }
}