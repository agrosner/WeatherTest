package com.grosner.weathertest.current

import android.view.View
import com.grosner.weathertest.WeatherApp
import com.grosner.weathertest.base.BaseController
import org.jetbrains.anko.AnkoComponent
import javax.inject.Inject

/**
 * Description:
 */
class CurrentWeatherController : BaseController<CurrentWeatherController>() {


    val viewModel = CurrentWeatherViewModel()

    override fun createComponent(): AnkoComponent<CurrentWeatherController> {
        return CurrentWeatherComponent(viewModel)
    }



    override fun onAttach(view: View) {
        super.onAttach(view)
        viewModel.load()
    }
}