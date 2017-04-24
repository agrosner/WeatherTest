package com.grosner.weathertest

import android.content.Context
import com.grosner.weathertest.utils.FontManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideContext() = WeatherApp.context

    @Singleton
    @Provides
    fun provideFontManager(context: Context) = FontManager(context)
}