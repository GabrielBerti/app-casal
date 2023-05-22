package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.TransacaoRepository
import br.com.appcasal.domain.model.Transacao

class AlteraTransacaoUseCase(
    private val transacaoRepository: TransacaoRepository
) {
    suspend fun runAsync(transacao: Transacao): Transacao {
        return transacaoRepository.alteraTransacao(transacao)
    }
}
