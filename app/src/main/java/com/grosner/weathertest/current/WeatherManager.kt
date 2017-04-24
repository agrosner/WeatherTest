package com.grosner.weathertest.current

import com.grosner.weathertest.api.Callback
import com.grosner.weathertest.current.forecast.WeatherForecastResponse
import com.grosner.weathertest.current.today.Coordinate
import com.grosner.weathertest.current.today.WeatherResponse
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Description:
 */
class WeatherManager(val weatherService: WeatherService) {

    fun downloadWeatherData(success: Callback<WeatherResponse>)
            = weatherService.getCurrentWeather(Coordinate().apply { lat = 35.0; lon = 139.0 })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { success(it) }!!

    fun downloadWeekForecast(id: Long, success: Callback<WeatherForecastResponse>)
            = weatherService.getWeatherForecastForCity(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { success(it) }!!
}