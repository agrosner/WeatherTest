package com.fuzz.android.mvvm_lite

/**
 * Description: Provides methods converting list of model into viewmodels.
 *
 * @author Andrew Grosner (fuzz)
 */
open class ModelListViewModel<TModel, TViewModel : ModelViewModel<TModel>>
@JvmOverloads
constructor(protected var _viewModelCreator: ViewModelCreator<TViewModel>? = null,
            viewModelInstanceState: ViewModelInstanceState<TViewModel>
            = ListViewModel.DefaultViewModelInstanceState())
    : ListViewModel<TViewModel>(viewModelInstanceState) {

    val viewModelCreator: ViewModelCreator<TViewModel> by lazy {
        _viewModelCreator ?: throw IllegalStateException("You must specify a " +
                "ViewModelCreator so when converting from models, it can be used.")
    }

    interface ModelSetter<in Model, in ViewModel : com.fuzz.android.mvvm_lite.ViewModel> {

        fun setModel(model: Model, viewModel: ViewModel)
    }


    open fun setModelList(modelList: MutableList<TModel>?) {
        // cleanup any existing TViewModel references here.
        model?.forEach { it.dispose() }
        model = modelList?.let { convertIntoViewModelList(modelList) } ?: mutableListOf()
    }

    open fun addModelList(modelList: MutableList<TModel>?) = modelList?.let {
        this += convertIntoViewModelList(modelList)
    }

    fun indexOf(item: TModel) = model?.indexOfFirst { item?.equals(it) ?: false } ?: -1

    protected open fun convertIntoViewModelList(modelList: MutableList<TModel>): MutableList<TViewModel> {
        return ViewModelConversions.convertIntoViewModelList(modelList, viewModelCreator,
                onViewModelReadyListener)
    }
}