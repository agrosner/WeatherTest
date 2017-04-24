package com.grosner.weathertest.base.widget

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.andrewgrosner.okbinding.BindingHolder
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext

/**
 * Description: ViewHolder that holds a required binding.
 */
abstract class BaseViewHolder<C : AnkoComponent<ViewGroup>, Data>(view: View)
    : RecyclerView.ViewHolder(view) {

    constructor(parent: ViewGroup, component: C) : this(component.createView(AnkoContext.create(parent.context, parent)))

    var holder: BindingHolder<Data>? = null

    val context
        get() = itemView.context

    fun bind(data: Data) {
        if (holder != null) {
            holder?.unbindAll()
        } else {
            holder = BindingHolder(data)
        }
        applyBindings(data, holder!!)
        holder?.bindAll()
    }

    abstract fun applyBindings(data: Data, holder: BindingHolder<Data>)
}

class NoBindingViewHolder<C : AnkoComponent<ViewGroup>, Any>(view: View)
    : BaseViewHolder<C, Any>(view) {
    override fun applyBindings(data: Any, holder: BindingHolder<Any>) = Unit
}
