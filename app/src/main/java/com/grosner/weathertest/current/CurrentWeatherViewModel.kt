package com.grosner.weathertest.current

import com.andrewgrosner.okbinding.observable
import com.fuzz.android.mvvm_lite.ModelListViewModel
import com.fuzz.android.mvvm_lite.ViewModel
import com.grosner.weathertest.WeatherApp
import com.grosner.weathertest.current.forecast.ForecastViewModel
import com.grosner.weathertest.current.today.DayWeatherViewModel
import javax.inject.Inject

class CurrentWeatherViewModel : ViewModel() {

    @Inject
    lateinit var weatherManager: WeatherManager

    val todayWeather = observable(DayWeatherViewModel()) { _, prop ->
        notifyChange(prop)
    }

    val days = ModelListViewModel({ ForecastViewModel() })

    init {
        WeatherApp.component.inject(this)
    }

    override fun loadContent() {
        super.loadContent()

        weatherManager.downloadWeatherData {
            todayWeather.value.apply { model = it }
            weatherManager.downloadWeekForecast(it.id) { days.setModelList(it.list) }
        }

    }
}