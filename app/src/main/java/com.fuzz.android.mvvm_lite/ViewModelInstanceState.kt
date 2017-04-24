package com.fuzz.android.mvvm_lite

import android.os.Bundle

/**
 * Description: Provides implementation for restoring and saving state in a [ListViewModel]
 * for individual items.
 */
interface ViewModelInstanceState<TViewModel : ViewModel> {

    /**
     * Restores the view model at specific position. The bundle itself contains a bundle of the item
     * at specified position (if exists).
     */
    fun onRestoreViewModel(bundle: Bundle, position: Int = -1): TViewModel?

    /**
     * Saves a viewmodel to the specified bundle as a separate, inner bundle with unique key at position.
     */
    fun onSaveViewModel(bundle: Bundle, viewModel: TViewModel, position: Int = -1)

}
