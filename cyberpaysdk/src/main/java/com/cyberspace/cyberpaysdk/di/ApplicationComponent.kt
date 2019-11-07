package com.cyberspace.cyberpaysdk.di

import dagger.Component
import dagger.android.AndroidInjectionModule

@Component(modules = [AndroidInjectionModule::class, ApplicationModule::class])
interface ApplicationComponent {

}