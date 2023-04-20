package br.com.appcasal.viewmodel

import androidx.lifecycle.ViewModel
import br.com.appcasal.domain.model.Receita
import br.com.appcasal.domain.usecase.*
import br.com.appcasal.ui.ViewState
import br.com.appcasal.ui.fetchData
import kotlinx.coroutines.flow.*

class ListaReceitasViewModel(
    private val getReceitasUseCase: GetReceitasUseCase,
    private val deletaReceitaUseCase: DeletaReceitaUseCase
) : ViewModel() {
    var filterByDescricao: String? = null
        private set

    private lateinit var receita: Receita

    private val _receitaGetResult = MutableStateFlow<ViewState<List<Receita>>>(ViewState.Initial())
    val receitaGetResult: StateFlow<ViewState<List<Receita>>> get() = _receitaGetResult

    private val _receitaDeleteResult: MutableSharedFlow<ViewState<Boolean>> = MutableSharedFlow()
    val receitaDeleteResult: SharedFlow<ViewState<Boolean>> get() = _receitaDeleteResult

    fun recuperaReceitas(filterByDescricao: String? = null) {
        this.filterByDescricao = filterByDescricao

        fetchData(::recuperaReceitasUseCase) {
            onAny { viewState -> _receitaGetResult.update { viewState } }
        }
    }

    fun deletaReceita(receita: Receita) {
        this.receita = receita

        fetchData(::deletaReceitaUseCase) {
            onAny { viewState ->
                _receitaDeleteResult.emit(viewState)
            }
        }
    }

    private suspend fun recuperaReceitasUseCase() = getReceitasUseCase.runAsync(filterByDescricao)
    private suspend fun deletaReceitaUseCase() = deletaReceitaUseCase.runAsync(receita)

}
