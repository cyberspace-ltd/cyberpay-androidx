package com.cyberspace.cyberpaysdk.data.transaction.repository

import android.util.Log
import com.cyberspace.cyberpaysdk.data.base.remote.ApiResponse
import com.cyberspace.cyberpaysdk.data.base.remote.BaseService
import com.cyberspace.cyberpaysdk.data.base.remote.Service
import com.cyberspace.cyberpaysdk.data.transaction.Transaction
import com.cyberspace.cyberpaysdk.data.transaction.remote.response.SetTransaction
import com.cyberspace.cyberpaysdk.data.transaction.remote.TransactionService
import com.google.gson.JsonObject
import io.reactivex.Observable

internal class TransactionRepositoryImpl : TransactionRepository{

    private var service : Service = BaseService()

    override fun beginTransaction(transaction: Transaction): Observable<ApiResponse<SetTransaction>>? {
        val param  = mutableMapOf<String, Any?>()
        param["currency"] = transaction.currency
        param["merchantRef"] = transaction.merchantReference
        param["amount"] = transaction.amount
        param["description"] = transaction.description
        param["integrationKey"] = transaction.key
        param["returnUrl"] = transaction.returnUrl
        param["splits"] = transaction.splits

        return service.create(TransactionService::class.java)?.beginTransaction(param)
    }

    override fun chargeCard(transaction: Transaction): Observable<JsonObject> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun verifyTransaction(reference: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun verifyMerchantTransaction(merchantReference: String): Observable<JsonObject> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun verifyOtp(transaction: Transaction): Observable<JsonObject> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun verifyBankOtp(transaction: Transaction): Observable<JsonObject> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}