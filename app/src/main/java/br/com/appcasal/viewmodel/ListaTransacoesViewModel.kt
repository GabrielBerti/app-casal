package br.com.appcasal.viewmodel

import androidx.lifecycle.ViewModel
import br.com.appcasal.domain.model.Resumo
import br.com.appcasal.domain.model.Transacao
import br.com.appcasal.domain.usecase.*
import br.com.appcasal.ui.ViewState
import br.com.appcasal.ui.fetchData
import kotlinx.coroutines.flow.*

class ListaTransacoesViewModel(
    private val getTransacoesUseCase: GetTransacoesUseCase,
    private val insereTransacaoUseCase: InsereTransacaoUseCase,
    private val alteraTransacaoUseCase: AlteraTransacaoUseCase,
    private val deletaTransacaoUseCase: DeletaTransacaoUseCase,
    private val deletaTodasTransacoesUseCase: DeletaTodasTransacoesUseCase,
    private val getResumoUseCase: GetResumoUseCase
) : ViewModel() {

    private lateinit var transacao: Transacao

    private val _transacaoGetResult = MutableStateFlow<ViewState<List<Transacao>>>(ViewState.Initial())
    val transacaoGetResult: StateFlow<ViewState<List<Transacao>>> get() = _transacaoGetResult

    private val _resumoGetResult = MutableStateFlow<ViewState<Resumo>>(ViewState.Initial())
    val resumoGetResult: StateFlow<ViewState<Resumo>> get() = _resumoGetResult

    private val _transacaoInsertResult: MutableSharedFlow<ViewState<Transacao>> = MutableSharedFlow()
    val transacaoInsertResult: SharedFlow<ViewState<Transacao>> get() = _transacaoInsertResult

    private val _transacaoUpdateResult: MutableSharedFlow<ViewState<Transacao>> = MutableSharedFlow()
    val transacaoUpdateResult: SharedFlow<ViewState<Transacao>> get() = _transacaoUpdateResult

    private val _transacaoDeleteResult: MutableSharedFlow<ViewState<Boolean>> = MutableSharedFlow()
    val transacaoDeleteResult: SharedFlow<ViewState<Boolean>> get() = _transacaoDeleteResult

    private val _transacaoDeleteAllResult: MutableSharedFlow<ViewState<Boolean>> = MutableSharedFlow()
    val transacaoDeleteAllResult: SharedFlow<ViewState<Boolean>> get() = _transacaoDeleteAllResult

    fun recuperaTransacoes() = fetchData(::recuperaTransacoesUseCase) {
        onAny { viewState -> _transacaoGetResult.update { viewState } }
    }

    fun recuperaResumo() = fetchData(::recuperaResumoUseCase) {
        onAny { viewState -> _resumoGetResult.update { viewState } }
    }

    fun insereTransacao(transacao: Transacao) {
        this.transacao = transacao

        fetchData(::insereTransacaoUseCase) {
            onAny { viewState ->
                _transacaoInsertResult.emit(viewState)
            }
        }
    }

    fun alteraTransacao(transacao: Transacao) {
        this.transacao = transacao

        fetchData(::alteraTransacaoUseCase) {
            onAny { viewState ->
                _transacaoUpdateResult.emit(viewState)
            }
        }
    }

    fun deletaTransacao(transacao: Transacao) {
        this.transacao = transacao

        fetchData(::deletaTransacaoUseCase) {
            onAny { viewState ->
                _transacaoDeleteResult.emit(viewState)
            }
        }
    }

    fun deletaTodasTransacoes() {

        fetchData(::deletaTodasTransacoesUseCase) {
            onAny { viewState ->
                _transacaoDeleteAllResult.emit(viewState)
            }
        }
    }

    private suspend fun recuperaTransacoesUseCase() = getTransacoesUseCase.runAsync()

    private suspend fun insereTransacaoUseCase() = insereTransacaoUseCase.runAsync(transacao)

    private suspend fun alteraTransacaoUseCase() = alteraTransacaoUseCase.runAsync(transacao)

    private suspend fun deletaTransacaoUseCase() = deletaTransacaoUseCase.runAsync(transacao)

    private suspend fun deletaTodasTransacoesUseCase() = deletaTodasTransacoesUseCase.runAsync()

    private suspend fun recuperaResumoUseCase() = getResumoUseCase.runAsync()

}
