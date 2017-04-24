package com.grosner.weathertest.current

import android.support.v7.widget.LinearLayoutCompat
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import com.andrewgrosner.okbinding.BindingHolder
import com.andrewgrosner.okbinding.bindings.bind
import com.andrewgrosner.okbinding.bindings.onSelf
import com.andrewgrosner.okbinding.bindings.toText
import com.andrewgrosner.okbinding.viewextensions.color
import com.grosner.weathertest.R
import com.grosner.weathertest.base.widget.BaseRecyclerViewAdapter
import com.grosner.weathertest.base.widget.BaseViewHolder
import com.grosner.weathertest.current.forecast.ForecastViewModel
import com.grosner.weathertest.utils.*
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.linearLayoutCompat

class CurrentWeatherListComponent : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
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
            }

            textView {
                id = R.id.day
                textSize = textMediumSmall
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
            }.lparams {
                marginStart = dip(16)
            }
        }
    }
}

class CurrentWeatherItemHolder(ctx: ViewGroup)
    : BaseViewHolder<CurrentWeatherListComponent, ForecastViewModel>(ctx, CurrentWeatherListComponent()) {

    val day = itemView.find<TextView>(R.id.day)
    val tempHigh = itemView.find<TextView>(R.id.tempHigh)
    val tempLow = itemView.find<TextView>(R.id.tempLow)

    override fun applyBindings(data: ForecastViewModel, holder: BindingHolder<ForecastViewModel>) {
        holder.oneWay(data::day, bind { data.day }.onSelf().toText(day))
        holder.oneWay(data::tempMax, bind { data.tempMax }.forTemp().toText(tempHigh))
        holder.oneWay(data::tempMin, bind { data.tempMin }.forTemp().toText(tempLow))
    }
}

class CurrentWeatherAdapter : BaseRecyclerViewAdapter<ForecastViewModel, CurrentWeatherItemHolder,
        CurrentWeatherListComponent>() {

    override fun onCreateItemViewHolder(parent: ViewGroup, viewType: Int) = CurrentWeatherItemHolder(parent)

    override fun onBindViewHolder(holder: CurrentWeatherItemHolder, item: ForecastViewModel, position: Int) {
        holder.bind(item)
    }
}



