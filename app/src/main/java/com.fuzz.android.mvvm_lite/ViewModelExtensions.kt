package com.fuzz.android.mvvm_lite

/**
 * Description: Wraps [ListViewModel.beginTransaction] with [ListViewModel.endTransactionAndNotify]
 * in an action call.
 */
inline fun <TViewModel : ViewModel> ListViewModel<TViewModel>.transact(action: ListViewModel<TViewModel>.() -> Unit)
        : ListViewModel<TViewModel> {
    beginTransaction()
    action(this)
    return endTransactionAndNotify()
}