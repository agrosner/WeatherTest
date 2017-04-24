package com.fuzz.android.mvvm_lite

import java.util.concurrent.CopyOnWriteArraySet

/**
 * Description: Collection of functions that operate as one unit.

 * @author Andrew Grosner (fuzz)
 */
class EventGroup<T> {

    private val _eventSet: MutableSet<(T) -> Unit> = CopyOnWriteArraySet<(T) -> Unit>()
    private val eventSet: MutableSet<(T) -> Unit>
        @Synchronized
        get() = _eventSet

    fun register(viewModelEvent: (T) -> Unit) = eventSet.add(viewModelEvent)

    fun deregister(viewModelEvent: (T) -> Unit) = eventSet.remove(viewModelEvent)

    fun clear() = eventSet.clear()

    operator fun invoke(type: T) = eventSet.forEach { it(type) }
}
