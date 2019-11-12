package com.cyberspace.cyberpaysdk.rx

import dagger.Component
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@Component
class SchedulerImpl @Inject constructor(): com.cyberspace.cyberpaysdk.rx.Scheduler {

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun background(): Scheduler {
        return Schedulers.io()
    }
}