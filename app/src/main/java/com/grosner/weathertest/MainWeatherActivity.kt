package com.grosner.weathertest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.grosner.weathertest.current.CurrentWeatherController

/**
 * Description:
 */
class MainWeatherActivity : AppCompatActivity() {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        router = Conductor.attachRouter(this, findViewById(R.id.container) as ViewGroup, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(CurrentWeatherController()))
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}