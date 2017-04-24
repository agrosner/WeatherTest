package com.fuzz.android.mvvm_lite

/**
 * Description: A special kind of [ViewModel] that contains a backing model [T] object.
 * [.populateFromModel] is called whenever this class detects a change.

 * @author Andrew Grosner (fuzz)
 */
abstract class ModelViewModel<T>() : ViewModel() {

    private var _model: T? = null

    open var model: T?
        get() = _model
        set(value) {
            if (_model == null && value != null
                    || _model != value) {
                _model = value
                value?.let { populateFromModel(it) }
                notifySelfChanged()
                notifyChange()
            }
        }

    constructor(model: T) : this() {
        _model = model
    }

    /**
     * Feeds its existing [T] model object through the [populateFromModel] method if the object
     * is not null.
     */
    fun repopulateFromModel() {
        model?.let { populateFromModel(it) }
    }

    protected abstract fun populateFromModel(model: T)
}
