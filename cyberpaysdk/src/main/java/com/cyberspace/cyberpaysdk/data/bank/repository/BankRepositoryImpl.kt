package com.cyberspace.cyberpaysdk.data.bank.repository

import com.cyberspace.cyberpaysdk.data.bank.Bank
import com.cyberspace.cyberpaysdk.data.bank.remote.BankService
import com.cyberspace.cyberpaysdk.data.base.remote.ApiResponse
import com.cyberspace.cyberpaysdk.data.base.remote.BaseService
import com.cyberspace.cyberpaysdk.data.base.remote.Service
import io.reactivex.Observable

internal class BankRepositoryImpl : BankRepository {

    private var service : Service = BaseService()

    override fun getBanks(): Observable<MutableList<Bank>>? {

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

    override fun getAllBanks(): Observable<ApiResponse<MutableList<Bank>>>? {
        return service.create(BankService::class.java)?.getAllBanks()
    }
}