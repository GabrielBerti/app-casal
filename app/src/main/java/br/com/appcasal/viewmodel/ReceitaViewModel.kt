package br.com.appcasal.viewmodel

import androidx.lifecycle.ViewModel
import br.com.appcasal.domain.model.Receita
import br.com.appcasal.domain.usecase.AlteraReceitaUseCase
import br.com.appcasal.domain.usecase.DeletaReceitaUseCase
import br.com.appcasal.domain.usecase.GetReceitasUseCase
import br.com.appcasal.domain.usecase.InsereReceitaUseCase
import br.com.appcasal.ui.ViewState
import br.com.appcasal.ui.fetchData
import kotlinx.coroutines.flow.*

class ReceitaViewModel(
    private val getReceitasUseCase: GetReceitasUseCase,
    private val insereReceitaUseCase: InsereReceitaUseCase,
    private val alteraReceitaUseCase: AlteraReceitaUseCase,
    private val deletaReceitaUseCase: DeletaReceitaUseCase
) : ViewModel() {

    private lateinit var receita: Receita

    private val _receitaGetResult = MutableStateFlow<ViewState<List<Receita>>>(ViewState.Initial())
    val receitaGetResult: StateFlow<ViewState<List<Receita>>> get() = _receitaGetResult

    private val _receitaInsertResult: MutableSharedFlow<ViewState<Receita>> = MutableSharedFlow()
    val receitaInsertResult: SharedFlow<ViewState<Receita>> get() = _receitaInsertResult

    private val _receitaUpdateResult: MutableSharedFlow<ViewState<Receita>> = MutableSharedFlow()
    val receitaUpdateResult: SharedFlow<ViewState<Receita>> get() = _receitaUpdateResult

    private val _receitaDeleteResult: MutableSharedFlow<ViewState<Boolean>> = MutableSharedFlow()
    val receitaDeleteResult: SharedFlow<ViewState<Boolean>> get() = _receitaDeleteResult

    fun recuperaReceitas() = fetchData(::recuperaReceitasUseCase) {
        onAny { viewState -> _receitaGetResult.update { viewState } }
    }

    fun insereReceita(receita: Receita) {
        this.receita = receita

        fetchData(::insereReceitaUseCase) {
            onAny { viewState ->
                _receitaInsertResult.emit(viewState)
            }
        }
    }

    fun alteraReceita(receita: Receita) {
        this.receita = receita

        fetchData(::alteraReceitaUseCase) {
            onAny { viewState ->
                _receitaUpdateResult.emit(viewState)
            }
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

    private suspend fun recuperaReceitasUseCase() = getReceitasUseCase.runAsync()

    private suspend fun insereReceitaUseCase() = insereReceitaUseCase.runAsync(receita)

    private suspend fun alteraReceitaUseCase() = alteraReceitaUseCase.runAsync(receita)

    private suspend fun deletaReceitaUseCase() = deletaReceitaUseCase.runAsync(receita)

}
