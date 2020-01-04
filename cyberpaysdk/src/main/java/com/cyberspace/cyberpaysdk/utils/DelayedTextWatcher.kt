package com.cyberspace.cyberpaysdk.utils

import android.os.Handler
import android.text.Editable
import android.text.TextWatcher

open class DelayedTextWatcher : TextWatcher {
    interface DelayedTextWatcherListener {
        fun onTimeout(text: CharSequence?)
    }

    private var idle_min: Long = 2000 // 2 seconds after user stops typing
    private var last_text_edit: Long = 0
    private val h: Handler = Handler()
    private var text: CharSequence? = null
    private var ignoreNextChange = false
    private var listener: DelayedTextWatcherListener? = null

    constructor() {}
    constructor(l: DelayedTextWatcherListener?) {
        listener = l
    }

    override fun afterTextChanged(s: Editable) {}
    override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
    ) {
    }

    override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        if (ignoreNextChange) {
            ignoreNextChange = false
            return
        }
        last_text_edit = System.currentTimeMillis()
        text = s
        h.removeCallbacks(input_finish_checker)
        h.postDelayed(input_finish_checker, idle_min)
    }

    private val input_finish_checker = Runnable {
        if (System.currentTimeMillis() > last_text_edit + idle_min) {
            if (listener != null && text != null) listener!!.onTimeout(text)
        }
    }

    fun setTimeout(t: Long) {
        idle_min = t
    }

    fun setListener(l: DelayedTextWatcherListener?) {
        listener = l
    }

    fun bypassNextChange() {
        ignoreNextChange = true
    }
}