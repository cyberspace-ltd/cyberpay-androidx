package com.cyberspace.cyberpaysdk.utils.fonts

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class Medium constructor(context : Context, attributeSet : AttributeSet) : AppCompatTextView(context, attributeSet) {

    init {
        this.typeface = Typeface.createFromAsset(context.assets,"fonts/Raleway-Medium.ttf")
    }

}