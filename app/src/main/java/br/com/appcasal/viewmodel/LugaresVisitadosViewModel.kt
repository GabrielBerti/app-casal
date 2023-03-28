package br.com.appcasal.viewmodel

import androidx.lifecycle.ViewModel
import br.com.appcasal.domain.model.LugarVisitado
import br.com.appcasal.domain.model.Viagem
import br.com.appcasal.domain.usecase.*
import br.com.appcasal.ui.ViewState
import br.com.appcasal.ui.fetchData
import kotlinx.coroutines.flow.*

class LugaresVisitadosViewModel(
    private val getLugarVisitadoByViagemUseCase: GetLugarVisitadoByViagemUseCase,
    private val insereLugarVisitadoUseCase: InsereLugarVisitadoUseCase,
    private val alteraLugarVisitadoUseCase: AlteraLugarVisitadoUseCase,
    private val deletaLugarVisitadoUseCase: DeletaLugarVisitadoUseCase
) : ViewModel() {

    private lateinit var lugarVisitado: LugarVisitado
    private lateinit var viagem: Viagem

    private val _lugarVisitadoGetResult: MutableSharedFlow<ViewState<List<LugarVisitado>>> = MutableSharedFlow()
    val lugarVisitadoGetResult: SharedFlow<ViewState<List<LugarVisitado>>> get() = _lugarVisitadoGetResult

    private val _lugarVisitadoInsertResult: MutableSharedFlow<ViewState<LugarVisitado>> = MutableSharedFlow()
    val lugarVisitadoInsertResult: SharedFlow<ViewState<LugarVisitado>> get() = _lugarVisitadoInsertResult

    private val _lugarVisitadoUpdateResult: MutableSharedFlow<ViewState<LugarVisitado>> = MutableSharedFlow()
    val lugarVisitadoUpdateResult: SharedFlow<ViewState<LugarVisitado>> get() = _lugarVisitadoUpdateResult

    private val _lugarVisitadoDeleteResult: MutableSharedFlow<ViewState<Boolean>> = MutableSharedFlow()
    val lugarVisitadoDeleteResult: SharedFlow<ViewState<Boolean>> get() = _lugarVisitadoDeleteResult

    fun recuperaLugaresVisitados(viagem: Viagem) {
        this.viagem = viagem

        fetchData(::recuperaLugaresVisitadosByViagemIdUseCase) {
            onAny { viewState ->
                _lugarVisitadoGetResult.emit(viewState)
            }
        }
    }

    fun insereLugarVisitado(lugarVisitado: LugarVisitado, viagem: Viagem) {
        this.lugarVisitado = lugarVisitado
        this.viagem = viagem

        fetchData(::insereLugarVisitadoUseCase) {
            onAny { viewState ->
                _lugarVisitadoInsertResult.emit(viewState)
            }
        }
    }

    fun alteraLugarVisitado(lugarVisitado: LugarVisitado, viagem: Viagem) {
        this.lugarVisitado = lugarVisitado
        this.viagem = viagem

        fetchData(::alteraLugarVisitadoUseCase) {
            onAny { viewState ->
                _lugarVisitadoUpdateResult.emit(viewState)
            }
        }
    }

    fun deletaLugarVisitado(lugarVisitado: LugarVisitado) {
        this.lugarVisitado = lugarVisitado

        fetchData(::deletaLugarVisitadoUseCase) {
            onAny { viewState ->
                _lugarVisitadoDeleteResult.emit(viewState)
            }
        }
    }

    private suspend fun recuperaLugaresVisitadosByViagemIdUseCase() = getLugarVisitadoByViagemUseCase.runAsync(viagem)

    private suspend fun insereLugarVisitadoUseCase() = insereLugarVisitadoUseCase.runAsync(lugarVisitado, viagem)

    private suspend fun alteraLugarVisitadoUseCase() = alteraLugarVisitadoUseCase.runAsync(lugarVisitado, viagem)

    private suspend fun deletaLugarVisitadoUseCase() = deletaLugarVisitadoUseCase.runAsync(lugarVisitado)
}
