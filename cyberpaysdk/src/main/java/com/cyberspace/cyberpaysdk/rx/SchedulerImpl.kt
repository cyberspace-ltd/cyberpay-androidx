package com.cyberspace.cyberpaysdk.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SchedulerImpl : com.cyberspace.cyberpaysdk.rx.Scheduler {

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun background(): Scheduler {
        return Schedulers.io()
    }
}