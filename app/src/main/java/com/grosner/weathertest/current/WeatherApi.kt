package com.grosner.weathertest.current

import com.grosner.weathertest.api.ApiModule
import com.grosner.weathertest.current.forecast.WeatherForecastResponse
import com.grosner.weathertest.current.today.Coordinate
import com.grosner.weathertest.current.today.WeatherResponse
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Description:
 */
interface WeatherApi {

    @GET("/data/2.5/weather")
    fun getCurrentWeather(@Query("lat") lat: Double, @Query("lon") lon: Double,
                          @Query("units") unit: String = "imperial",
                          @Query("appid") apiKey: String = ApiModule.API_KEY)
            : Observable<WeatherResponse>

    @GET("/data/2.5/forecast/daily")
    fun getWeatherForecastForCity(@Query("id") id: Long,
                                  @Query("units") unit: String = "imperial",
                                  @Query("appid") apiKey: String = ApiModule.API_KEY)
            : Observable<WeatherForecastResponse>
}

class WeatherService(retrofit: Retrofit) {

    val api = retrofit.create(WeatherApi::class.java)!!

    fun getCurrentWeather(coordinate: Coordinate) = api.getCurrentWeather(coordinate.lat, coordinate.lon)

    fun getWeatherForecastForCity(id: Long) = api.getWeatherForecastForCity(id)
}