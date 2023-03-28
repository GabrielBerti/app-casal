package br.com.appcasal.viewmodel

import androidx.lifecycle.ViewModel
import br.com.appcasal.domain.model.GastoViagem
import br.com.appcasal.domain.model.Viagem
import br.com.appcasal.domain.usecase.*
import br.com.appcasal.ui.ViewState
import br.com.appcasal.ui.fetchData
import kotlinx.coroutines.flow.*

class GastosViagemViewModel(
    private val getGastoViagemByViagemUseCase: GetGastoViagemByViagemUseCase,
    private val insereGastoViagemUseCase: InsereGastoViagemUseCase,
    private val alteraGastoViagemUseCase: AlteraGastoViagemUseCase,
    private val deletaGastoViagemUseCase: DeletaGastoViagemUseCase
) : ViewModel() {

    private lateinit var gastoViagem: GastoViagem
    private lateinit var viagem: Viagem

    private val _gastoViagemGetResult = MutableStateFlow<ViewState<List<GastoViagem>>>(ViewState.Initial())
    val gastoViagemGetResult: StateFlow<ViewState<List<GastoViagem>>> get() = _gastoViagemGetResult

    private val _gastoViagemInsertResult: MutableSharedFlow<ViewState<GastoViagem>> = MutableSharedFlow()
    val gastoViagemInsertResult: SharedFlow<ViewState<GastoViagem>> get() = _gastoViagemInsertResult

    private val _gastoViagemUpdateResult: MutableSharedFlow<ViewState<GastoViagem>> = MutableSharedFlow()
    val gastoViagemUpdateResult: SharedFlow<ViewState<GastoViagem>> get() = _gastoViagemUpdateResult

    private val _gastoViagemDeleteResult: MutableSharedFlow<ViewState<Boolean>> = MutableSharedFlow()
    val gastoViagemDeleteResult: SharedFlow<ViewState<Boolean>> get() = _gastoViagemDeleteResult

    fun recuperaGastosViagem(viagem: Viagem) {
        this.viagem = viagem

        fetchData(::recuperaGastosViagemByViagemIdUseCase) {
            onAny { viewState ->
                _gastoViagemGetResult.emit(viewState)
            }
        }
    }

    fun insereGastoViagem(gastoViagem: GastoViagem, viagem: Viagem) {
        this.gastoViagem = gastoViagem
        this.viagem = viagem

        fetchData(::insereGastoViagemUseCase) {
            onAny { viewState ->
                _gastoViagemInsertResult.emit(viewState)
            }
        }
    }

    fun alteraGastoViagem(gastoViagem: GastoViagem, viagem: Viagem) {
        this.gastoViagem = gastoViagem
        this.viagem = viagem

        fetchData(::alteraGastoViagemUseCase) {
            onAny { viewState ->
                _gastoViagemUpdateResult.emit(viewState)
            }
        }
    }

    fun deletaGastoViagem(gastoViagem: GastoViagem) {
        this.gastoViagem = gastoViagem

        fetchData(::deletaGastoViagemUseCase) {
            onAny { viewState ->
                _gastoViagemDeleteResult.emit(viewState)
            }
        }
    }

    private suspend fun recuperaGastosViagemByViagemIdUseCase() = getGastoViagemByViagemUseCase.runAsync(viagem)

    private suspend fun insereGastoViagemUseCase() = insereGastoViagemUseCase.runAsync(gastoViagem, viagem)

    private suspend fun alteraGastoViagemUseCase() = alteraGastoViagemUseCase.runAsync(gastoViagem, viagem)

    private suspend fun deletaGastoViagemUseCase() = deletaGastoViagemUseCase.runAsync(gastoViagem)
}
