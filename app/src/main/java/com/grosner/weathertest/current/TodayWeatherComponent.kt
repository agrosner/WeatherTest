package com.grosner.weathertest.current

import android.view.Gravity.BOTTOM
import android.view.Gravity.TOP
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.andrewgrosner.kbinding.BindingRegister
import com.andrewgrosner.kbinding.anko.BindingComponent
import com.andrewgrosner.kbinding.bindings.onSelf
import com.andrewgrosner.kbinding.bindings.toText
import com.andrewgrosner.kbinding.viewextensions.color
import com.grosner.weathertest.R
import com.grosner.weathertest.base.widget.BaseViewHolder
import com.grosner.weathertest.current.today.DayWeatherViewModel
import com.grosner.weathertest.utils.*
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.linearLayoutCompat

class TodayWeatherComponent : BindingComponent<ViewGroup, DayWeatherViewModel>() {

    override fun createViewWithBindings(ui: AnkoContext<ViewGroup>) = with(ui) {
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
                        bind(DayWeatherViewModel::tempMax) { it.tempMax }.forTemp().toText(this)
                    }.lparams {
                        marginStart = dip(16)
                        gravity = BOTTOM
                    }
                    textView {
                        id = R.id.day
                        textSize = textLarge
                        bind(DayWeatherViewModel::date) { it.date }.onSelf().toText(this)
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
                        bind(DayWeatherViewModel::tempMin) { it.tempMin }.forTemp().toText(this)
                    }.lparams {
                        marginStart = dip(16)
                        gravity = TOP
                    }
                    textView {
                        id = R.id.time
                        textSize = textLarge
                        bind(DayWeatherViewModel::time) { it.time }.onSelf().toText(this)
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
                bind(DayWeatherViewModel::temperature) { it.temperature }.forTemp().toText(this)

            }.lparams {
                below(R.id.image)
                topMargin = dip(16)
            }
            verticalLayout {
                textView {
                    id = R.id.location
                    textSize = textLarge
                    bind(DayWeatherViewModel::location) { it.location }.onSelf().toText(this)
                }
                textView {
                    id = R.id.description
                    textSize = textMedium
                    bind(DayWeatherViewModel::description) { it.description }.onSelf().toText(this)

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
    : BaseViewHolder<DayWeatherViewModel>(parent, TodayWeatherComponent()) {

    override fun applyBindings(data: DayWeatherViewModel, holder: BindingRegister<DayWeatherViewModel>) {
        holder.viewModel = data
    }
}