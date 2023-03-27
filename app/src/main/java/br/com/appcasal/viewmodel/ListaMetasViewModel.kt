package br.com.appcasal.viewmodel

import androidx.lifecycle.ViewModel
import br.com.appcasal.domain.model.Meta
import br.com.appcasal.domain.usecase.*
import br.com.appcasal.ui.ViewState
import br.com.appcasal.ui.fetchData
import kotlinx.coroutines.flow.*

class ListaMetasViewModel(
    private val getMetasUseCase: GetMetasUseCase,
    private val getMetasByFilterUseCase: GetMetasByFilterUseCase,
    private val insereMetaUseCase: InsereMetaUseCase,
    private val alteraMetaUseCase: AlteraMetaUseCase,
    private val deletaMetaUseCase: DeletaMetaUseCase
) : ViewModel() {

    private lateinit var meta: Meta
    private var filterByConcluidas: Boolean = true

    private val _metaGetResult = MutableStateFlow<ViewState<List<Meta>>>(ViewState.Initial())
    val metaGetResult: StateFlow<ViewState<List<Meta>>> get() = _metaGetResult

    private val _metaInsertResult: MutableSharedFlow<ViewState<Meta>> = MutableSharedFlow()
    val metaInsertResult: SharedFlow<ViewState<Meta>> get() = _metaInsertResult

    private val _metaUpdateResult: MutableSharedFlow<ViewState<Meta>> = MutableSharedFlow()
    val metaUpdateResult: SharedFlow<ViewState<Meta>> get() = _metaUpdateResult

    private val _metaDeleteResult: MutableSharedFlow<ViewState<Boolean>> = MutableSharedFlow()
    val metaDeleteResult: SharedFlow<ViewState<Boolean>> get() = _metaDeleteResult

    fun recuperaMetas() = fetchData(::recuperaMetasUseCase) {
        onAny { viewState -> _metaGetResult.update { viewState } }
    }

    fun recuperaMetasByFilter(filterByConcluidas: Boolean) {
        this.filterByConcluidas = filterByConcluidas

        fetchData(::recuperaMetasByFilter) {
            onAny { viewState ->
                _metaGetResult.emit(viewState)
            }
        }
    }

    fun insereMeta(meta: Meta) {
        this.meta = meta

        fetchData(::insereMetaUseCase) {
            onAny { viewState ->
                _metaInsertResult.emit(viewState)
            }
        }
    }

    fun alteraMeta(meta: Meta) {
        this.meta = meta

        fetchData(::alteraMetaUseCase) {
            onAny { viewState ->
                _metaUpdateResult.emit(viewState)
            }
        }
    }

    fun deletaMeta(meta: Meta) {
        this.meta = meta

        fetchData(::deletaMetaUseCase) {
            onAny { viewState ->
                _metaDeleteResult.emit(viewState)
            }
        }
    }

    private suspend fun recuperaMetasUseCase() = getMetasUseCase.runAsync()

    private suspend fun recuperaMetasByFilter() = getMetasByFilterUseCase.runAsync(filterByConcluidas)

    private suspend fun insereMetaUseCase() = insereMetaUseCase.runAsync(meta)

    private suspend fun alteraMetaUseCase() = alteraMetaUseCase.runAsync(meta)

    private suspend fun deletaMetaUseCase() = deletaMetaUseCase.runAsync(meta)

}
