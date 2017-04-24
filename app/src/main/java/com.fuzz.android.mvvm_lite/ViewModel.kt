package com.fuzz.android.mvvm_lite

import android.os.Bundle
import com.andrewgrosner.kbinding.BaseObservable

/**
 * Basic ViewModel construct. It has a small lifecycle that it adheres to.
 *
 * [load]
 *   - [prepare]
 *   - [loadContent]
 *
 * [dispose]
 *   - [release]
 *
 *
 */
open class ViewModel : BaseObservable() {

    internal var prepared = false

    private val viewModelEventGroup = EventGroup<ViewModel>()

    private val innerViewModelCollection: MutableList<ViewModel> = mutableListOf()

    open val isValid: Boolean
        get() = true

    /**
     * Listen for changes on this ViewModel that are called when [notifySelfChanged] is called.
     */
    fun registerForEvents(viewModelEvent: (ViewModel) -> Unit) {
        viewModelEventGroup.register(viewModelEvent)
    }

    /**
     * Stop listening for changes on this ViewModel.
     */
    fun deregisterFromEvents(viewModelEvent: (ViewModel) -> Unit) {
        viewModelEventGroup.deregister(viewModelEvent)
    }

    /**
     * Clears out listeners for this ViewModel.
     */
    open fun clearEventCallbacks() = viewModelEventGroup.clear()

    /**
     * Triggers listeners registered in [registerForEvents] to get called.
     */
    open fun notifySelfChanged() = viewModelEventGroup(this)

    /**
     * Notifies that [notifySelfChanged] and that [notifyChange] to databinding.
     */
    open fun notifyViewModelChanged() {
        notifySelfChanged()
        notifyChange()
    }

    /**
     * Call to [loadContent]. If we need to [prepare], the method will get called here.
     */
    fun load() {
        prepare()
        loadContent()
    }

    /**
     * Call to release references to certain dependencies. Usually its good to release callbacks.
     */
    fun dispose() {
        release()
        prepared = false

        innerViewModelCollection.forEach { it.dispose() }
    }

    /**
     * Call this method directly if you wish to control the moment dependencies are injected.
     * If not called when you call [load], it will get called automatically. Subsequent calls
     * to this method will have no effect. Call [rePrepare] to reset dependencies.
     */
    fun prepare() {
        if (!prepared) {
            prepareDependencies()
            prepared = true
        }

        innerViewModelCollection.forEach { it.prepare() }
    }

    /**
     * Call this method after the view is created. Register callbacks, initialize events, etc.
     */
    fun viewCreated() {
        registerForViewCreated()

        innerViewModelCollection.forEach { it.viewCreated() }
    }

    /**
     * Forces [prepare] to [prepareDependencies]
     */
    fun rePrepare() {
        prepared = false
        prepare()

        innerViewModelCollection.forEach { it.rePrepare() }
    }

    /**
     * Clean up any more remaining resources when the fragment or activity is destroyed completely.
     */
    fun destroy() {
        innerViewModelCollection.forEach { it.destroy() }
    }

    /**
     * Call this to save instance state data here.
     */
    open fun onSaveInstanceState(bundle: Bundle) {
        innerViewModelCollection.forEach { it.onSaveInstanceState(bundle) }
    }

    /**
     * Call this to restore instance state data here.
     */
    open fun onRestoreInstanceState(bundle: Bundle) {
        innerViewModelCollection.forEach { it.onRestoreInstanceState(bundle) }
    }

    /**
     * Registers other [ViewModel] for life cycle events.
     */
    protected fun registerForLifeCycle(viewModel: ViewModel) {
        innerViewModelCollection += viewModel
    }

    protected fun deregisterForLifeCycle(viewModel: ViewModel) {
        innerViewModelCollection -= viewModel
    }

    /**
     * Called when [load] is called. It is safe here to operate on injected dependencies and
     * load content for this ViewModel.
     */
    protected open fun loadContent() {

    }

    /**
     * Called before [loadContent], this method is only ever called once and use this method
     * to inject dependencies.
     */
    protected open fun prepareDependencies() {

    }

    /**
     * Release any holdings on expensive objects. Called when [dispose] is called. Once an object
     * is disposed, we must call [load] to
     */
    protected open fun release() {

    }

    protected open fun registerForViewCreated() {

    }


}
