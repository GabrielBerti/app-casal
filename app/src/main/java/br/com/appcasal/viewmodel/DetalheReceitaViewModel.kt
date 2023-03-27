package br.com.appcasal.viewmodel

import androidx.lifecycle.ViewModel
import br.com.appcasal.domain.model.Ingrediente
import br.com.appcasal.domain.model.Receita
import br.com.appcasal.domain.usecase.*
import br.com.appcasal.ui.ViewState
import br.com.appcasal.ui.fetchData
import kotlinx.coroutines.flow.*

class DetalheReceitaViewModel(
    private val marcarDesmarcarIngredienteUseCase: MarcarDesmarcarIngredienteUseCase,
    private val desmarcarTodosIngredientesUseCase: DesmarcarTodosIngredientesUseCase
) : ViewModel() {

    private lateinit var ingredientes: List<Ingrediente>
    private lateinit var ingrediente: Ingrediente
    private lateinit var receita: Receita

    private val _ingredienteMarcouResult: MutableSharedFlow<ViewState<Boolean>> = MutableSharedFlow()
    val ingredienteMarcouResult: SharedFlow<ViewState<Boolean>> get() = _ingredienteMarcouResult

    private val _ingredienteDesmarcarTodasResult: MutableSharedFlow<ViewState<Boolean>> = MutableSharedFlow()
    val ingredienteDesmarcarTodasResult: SharedFlow<ViewState<Boolean>> get() = _ingredienteDesmarcarTodasResult

    fun marcarDesmarcarIngrediente(ingrediente: Ingrediente) {
        this.ingrediente = ingrediente

        fetchData(::marcarDesmarcarIngredienteUseCase) {
            onAny { viewState ->
                _ingredienteMarcouResult.emit(viewState)
            }
        }
    }

    fun desmarcarTodosIngredientes(receita: Receita) {
        this.receita = receita

        fetchData(::desmarcarTodosIngredientesUseCase) {
            onAny { viewState ->
                _ingredienteDesmarcarTodasResult.emit(viewState)
            }
        }
    }

    private suspend fun marcarDesmarcarIngredienteUseCase() = marcarDesmarcarIngredienteUseCase.runAsync(ingrediente.id, ingrediente.marcado)

    private suspend fun desmarcarTodosIngredientesUseCase() = desmarcarTodosIngredientesUseCase.runAsync(receita.id)

}
