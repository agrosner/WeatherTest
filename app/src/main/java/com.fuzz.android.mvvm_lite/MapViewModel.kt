package com.fuzz.android.mvvm_lite

import android.os.Bundle

/**
 * Description: Represents a [Map] of [TViewModel].
 */
open class MapViewModel<TKey, TViewModel : ViewModel>(
        protected open val keyGetter: KeyGetter<TKey, *, TViewModel>,
        private val viewModelInstanceState: ViewModelInstanceState<TViewModel> =
        ListViewModel.DefaultViewModelInstanceState())
    : ModelViewModel<MutableMap<TKey, TViewModel>>(mutableMapOf()) {

    interface KeyGetter<out TKey, in TModel, in TViewModel : ViewModel> {

        fun getKey(model: TModel): TKey

        fun getKey(viewModel: TViewModel): TKey
    }

    val count: Int
        get() = model?.size ?: 0

    val isEmpty: Boolean
        get() = count == 0

    val argCount: String by lazy { "${javaClass.simpleName}:count" }

    var onViewModelReadyListener: OnViewModelReadyListener<TViewModel>? = null

    override fun prepareDependencies() {
        super.prepareDependencies()
        model?.values?.forEach { it.prepare() }
    }

    override fun release() {
        super.release()
        model?.values?.forEach { it.dispose() }
    }

    override fun registerForViewCreated() {
        super.registerForViewCreated()
        model?.values?.forEach { it.viewCreated() }
    }

    operator fun get(key: TKey) = model?.get(key) ?:
            throw IndexOutOfBoundsException("Item at key $key not found. Map size was $count.")

    operator fun set(key: TKey, viewModel: TViewModel) {
        val model = model
        if (model != null) {
            model[key] = viewModel
            notifySelfChanged()
        }
    }

    fun getOrNull(key: TKey) = model?.get(key)

    fun getItem(index: Int) = model?.entries?.indices?.firstOrNull { it == index }

    fun clear() {
        model?.clear()
        notifySelfChanged()
    }

    fun toValuesList() = model?.values?.toList() ?: listOf()

    fun toKeysList() = model?.keys?.toList() ?: listOf()

    fun putAll(viewModels: Iterable<Pair<TKey, TViewModel>>) = plusAssign(viewModels)

    fun put(key: TKey, viewModel: TViewModel) = plusAssign(Pair(key, viewModel))

    operator fun plusAssign(pair: Pair<TKey, TViewModel>) = plusAssign(arrayListOf(pair))

    operator fun plusAssign(viewModel: TViewModel) {
        val model = model
        if (model != null) {
            model.put(keyGetter.getKey(viewModel), viewModel)
            notifySelfChanged()
        }
    }

    operator fun plusAssign(viewModels: Iterable<Pair<TKey, TViewModel>>?) {
        val model = model
        if (viewModels != null && model != null) {
            model.plus(viewModels)
            notifySelfChanged()
        }
    }

    fun removeAll(keys: Collection<TKey>) = minusAssign(keys)

    fun remove(key: TKey) = minusAssign(key)

    operator fun minusAssign(key: TKey) = minusAssign(mutableListOf(key))

    operator fun minusAssign(keys: Iterable<TKey>) {
        val model = model
        if (model != null) {
            keys.forEach { model.remove(it) }

            // notify size change of whole list when removing list
            notifySelfChanged()
        }
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)
        bundle.putInt(argCount, count)
        val entriesList = model?.toList() ?: mutableListOf()
        (0..count - 1).forEach { i ->
            val (key, value) = entriesList[i]
            viewModelInstanceState.onSaveViewModel(bundle, value, i)
        }
    }

    override fun onRestoreInstanceState(bundle: Bundle) {
        super.onRestoreInstanceState(bundle)
        val count = bundle.getInt(argCount)
        model = (0..count - 1).mapNotNull {
            val viewModel = viewModelInstanceState.onRestoreViewModel(bundle, it)
            viewModel?.let { viewModel.prepare() }
            if (viewModel != null) {
                keyGetter.getKey(viewModel) to viewModel
            } else {
                null
            }
        }.toMap() as MutableMap<TKey, TViewModel>
    }

    override fun populateFromModel(model: MutableMap<TKey, TViewModel>) {

    }
}