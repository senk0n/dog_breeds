package dev.senk0n.dogbreeds.application.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.senk0n.dogbreeds.shared.core.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class StateViewModel<T> : ViewModel() {

    protected val mutableState = MutableStateFlow<ResultState<T>>(Pending)
    val state = mutableState.asStateFlow()

    /**
     * Function should produce [ResultState] to the lambda's receiver.
     *
     * Initial value is [Pending].
     */
    protected abstract suspend fun MutableStateFlow<ResultState<T>>.load()

    open fun refresh() = viewModelScope.launch(handler) {
        mutableState.load()
    }

    private val handler = CoroutineExceptionHandler { _, throwable ->
        if (throwable is EmptyStateException) {
            mutableState.value = Empty
        } else {
            mutableState.value = Error(throwable)
        }
    }
}

abstract class FlowViewModel<T> : ViewModel() {

    val state = load()
        .map<T, ResultState<T>>(::Success)
        .catch {
            if (it is EmptyStateException) emit(Empty)
            else emit(Error(it))
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, Pending)

    /**
     * Function produce [Success] of [ResultState].
     *
     * Initial value is [Pending].
     *
     * [Error] state is produced by handler through catching exceptions.
     *
     * [Empty] state is produced by throwing [EmptyStateException]
     * @throws EmptyStateException
     */
    protected abstract fun load(): Flow<T>

}
