package br.com.appcasal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

typealias OnAnyCallback<T> = (suspend (viewState: ViewState<T>) -> Unit)?
typealias FetchSuspendData<T> = (suspend () -> T)
typealias SuspendViewDataTemplateScope<T> = SuspendViewStateDataTemplate<T>.() -> Unit

abstract class SuspendViewStateDataTemplate<T : Any> {
    private var _onLoading: OnLoadingCallback = null
    private var _onError: OnErrorCallback = null
    private var _onSuccess: OnSuccessCallback<T> = null
    private var _onValidationError: OnErrorCallback = null
    private var _onAny: OnAnyCallback<T> = null
    private lateinit var _fetchData: FetchSuspendData<T>

    protected suspend fun execute() {
        _onLoading?.invoke(true)
        _onAny?.invoke(ViewState.Loading())
        try {
            val data = _fetchData.invoke()
            _onSuccess?.invoke(data)
            _onAny?.invoke(ViewState.Success(data))
        } catch (e: Exception) {
            _onError?.invoke(e)
            _onAny?.invoke(ViewState.Error(e))
        }
    }

    protected fun fetchData(callback: FetchSuspendData<T>) {
        _fetchData = callback
    }

    fun onLoading(callback: OnLoadingCallback) {
        _onLoading = callback
    }

    fun onSuccess(callback: OnSuccessCallback<T>) {
        _onSuccess = callback
    }

    fun onError(callback: OnErrorCallback) {
        _onError = callback
    }

    fun onValidationError(callback: OnErrorCallback) {
        _onValidationError = callback
    }

    fun onAny(callback: OnAnyCallback<T>) {
        _onAny = callback
    }
}


fun <T : Any> ViewModel.fetchData(
    fetchData: FetchSuspendData<T>,
    scope: SuspendViewDataTemplateScope<T>,
) {
    object : SuspendViewStateDataTemplate<T>() {
        init {
            apply(scope)
            fetchData(fetchData)
            viewModelScope.launch(Dispatchers.IO) { execute() }
        }
    }
}
