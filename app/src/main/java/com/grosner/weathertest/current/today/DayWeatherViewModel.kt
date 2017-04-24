package com.grosner.weathertest.current.today

import com.fuzz.android.mvvm_lite.ModelViewModel
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

class DayWeatherViewModel : ModelViewModel<WeatherResponse>() {

    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")!!
    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")!!

    var location: String = ""
    var temperature: Double = 0.0
    var tempMin: Double = 0.0
    var tempMax: Double = 0.0
    var description: String = ""
    var main: String = ""
    var date: String = ""
    var time: String = ""

    override fun populateFromModel(model: WeatherResponse) {
        val weather = model.weather?.getOrNull(0)
        if (weather != null) {
            main = weather.main_
            description = weather.description
        }

        val m = model.main
        if (m != null) {
            temperature = m.temp
            tempMin = m.temp_min
            tempMax = m.temp_max
        }
        val country = model.sys?.country ?: ""
        location = "${model.name ?: ""}, $country"

        val lastUpdatedTime = LocalDateTime.now(ZoneId.systemDefault())
        date = dateFormatter.format(lastUpdatedTime)
        time = timeFormatter.format(lastUpdatedTime)

        notifyChange()
    }
}