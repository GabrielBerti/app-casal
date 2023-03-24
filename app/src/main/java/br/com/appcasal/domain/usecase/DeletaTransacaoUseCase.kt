package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.TransacaoRepository
import br.com.appcasal.domain.model.Transacao

class DeletaTransacaoUseCase(
    private val transacaoRepository: TransacaoRepository
) {
    suspend fun runAsync(transacao: Transacao): Boolean {
        return transacaoRepository.deletaTransacao(transacao)
    }
}
