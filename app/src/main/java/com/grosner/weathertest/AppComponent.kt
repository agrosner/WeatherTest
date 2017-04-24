package com.grosner.weathertest

import com.grosner.weathertest.api.ApiModule
import com.grosner.weathertest.current.CurrentWeatherViewModel
import com.grosner.weathertest.utils.FontManager
import dagger.Component
import javax.inject.Singleton

@Component(modules = arrayOf(ApiModule::class, AppModule::class))
@Singleton
interface AppComponent {

    fun inject(currentWeatherViewModel: CurrentWeatherViewModel)

    val fontManager: FontManager

}