package com.grosner.weathertest

import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen

class WeatherApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        _context = this
    }

    companion object {

        val component by lazy { DaggerAppComponent.create()!! }

        private var _context: Context? = null
        val context
            get() = _context!!
    }
}