package br.com.appcasal.viewmodel

import androidx.lifecycle.ViewModel
import br.com.appcasal.domain.model.Meta
import br.com.appcasal.domain.usecase.*
import br.com.appcasal.ui.ViewState
import br.com.appcasal.ui.fetchData
import kotlinx.coroutines.flow.*

class MetasViewModel(
    private val getMetasUseCase: GetMetasUseCase,
    private val insereMetaUseCase: InsereMetaUseCase,
    private val alteraMetaUseCase: AlteraMetaUseCase,
    private val deletaMetaUseCase: DeletaMetaUseCase
) : ViewModel() {

    private lateinit var meta: Meta
    var filterByConcluidas: Boolean? = null
        private set

    var filterByName: String? = null
        private set

    private val _metaGetResult = MutableStateFlow<ViewState<List<Meta>>>(ViewState.Initial())
    val metaGetResult: StateFlow<ViewState<List<Meta>>> get() = _metaGetResult

    private val _metaInsertResult: MutableSharedFlow<ViewState<Meta>> = MutableSharedFlow()
    val metaInsertResult: SharedFlow<ViewState<Meta>> get() = _metaInsertResult

    private val _metaUpdateResult: MutableSharedFlow<ViewState<Meta>> = MutableSharedFlow()
    val metaUpdateResult: SharedFlow<ViewState<Meta>> get() = _metaUpdateResult

    private val _metaDeleteResult: MutableSharedFlow<ViewState<Boolean>> = MutableSharedFlow()
    val metaDeleteResult: SharedFlow<ViewState<Boolean>> get() = _metaDeleteResult

    fun recuperaMetas(filterByConcluidas: Boolean? = null, filterByName: String? = null) {
        this.filterByConcluidas = filterByConcluidas
        this.filterByName = filterByName

        fetchData(::recuperaMetasUseCase) {
            onAny { viewState -> _metaGetResult.update { viewState } }
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

    private suspend fun recuperaMetasUseCase() = getMetasUseCase.runAsync(filterByConcluidas, filterByName)

    private suspend fun insereMetaUseCase() = insereMetaUseCase.runAsync(meta)

    private suspend fun alteraMetaUseCase() = alteraMetaUseCase.runAsync(meta)

    private suspend fun deletaMetaUseCase() = deletaMetaUseCase.runAsync(meta)

}
