package com.fuzz.android.mvvm_lite

/**
 * Description: A [Map] of [TKey] to [TViewModel]. It will attempt to reuse its [TViewModel] already
 * if you call [setModelList]. The [keyGetter] specified will do a comparison on the key. If a [ViewModel]
 * already exists in the [Map] of items, we will reuse that object instead of creating a new instance.
 */
open class ModelMapViewModel<TKey, TModel, TViewModel : ModelViewModel<TModel>>(
        protected val viewModelCreator: ViewModelCreator<TViewModel>,
        keyGetter: KeyGetter<TKey, TModel, TViewModel>,
        viewModelInstanceState: ViewModelInstanceState<TViewModel>)
    : MapViewModel<TKey, TViewModel>(keyGetter, viewModelInstanceState) {

    @Suppress("UNCHECKED_CAST")
    override val keyGetter: KeyGetter<TKey, TModel, TViewModel>
        get() = super.keyGetter as KeyGetter<TKey, TModel, TViewModel>

    fun setModelList(modelList: MutableList<TModel>?) {
        // cleanup any existing ViewModel references here.
        model?.values?.forEach { it.dispose() }
        model = modelList?.let { convertIntoViewModelMap(modelList) } ?: mutableMapOf()
    }

    private fun convertIntoViewModelMap(modelList: MutableList<TModel>): MutableMap<TKey, TViewModel> {
        return ViewModelConversions.convertIntoViewModelMap(modelList, model ?: mutableMapOf(),
                keyGetter, viewModelCreator, onViewModelReadyListener)
    }
}