package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.TransacaoRepository
import br.com.appcasal.domain.model.Resumo

class GetResumoUseCase(
    private val transacaoRepository: TransacaoRepository
) {
    suspend fun runAsync(): Resumo {
        return transacaoRepository.recuperaResumo()
    }
}
