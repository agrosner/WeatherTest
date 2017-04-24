package com.fuzz.android.mvvm_lite

import android.os.Bundle

/**
 * Description: Base implementation which simplifies the saving/restoring methods to a standard way.
 * The standard way encompasses using a separate isolated [Bundle] instance for each viewmodel.

 * @author Andrew Grosner (fuzz)
 */
abstract class BaseViewModelInstanceState<TViewModel : ViewModel> : ViewModelInstanceState<TViewModel> {

    override fun onRestoreViewModel(bundle: Bundle, position: Int): TViewModel? {
        var viewModel: TViewModel? = null
        val viewModelBundle = bundle.getBundle(getKeyForPosition(position))
        if (viewModelBundle != null) {
            viewModel = getViewModelCreator(bundle, position)(position)
            viewModel.prepare()
            viewModel.onRestoreInstanceState(viewModelBundle)
        }
        return viewModel
    }

    override fun onSaveViewModel(bundle: Bundle, viewModel: TViewModel, position: Int) {
        onSaveViewModel(bundle, getKeyForPosition(position), viewModel)
        val viewModelBundle = Bundle()
        viewModel.onSaveInstanceState(viewModelBundle)
        bundle.putBundle(getKeyForPosition(position), viewModelBundle)
    }

    protected open fun onSaveViewModel(existing: Bundle, key: String, viewModel: TViewModel) {

    }

    protected abstract fun getViewModelCreator(existing: Bundle, position: Int)
            : ViewModelCreator<TViewModel>

    protected abstract fun getKeyForPosition(position: Int): String
}
