package com.grosner.weathertest.current

import android.support.v7.widget.LinearLayoutCompat
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.andrewgrosner.kbinding.BindingRegister
import com.andrewgrosner.kbinding.anko.BindingComponent
import com.andrewgrosner.kbinding.bindings.onSelf
import com.andrewgrosner.kbinding.bindings.toText
import com.andrewgrosner.kbinding.viewextensions.color
import com.grosner.weathertest.R
import com.grosner.weathertest.base.widget.BaseRecyclerViewAdapter
import com.grosner.weathertest.base.widget.BaseViewHolder
import com.grosner.weathertest.current.forecast.ForecastViewModel
import com.grosner.weathertest.utils.*
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.linearLayoutCompat

class CurrentWeatherListComponent : BindingComponent<ViewGroup, ForecastViewModel>() {
    override fun createViewWithBindings(ui: AnkoContext<ViewGroup>) = with(ui) {
        linearLayoutCompat {
            orientation = LinearLayoutCompat.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            padding = dip(16)
            backgroundColor = color(R.color.colorPrimary)
            lparams {
                width = MATCH_PARENT
                height = WRAP_CONTENT
            }

            textView {
                id = R.id.tempHigh
                textSize = textLarger
                font = antonFont
                bind(ForecastViewModel::tempMax) { it.tempMax }.forTemp().toText(this)
            }

            textView {
                id = R.id.day
                textSize = textMediumSmall
                bind(ForecastViewModel::day) { it.day }.onSelf().toText(this)
            }.lparams {
                weight = 1.0f
                marginStart = dip(16)
            }

            imageView {
                id = R.id.image
                imageResource = R.drawable.ic_sun_small
            }

            textView {
                id = R.id.tempLow
                textSize = textLarge
                font = antonFont
                bind(ForecastViewModel::tempMin) { it.tempMin }.forTemp().toText(this)
            }.lparams {
                marginStart = dip(16)
            }
        }
    }
}

class CurrentWeatherItemHolder(ctx: ViewGroup)
    : BaseViewHolder<ForecastViewModel>(ctx, CurrentWeatherListComponent()) {

    override fun applyBindings(data: ForecastViewModel, holder: BindingRegister<ForecastViewModel>) {
        holder.viewModel = data
    }
}

class CurrentWeatherAdapter : BaseRecyclerViewAdapter<ForecastViewModel, CurrentWeatherItemHolder,
        CurrentWeatherListComponent>() {

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int) = CurrentWeatherItemHolder(parent)

    override fun onBindViewHolder(holder: CurrentWeatherItemHolder, item: ForecastViewModel, position: Int) {
        holder.bind(item)
    }
}



