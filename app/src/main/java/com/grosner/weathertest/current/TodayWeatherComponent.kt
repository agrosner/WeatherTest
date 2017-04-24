package com.grosner.weathertest.current

import android.view.Gravity.BOTTOM
import android.view.Gravity.TOP
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import com.andrewgrosner.okbinding.BindingHolder
import com.andrewgrosner.okbinding.bindings.bind
import com.andrewgrosner.okbinding.bindings.onSelf
import com.andrewgrosner.okbinding.bindings.toText
import com.andrewgrosner.okbinding.viewextensions.color
import com.grosner.weathertest.R
import com.grosner.weathertest.base.widget.BaseViewHolder
import com.grosner.weathertest.current.today.DayWeatherViewModel
import com.grosner.weathertest.utils.*
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.linearLayoutCompat

class TodayWeatherComponent : AnkoComponent<ViewGroup> {

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {
        relativeLayout {
            padding = dip(12)
            backgroundColor = color(R.color.colorPrimary)
            lparams {
                width = MATCH_PARENT
                height = WRAP_CONTENT
            }
            imageView {
                id = R.id.image
                imageResource = R.drawable.ic_sun_medium
            }.lparams {
                topMargin = dip(12)
            }
            verticalLayout {
                id = R.id.dayStatsContainer
                linearLayoutCompat {
                    imageView {
                        imageResource = R.drawable.ic_high_temp
                    }
                    textView {
                        id = R.id.tempHigh
                        textSize = textLarge
                        font = antonFont
                    }.lparams {
                        marginStart = dip(16)
                        gravity = BOTTOM
                    }
                    textView {
                        id = R.id.day
                        textSize = textLarge
                    }.lparams {
                        marginStart = dip(24)
                        gravity = BOTTOM
                    }
                }

                space { lparams { height = dip(8) } }

                linearLayoutCompat {
                    imageView {
                        imageResource = R.drawable.ic_low_temp
                    }
                    textView {
                        id = R.id.tempLow
                        textSize = textLarge
                        font = antonFont
                        textColor = color(R.color.secondaryTextColor)
                    }.lparams {
                        marginStart = dip(16)
                        gravity = TOP
                    }
                    textView {
                        id = R.id.time
                        textSize = textLarge
                    }.lparams {
                        marginStart = dip(24)
                        gravity = TOP
                    }
                }
            }.lparams {
                width = MATCH_PARENT
                height = WRAP_CONTENT
                rightOf(R.id.image)
                topMargin = dip(24)
                marginStart = dip(12)
            }

            textView {
                id = R.id.currentTemp
                textSize = textHuge
                font = antonFont
            }.lparams {
                below(R.id.image)
                topMargin = dip(16)
            }
            verticalLayout {
                textView {
                    id = R.id.location
                    textSize = textLarge
                }
                textView {
                    id = R.id.description
                    textSize = textMedium
                }
            }.lparams {
                width = MATCH_PARENT
                height = WRAP_CONTENT
                topMargin = dip(16)
                marginStart = dip(10)
                rightOf(R.id.currentTemp)
                below(R.id.image)
                topMargin = dip(40)
            }
        }
    }
}

class TodayWeatherHolder(parent: ViewGroup)
    : BaseViewHolder<TodayWeatherComponent, DayWeatherViewModel>(parent, TodayWeatherComponent()) {

    val day = itemView.find<TextView>(R.id.day)
    val time = itemView.find<TextView>(R.id.time)
    val currentTemp = itemView.find<TextView>(R.id.currentTemp)
    val image = itemView.find<ImageView>(R.id.image)
    val location = itemView.find<TextView>(R.id.location)
    val description = itemView.find<TextView>(R.id.description)
    val tempHigh = itemView.find<TextView>(R.id.tempHigh)
    val tempLow = itemView.find<TextView>(R.id.tempLow)

    override fun applyBindings(data: DayWeatherViewModel, holder: BindingHolder<DayWeatherViewModel>) {
        holder.apply {
            oneWay(data::date, bind { data.date }.onSelf().toText(day))
            oneWay(data::temperature, bind { data.temperature }
                    .forTemp().toText(currentTemp))
            oneWay(data::location, bind { data.location }.onSelf().toText(location))
            oneWay(data::description, bind { data.description }.onSelf().toText(description))
            oneWay(data::tempMax, bind { data.tempMax }.forTemp().toText(tempHigh))
            oneWay(data::tempMin, bind { data.tempMin }.forTemp().toText(tempLow))
            oneWay(data::time, bind { data.time }.onSelf().toText(time))
        }
    }
}