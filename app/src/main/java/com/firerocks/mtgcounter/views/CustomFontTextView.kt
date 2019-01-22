package com.firerocks.mtgcounter.views

import android.content.Context
import android.graphics.Typeface
import androidx.appcompat.widget.AppCompatTextView
import android.util.AttributeSet

/**
 * Custom text view since I didn't want to keep telling text views to use the medusa_gothic font.
 */
class CustomFontTextView: AppCompatTextView {

    private val newTypeFace: Typeface = Typeface.createFromAsset(context.assets, "fonts/medusa_gothic.otf")

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int):
            super(context, attributeSet, defStyle) {
        typeface = newTypeFace
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        typeface = newTypeFace
    }

    constructor(context: Context): super(context) {
        typeface = newTypeFace
    }
}