package com.cyberspace.cyberpaysdk.data.base.remote

internal interface Service {
   fun <T> create(classService: Class<T>): T?
}