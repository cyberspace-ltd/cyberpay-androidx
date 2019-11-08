package com.cyberspace.cyberpaysdk.data.transaction.repository

import com.cyberspace.cyberpaysdk.data.transaction.Transaction
import com.cyberspace.cyberpaysdk.data.transaction.service.TransactionService
import com.google.gson.JsonObject
import io.reactivex.Observable
import javax.inject.Inject

class TransactionRepositoryImpl : TransactionRepository{

    @Inject
    lateinit var service : TransactionService

    override fun beginTransaction(transaction: Transaction): Observable<JsonObject> {
        var param  = emptyMap<String, String>()
        /*
        requires further code refactoring
         */
        return service.beginTransaction(param)
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