package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.TransacaoRepository
import br.com.appcasal.domain.model.Transacao

class GetTransacoesUseCase(
    private val transacaoRepository: TransacaoRepository
) {
    suspend fun runAsync(): List<Transacao> {
        return transacaoRepository.recuperaTransacoes()
    }
}
