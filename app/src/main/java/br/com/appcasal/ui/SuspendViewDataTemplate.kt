package br.com.appcasal.ui

abstract class SuspendViewDataTemplate<T : Any> {
    suspend fun execute() {
        onLoading(ViewState.Loading())
        onAny(ViewState.Loading())
        try {
            val data = fetchData()
            onSuccess(ViewState.Success(data))
            onAny(ViewState.Success(data))
        } catch (e: Exception) {
            onError(ViewState.Error(e))
            onAny(ViewState.Error(e))
        }
    }

    open fun onLoading(data: ViewState.Loading<T>) {}
    open fun onAny(data: ViewState<T>) {}
    open fun onError(data: ViewState.Error<T>) {}
    open fun onSuccess(data: ViewState.Success<T>) {}

    abstract suspend fun fetchData(): T
}
