package com.cyberspace.cyberpaysdk.data.remote.base

interface Service {
   fun <T> create(classService: Class<T>): T?
}