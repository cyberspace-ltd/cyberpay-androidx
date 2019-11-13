package com.example.kotlin.ui.base

import java.util.*

internal interface BaseView {

    fun attachPresenter(presenter: Any)
    fun close()

}