package com.cyberspace.cyberpaysdk.di

import com.cyberspace.cyberpaysdk.CyberpaySdk
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module abstract class ApplicationModule {

    @Provides
    internal fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }


}