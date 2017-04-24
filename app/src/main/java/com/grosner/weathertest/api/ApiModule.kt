package com.grosner.weathertest.api

import com.grosner.weathertest.api.converter.LoganSquareConverterFactory
import com.grosner.weathertest.current.WeatherManager
import com.grosner.weathertest.current.WeatherService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Singleton

@Module
class ApiModule {


    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(LoganSquareConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .build()
    }

    @Singleton
    @Provides
    fun provideWeatherService(retrofit: Retrofit) = WeatherService(retrofit)

    @Singleton
    @Provides
    fun provideWeatherManager(weatherService: WeatherService) = WeatherManager(weatherService)

    companion object {

        const val baseUrl = "http://api.openweathermap.org/"
        const val API_KEY = "1dcb12bbf38545fc5b340bebb2b83ad7"

    }
}