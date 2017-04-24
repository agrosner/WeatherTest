package com.grosner.weathertest.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext

abstract class BaseController<C : BaseController<C>> : Controller() {

    abstract fun createComponent(): AnkoComponent<C>

    private var _component: AnkoComponent<C>? = null
    val component: AnkoComponent<C>
        get() {
            if (_component == null) {
                _component = createComponent()
            }
            return _component!!
        }

    override fun onAttach(view: View) {
        super.onAttach(view)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        @Suppress("UNCHECKED_CAST")
        return component.createView(AnkoContext.create(activity!!, this as C))
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        _component = null
    }
}