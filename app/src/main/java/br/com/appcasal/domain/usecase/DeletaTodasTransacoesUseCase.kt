package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.TransacaoRepository

class DeletaTodasTransacoesUseCase(
    private val transacaoRepository: TransacaoRepository
) {
    suspend fun runAsync(): Boolean {
        return transacaoRepository.deletaTodasTransacoes()
    }
}
