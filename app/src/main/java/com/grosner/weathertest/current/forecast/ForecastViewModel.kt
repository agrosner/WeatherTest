package com.grosner.weathertest.current.forecast

import com.fuzz.android.mvvm_lite.ModelViewModel
import com.grosner.weathertest.R
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class ForecastViewModel : ModelViewModel<List_>() {

    val dayFormatter = DateTimeFormatter.ofPattern("EEE")!!

    var tempMin: Double = 0.0
    var tempMax: Double = 0.0

    var day: String = ""
    var image: Int = R.drawable.ic_sun_small

    override fun populateFromModel(model: List_) {
        val m = model.temp
        if (m != null) {
            tempMin = m.min
            tempMax = m.max
        }

        day = dayFormatter.format(Instant.ofEpochMilli(model.dt * 1000)
                .atZone(ZoneId.systemDefault()).toLocalDateTime())
        notifyChange()
    }

}