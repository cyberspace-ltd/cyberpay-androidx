package com.example.kotlin.ui.base

public interface BasePresenter<V> {

    fun start()
    fun attachView (view : V)
    fun detachView()
    fun destroy()
    fun getView() : V?
}