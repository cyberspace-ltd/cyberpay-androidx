package com.cyberspace.cyberpaysdk.data.transaction.repository

import com.cyberspace.cyberpaysdk.data.transaction.Transaction
import com.cyberspace.cyberpaysdk.data.transaction.service.TransactionService
import com.google.gson.JsonObject
import io.reactivex.Observable
import java.lang.ref.Reference
import javax.inject.Inject

interface TransactionRepository {

    fun beginTransaction(transaction : Transaction) : Observable<JsonObject>
    fun chargeCard (transaction: Transaction) :  Observable<JsonObject>
    fun verifyTransaction(reference: String)


}