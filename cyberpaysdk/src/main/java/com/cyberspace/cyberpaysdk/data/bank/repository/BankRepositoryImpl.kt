package com.cyberspace.cyberpaysdk.data.bank.repository

import com.cyberspace.cyberpaysdk.data.bank.Bank
import com.cyberspace.cyberpaysdk.data.bank.remote.BankService
import com.cyberspace.cyberpaysdk.data.base.remote.ApiResponse
import com.cyberspace.cyberpaysdk.data.base.remote.BaseService
import com.cyberspace.cyberpaysdk.data.base.remote.Service
import io.reactivex.Observable

internal class BankRepositoryImpl : BankRespository {

    private var service : Service = BaseService()

    override fun getBanks(): Observable<ApiResponse<MutableList<Bank>>>? {

            return service.create(BankService::class.java)?.banks()
    }

    override fun getAllBanks(): Observable<ApiResponse<MutableList<Bank>>>? {
        return service.create(BankService::class.java)?.getAllBanks()
    }
}