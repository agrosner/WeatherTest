package com.grosner.weathertest.utils

import android.view.View
import android.widget.TextView
import com.grosner.weathertest.WeatherApp

val View.textHuge
    get() = 53.0f

val View.textLarge
    get() = 24.0f

val View.textMedium
    get() = 20.0f

val View.textMediumSmall
    get() = 18.0f

val View.textLarger
    get() = 35.0f

// fonts

val antonFont = "Anton-Regular"

var TextView.font: String
    get() = throw IllegalAccessException("Font has no getter")
    set(value) {
        typeface = WeatherApp.component.fontManager.getOrCreateTypeFace(value)
    }