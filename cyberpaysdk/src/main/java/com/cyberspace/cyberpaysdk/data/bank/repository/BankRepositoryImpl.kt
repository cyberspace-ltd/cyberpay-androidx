package com.cyberspace.cyberpaysdk.data.bank.repository

import android.util.Base64
import com.cyberspace.cyberpaysdk.CyberpaySdk
import com.cyberspace.cyberpaysdk.data.bank.remote.response.BankResponse
import com.cyberspace.cyberpaysdk.data.bank.remote.BankService
import com.cyberspace.cyberpaysdk.data.bank.remote.response.AccountResponse
import com.cyberspace.cyberpaysdk.data.base.remote.ApiResponse
import com.cyberspace.cyberpaysdk.data.base.remote.ApiClient
import com.cyberspace.cyberpaysdk.data.base.remote.ErrorHandler
import com.cyberspace.cyberpaysdk.data.base.remote.Service
import com.cyberspace.cyberpaysdk.data.transaction.remote.TransactionService
import io.reactivex.Observable
import java.net.URLEncoder

internal class BankRepositoryImpl : BankRepository {

    private var service : Service = ApiClient()

    override fun getBanks(): Observable<MutableList<BankResponse>>? {

      return when(com.cyberspace.cyberpaysdk.data.bank.local.Bank.list)
        {
            null ->  service.create(BankService::class.java)?.banks()
                ?.flatMap { res ->
                    com.cyberspace.cyberpaysdk.data.bank.local.Bank.list = res.data
                    Observable.just(res.data)
                }
            else -> Observable.just(com.cyberspace.cyberpaysdk.data.bank.local.Bank.list)
        }

    }

    override fun getAllBanks(): Observable<ApiResponse<MutableList<BankResponse>>>? {
        return service.create(BankService::class.java)?.getAllBanks()
            ?.onErrorResumeNext { throwable : Throwable ->
                Observable.error(ErrorHandler.getError(throwable))
            }
    }

    override fun getAccountName(bankCode: String, accountNo: String): Observable<ApiResponse<AccountResponse>>? {

        val param = mutableMapOf<String, Any?>()
        param["bankCode"] = bankCode
        param["accountId"] = accountNo

        return service.create(BankService::class.java)?.getAccountName(
            Base64.encodeToString(CyberpaySdk.key.toByteArray(), Base64.NO_WRAP)
            ,param)
            ?.onErrorResumeNext { throwable : Throwable ->
                Observable.error(ErrorHandler.getError(throwable))
            }
    }
}