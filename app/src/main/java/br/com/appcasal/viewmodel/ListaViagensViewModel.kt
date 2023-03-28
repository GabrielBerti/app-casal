package br.com.appcasal.viewmodel

import androidx.lifecycle.ViewModel
import br.com.appcasal.domain.model.Viagem
import br.com.appcasal.domain.usecase.*
import br.com.appcasal.ui.ViewState
import br.com.appcasal.ui.fetchData
import kotlinx.coroutines.flow.*

class ListaViagensViewModel(
    private val getViagensUseCase: GetViagensUseCase,
    private val insereViagemUseCase: InsereViagemUseCase,
    private val alteraViagemUseCase: AlteraViagemUseCase,
    private val deletaViagemUseCase: DeletaViagemUseCase
) : ViewModel() {

    private lateinit var viagem: Viagem

    private val _viagemGetResult = MutableStateFlow<ViewState<List<Viagem>>>(ViewState.Initial())
    val viagemGetResult: StateFlow<ViewState<List<Viagem>>> get() = _viagemGetResult

    private val _viagemInsertResult: MutableSharedFlow<ViewState<Viagem>> = MutableSharedFlow()
    val viagemInsertResult: SharedFlow<ViewState<Viagem>> get() = _viagemInsertResult

    private val _viagemUpdateResult: MutableSharedFlow<ViewState<Viagem>> = MutableSharedFlow()
    val viagemUpdateResult: SharedFlow<ViewState<Viagem>> get() = _viagemUpdateResult

    private val _viagemDeleteResult: MutableSharedFlow<ViewState<Boolean>> = MutableSharedFlow()
    val viagemDeleteResult: SharedFlow<ViewState<Boolean>> get() = _viagemDeleteResult

    fun recuperaViagens() = fetchData(::recuperaViagensUseCase) {
        onAny { viewState -> _viagemGetResult.update { viewState } }
    }

    fun insereViagem(viagem: Viagem) {
        this.viagem = viagem

        fetchData(::insereViagemUseCase) {
            onAny { viewState ->
                _viagemInsertResult.emit(viewState)
            }
        }
    }

    fun alteraViagem(viagem: Viagem) {
        this.viagem = viagem

        fetchData(::alteraViagemUseCase) {
            onAny { viewState ->
                _viagemUpdateResult.emit(viewState)
            }
        }
    }

    fun deletaViagem(viagem: Viagem) {
        this.viagem = viagem

        fetchData(::deletaViagemUseCase) {
            onAny { viewState ->
                _viagemDeleteResult.emit(viewState)
            }
        }
    }

    private suspend fun recuperaViagensUseCase() = getViagensUseCase.runAsync()

    private suspend fun insereViagemUseCase() = insereViagemUseCase.runAsync(viagem)

    private suspend fun alteraViagemUseCase() = alteraViagemUseCase.runAsync(viagem)

    private suspend fun deletaViagemUseCase() = deletaViagemUseCase.runAsync(viagem)

}
