package br.com.appcasal.viewmodel

import androidx.lifecycle.ViewModel
import br.com.appcasal.domain.model.Ingrediente
import br.com.appcasal.domain.model.Receita
import br.com.appcasal.domain.usecase.*
import br.com.appcasal.ui.ViewState
import br.com.appcasal.ui.fetchData
import kotlinx.coroutines.flow.*

class IngredienteViewModel(
    private val insereIngredienteUseCase: InsereIngredienteUseCase,
    private val alteraIngredienteUseCase: AlteraIngredienteUseCase,
    private val deletaIngredienteUseCase: DeletaIngredienteUseCase,
    private val recuperaIngredienteByReceitaUseCase: RecuperaIngredienteByReceitaUseCase
) : ViewModel() {

    private lateinit var ingredientes: List<Ingrediente>
    private lateinit var ingrediente: Ingrediente
    private lateinit var receita: Receita

    private val _ingredienteInsertResult: MutableSharedFlow<ViewState<Ingrediente>> = MutableSharedFlow()
    val ingredienteInsertResult: SharedFlow<ViewState<Ingrediente>> get() = _ingredienteInsertResult

    private val _ingredienteUpdateResult: MutableSharedFlow<ViewState<Ingrediente>> = MutableSharedFlow()
    val ingredienteUpdateResult: SharedFlow<ViewState<Ingrediente>> get() = _ingredienteUpdateResult

    private val _ingredienteDeleteResult: MutableSharedFlow<ViewState<Boolean>> = MutableSharedFlow()
    val ingredienteDeleteResult: SharedFlow<ViewState<Boolean>> get() = _ingredienteDeleteResult

    private val _ingredienteGetResult: MutableSharedFlow<ViewState<List<Ingrediente>>> = MutableSharedFlow()
    val ingredienteGetResult: SharedFlow<ViewState<List<Ingrediente>>> get() = _ingredienteGetResult

    fun insereIngrediente(ingredientes: List<Ingrediente>, receita: Receita) {
        this.ingredientes = ingredientes
        this.receita = receita

        fetchData(::insereIngredienteUseCase) {
            onAny { viewState ->
                _ingredienteInsertResult.emit(viewState)
            }
        }
    }

    fun alteraIngrediente(ingrediente: Ingrediente, receita: Receita) {
        this.ingrediente = ingrediente
        this.receita = receita

        fetchData(::alteraIngredienteUseCase) {
            onAny { viewState ->
                _ingredienteUpdateResult.emit(viewState)
            }
        }
    }

    fun deletaIngrediente(ingrediente: Ingrediente) {
        this.ingrediente = ingrediente

        fetchData(::deletaIngredienteUseCase) {
            onAny { viewState ->
                _ingredienteDeleteResult.emit(viewState)
            }
        }
    }

    fun recuperaIngredientes(receita: Receita) {
        this.receita = receita

        fetchData(::recuperaIngredientesByReceitaId) {
            onAny { viewState ->
                _ingredienteGetResult.emit(viewState)
            }
        }
    }

    private suspend fun insereIngredienteUseCase() = insereIngredienteUseCase.runAsync(ingredientes, receita)

    private suspend fun alteraIngredienteUseCase() = alteraIngredienteUseCase.runAsync(ingrediente, receita)

    private suspend fun deletaIngredienteUseCase() = deletaIngredienteUseCase.runAsync(ingrediente)

    private suspend fun recuperaIngredientesByReceitaId() = recuperaIngredienteByReceitaUseCase.runAsync(receita)

}
