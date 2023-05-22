package br.com.appcasal.ui

sealed class ViewState<T> {
    class Initial<T> : ViewState<T>()
    class Loading<T> : ViewState<T>()
    class Error<T>(val error: Throwable) : ViewState<T>()
    class Success<T>(val data: T) : ViewState<T>()
}
