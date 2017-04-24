package com.fuzz.android.mvvm_lite

/**
 * Description: Provides helper conversion methods.
 */
object ViewModelConversions {

    @JvmStatic
    @JvmOverloads
    fun <TModel, TViewModel : ViewModel> convertIntoViewModelList(
            models: List<TModel>,
            modelSetter: ModelListViewModel.ModelSetter<TModel, TViewModel>,
            viewModelCreator: ViewModelCreator<TViewModel>,
            onViewModelReadyListener: OnViewModelReadyListener<TViewModel>? = null)
            : MutableList<TViewModel> {
        return models.indices.asSequence()
                .mapNotNull { i ->
                    val model = models[i]
                    val viewModel = viewModelCreator(i)
                    viewModel.prepare()
                    modelSetter.setModel(model, viewModel)
                    if (viewModel.isValid) {
                        onViewModelReadyListener?.invoke(viewModel, i)
                        viewModel
                    } else {
                        null
                    }
                }.toMutableList()
    }

    @JvmStatic
    @JvmOverloads
    fun <TModel, TViewModel : ModelViewModel<TModel>> convertIntoViewModelList(
            models: List<TModel>,
            viewModelCreator: ViewModelCreator<TViewModel>,
            onViewModelReadyListener: OnViewModelReadyListener<TViewModel>? = null)
            : MutableList<TViewModel> {
        return convertIntoViewModelList(models, object : ModelListViewModel.ModelSetter<TModel, TViewModel> {
            override fun setModel(model: TModel, viewModel: TViewModel) {
                viewModel.model = model
            }
        }, viewModelCreator, onViewModelReadyListener)
    }

    @JvmStatic
    @JvmOverloads
    fun <TKey, TModel, TViewModel : ViewModel> convertIntoViewModelMap(
            models: List<TModel>,
            existingMap: Map<TKey, TViewModel>,
            modelSetter: ModelListViewModel.ModelSetter<TModel, TViewModel>,
            keyGetter: MapViewModel.KeyGetter<TKey, TModel, TViewModel>,
            viewModelCreator: ViewModelCreator<TViewModel>,
            onViewModelReadyListener: OnViewModelReadyListener<TViewModel>? = null)
            : MutableMap<TKey, TViewModel> {
        return models.indices.asSequence().mapNotNull { i ->
            val model = models[i]
            var viewModel = existingMap[keyGetter.getKey(model)]
            if (viewModel == null) {
                viewModel = viewModelCreator(i)
                viewModel.prepare()
            }
            modelSetter.setModel(model, viewModel)
            if (viewModel.isValid) {
                onViewModelReadyListener?.invoke(viewModel, i)
                keyGetter.getKey(viewModel) to viewModel
            } else {
                null
            }
        }.toMap(mutableMapOf())
    }

    @JvmStatic
    @JvmOverloads
    fun <TKey, TModel, TViewModel : ModelViewModel<TModel>> convertIntoViewModelMap(
            models: List<TModel>,
            existingMap: Map<TKey, TViewModel>,
            keyGetter: MapViewModel.KeyGetter<TKey, TModel, TViewModel>,
            viewModelCreator: ViewModelCreator<TViewModel>,
            onViewModelReadyListener: OnViewModelReadyListener<TViewModel>? = null)
            : MutableMap<TKey, TViewModel> {
        return convertIntoViewModelMap(models, existingMap,
                object : ModelListViewModel.ModelSetter<TModel, TViewModel> {
                    override fun setModel(model: TModel, viewModel: TViewModel) {
                        viewModel.model = model
                    }
                }, keyGetter, viewModelCreator, onViewModelReadyListener)
    }
}