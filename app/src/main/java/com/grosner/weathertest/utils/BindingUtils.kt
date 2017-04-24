package com.grosner.weathertest.utils

import com.andrewgrosner.kbinding.bindings.BindingConverter
import com.andrewgrosner.kbinding.bindings.on

fun <Data, TBinding : BindingConverter<Data, Double>> TBinding.forTemp() = on { "${it?.toInt() ?: "-"}°" }