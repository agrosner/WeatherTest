package com.grosner.weathertest.current

import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.andrewgrosner.kbinding.anko.BindingComponent
import com.andrewgrosner.kbinding.bindings.onSelf
import com.fuzz.android.mvvm_lite.ListViewModel
import com.grosner.weathertest.R
import com.grosner.weathertest.base.widget.ItemSpacingDecoration
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.dip
import org.jetbrains.anko.recyclerview.v7.recyclerView

class CurrentWeatherComponent(viewModel: CurrentWeatherViewModel)
    : BindingComponent<CurrentWeatherController, CurrentWeatherViewModel>(viewModel) {

    override fun createViewWithBindings(ui: AnkoContext<CurrentWeatherController>) = with(ui) {
        recyclerView {
            lparams {
                width = MATCH_PARENT
                height = WRAP_CONTENT
            }
            layoutManager = LinearLayoutManager(context)
            adapter = CurrentWeatherAdapter().apply {
                addHeaderHolder(R.id.todayHeader, TodayWeatherHolder(this@recyclerView))
            }
            addItemDecoration(ItemSpacingDecoration(dip(2), 0))

            // bind data to header
            bind { it.todayWeather }.onSelf().toView(this) { recyclerView, vm ->
                vm?.let { vm ->
                    val adapter = recyclerView.adapter as CurrentWeatherAdapter
                    (adapter.headerHolders[0] as TodayWeatherHolder).bind(vm)
                }
            }

            // bind collection changes to adapter
            viewModel?.days?.registerCollectionChangeEvent {
                val adapter = this.adapter as CurrentWeatherAdapter
                if (it.op == ListViewModel.Change.Op.SET) {
                    adapter.setItemsList(it.listViewModel.model)
                } else if (it.op == ListViewModel.Change.Op.ADD) {
                    adapter.addItemsList(it.listViewModel.model?.subList(it.startPosition, it.endPosition))
                }
            }
        }
    }
}

