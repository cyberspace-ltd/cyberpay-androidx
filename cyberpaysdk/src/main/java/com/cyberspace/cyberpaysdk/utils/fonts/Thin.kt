package com.cyberspace.cyberpaysdk.utils.fonts

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class Thin constructor(context : Context, attributeSet : AttributeSet) : AppCompatTextView(context, attributeSet) {

    init {
        this.typeface = Typeface.createFromAsset(context.assets,"fonts/Raleway-Thin.ttf")
    }

}