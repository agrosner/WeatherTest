package com.grosner.weathertest.utils

import com.andrewgrosner.okbinding.bindings.BindingConverter
import com.andrewgrosner.okbinding.bindings.on

fun <TBinding : BindingConverter<Double>> TBinding.forTemp() = on { "${it.toInt()}°" }