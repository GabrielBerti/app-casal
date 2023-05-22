package br.com.appcasal.ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

typealias OnStartedCallback = (() -> Unit)?
typealias OnLoadingCallback = ((isLoading: Boolean) -> Unit)?
typealias OnSuccessCallback <T> = ((data: T) -> Unit)?
typealias OnErrorCallback = ((e: Throwable) -> Unit)?
typealias ViewStateTemplateScope <T> = ViewStateTemplate<T>.() -> Unit

abstract class ViewStateTemplate<T : Any> {
    private var _onStarted: OnStartedCallback = null
    private var _onLoading: OnLoadingCallback = null
    private var _onError: OnErrorCallback = null
    private var _onSuccess: OnSuccessCallback<T> = null

    fun onStarted(callback: OnStartedCallback) {
        _onStarted = callback
    }

    fun onSuccess(callback: OnSuccessCallback<T>) {
        _onSuccess = callback
    }

    fun onError(callback: OnErrorCallback) {
        _onError = callback
    }

    fun onLoading(callback: OnLoadingCallback) {
        _onLoading = callback
    }

    protected fun execute(state: ViewState<T>) {
        when (state) {
            is ViewState.Initial -> _onStarted?.invoke()
            is ViewState.Loading -> _onLoading?.invoke(true)
            is ViewState.Error -> {
                _onError?.invoke(state.error)
                _onLoading?.invoke(false)

            }
            is ViewState.Success -> {
                _onSuccess?.invoke(state.data)
                _onLoading?.invoke(false)
            }
        }
    }
}

suspend fun <T : Any> StateFlow<ViewState<T>>.collectViewState(
    coroutineScope: CoroutineScope,
    scope: ViewStateTemplateScope<T>
) {
    coroutineScope.launch {
        collect {
            object : ViewStateTemplate<T>() {
                init {
                    apply(scope)
                    execute(it)
                }
            }
        }
    }
}

suspend fun <T : Any> SharedFlow<ViewState<T>>.collectResult(
    coroutineScope: CoroutineScope,
    scope: ViewStateTemplateScope<T>
) {
    coroutineScope.launch {
        collect {
            object : ViewStateTemplate<T>() {
                init {
                    apply(scope)
                    execute(it)
                }
            }
        }
    }
}
