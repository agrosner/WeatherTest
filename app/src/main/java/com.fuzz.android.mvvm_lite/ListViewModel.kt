package com.fuzz.android.mvvm_lite

import android.os.Bundle
import com.fuzz.android.mvvm_lite.ListViewModel.Change.Op

typealias ViewModelCreator<TViewModel> = (Int) -> TViewModel
typealias OnViewModelReadyListener<TViewModel> = (TViewModel, Int) -> Unit

/**
 * Description: Provides a [List] style container of [TViewModel] that operate together.

 * @author Andrew Grosner (fuzz)
 */
open class ListViewModel<TViewModel : ViewModel>
@JvmOverloads
constructor(protected var viewModelInstanceState: ViewModelInstanceState<TViewModel> =
            ListViewModel.DefaultViewModelInstanceState())
    : ModelViewModel<MutableList<TViewModel>>(mutableListOf()) {

    data class Change<TViewModel : ViewModel>(
            val listViewModel: ListViewModel<TViewModel>,
            val startPosition: Int,
            val endPosition: Int,
            val op: Change.Op) {
        enum class Op {
            ADD,
            REMOVE,
            SET,
            CHANGE
        }

        val count: Int
            get() = endPosition - startPosition;
    }

    private var _inTransaction = false
    private var inTransaction: Boolean
        @Synchronized
        get() = _inTransaction
        @Synchronized
        set(value) {
            _inTransaction = value
        }

    var onViewModelReadyListener: OnViewModelReadyListener<TViewModel>? = null

    private val collectionChangedEvent = EventGroup<Change<TViewModel>>()

    val count: Int
        get() = model?.size ?: 0

    val isEmpty: Boolean
        get() = count == 0

    val argCount: String by lazy { "${javaClass.simpleName}:count" }

    override var model: MutableList<TViewModel>?
        get() = super.model
        set(value) {
            super.model = value
            notifyChangeIfNeeded(Change(this, 0, model?.size ?: 0, Change.Op.SET))
        }

    fun registerCollectionChangeEvent(sizeChangedEvent: (Change<TViewModel>) -> Unit) {
        this.collectionChangedEvent.register(sizeChangedEvent)
    }

    fun deregisterCollectionChangeEvent(sizeChangedEvent: (Change<TViewModel>) -> Unit) {
        this.collectionChangedEvent.deregister(sizeChangedEvent)
    }

    /**
     * Starts a transaction with changes that at the end [notifySelfChanged] and [collectionChangedEvent]
     * get called. The [collectionChangedEvent] changes with [Op.CHANGE] since we don't do diff calculation
     * of what's actually changed.
     */
    fun beginTransaction(): ListViewModel<TViewModel> {
        inTransaction = true
        return this
    }

    /**
     * Ends transaction and notify both [notifySelfChanged] and [collectionChangedEvent]
     */
    fun endTransactionAndNotify(): ListViewModel<TViewModel> {
        inTransaction = false
        model?.let { notifyChangeIfNeeded(Change(this, 0, it.size, Op.CHANGE)) }
        return this
    }

    /**
     * By default we don't do anything here when populating.
     */
    override fun populateFromModel(model: MutableList<TViewModel>) {

    }

    override fun prepareDependencies() {
        super.prepareDependencies()
        model?.forEach { it.prepare() }
    }

    override fun release() {
        super.release()
        model?.forEach { it.dispose() }
    }

    override fun registerForViewCreated() {
        super.registerForViewCreated()
        model?.forEach { it.viewCreated() }
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        super.onSaveInstanceState(bundle)

        bundle.putInt(argCount, count)
        (0..count - 1).forEach { viewModelInstanceState.onSaveViewModel(bundle, this[it], it) }
    }

    override fun onRestoreInstanceState(bundle: Bundle) {
        super.onRestoreInstanceState(bundle)
        val count = bundle.getInt(argCount)
        model = (0..count - 1).mapNotNull {
            val viewModel = viewModelInstanceState.onRestoreViewModel(bundle, it)
            viewModel?.let { viewModel.prepare() }
            viewModel
        } as MutableList<TViewModel>
    }

    fun addAll(viewModels: MutableList<TViewModel>) = plusAssign(viewModels)

    fun add(viewModel: TViewModel) = plusAssign(viewModel)

    operator fun plusAssign(viewModel: TViewModel) = plusAssign(count, arrayListOf(viewModel))

    operator fun plusAssign(viewModels: MutableList<TViewModel>) = plusAssign(count, viewModels)

    fun plusAssign(location: Int, viewModels: MutableList<TViewModel>?) {
        val model = model
        if (viewModels != null && model != null) {
            val startPos = model.size
            val end = startPos + viewModels.size
            model.addAll(location, viewModels)
            notifyChangeIfNeeded(Change(this, startPos, end, Change.Op.ADD))
        }
    }

    fun remove(viewModels: MutableList<TViewModel>) = minusAssign(viewModels)

    fun remove(viewModel: TViewModel) = minusAssign(viewModel)

    operator fun minusAssign(viewModel: TViewModel) {
        val model = model
        if (model != null) {
            val index = model.indexOf(viewModel)
            model.removeAt(index)

            // notify only one size change when removing single
            notifyChangeIfNeeded(Change(this, startPosition = index, endPosition = index,
                    op = Op.REMOVE))
        }
    }

    operator fun minusAssign(viewModels: MutableList<TViewModel>) {
        val model = model
        if (model != null) {
            model.removeAll(viewModels)

            // notify size change of whole list when removing list
            notifyChangeIfNeeded(Change(this, 0, model.size, Op.REMOVE))
        }
    }

    operator fun set(index: Int, viewModel: TViewModel) {
        val model = model
        if (model != null) {
            model[index] = viewModel

            notifyChangeIfNeeded(Change(this, startPosition = index,
                    endPosition = index,
                    op = Change.Op.SET))
        }
    }


    fun clear() {
        model?.let {
            it.clear()
            notifyChangeIfNeeded(Change(this, 0, 0, Change.Op.REMOVE))
        }
    }

    fun indexOf(viewModel: TViewModel) = model?.indexOf(viewModel) ?: -1

    /**
     * Returns an [TViewModel] at specified position.
     * @throws IndexOutOfBoundsException when [TViewModel] not found at index.
     */
    operator fun get(position: Int) = model?.getOrNull(position) ?:
            throw IndexOutOfBoundsException("Item at position $position not found. List size was $count.")

    /**
     * Nullable call that returns null if the [TViewModel] was not found.
     */
    fun getOrNull(position: Int) = model?.getOrNull(position)

    override fun clearEventCallbacks() {
        super.clearEventCallbacks()
        collectionChangedEvent.clear()
    }

    fun notifyChangeIfNeeded(change: Change<TViewModel>) {
        if (!inTransaction) {
            collectionChangedEvent(change)
            notifySelfChanged()
        }
    }


    internal class DefaultViewModelInstanceState<TViewModel : ViewModel>
        : ViewModelInstanceState<TViewModel> {
        override fun onRestoreViewModel(bundle: Bundle, position: Int) = null

        override fun onSaveViewModel(bundle: Bundle, viewModel: TViewModel, position: Int) = Unit

    }
}
