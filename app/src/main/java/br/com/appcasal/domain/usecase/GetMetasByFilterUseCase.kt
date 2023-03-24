package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.MetaRepository
import br.com.appcasal.domain.model.Meta

class GetMetasByFilterUseCase(
    private val metaRepository: MetaRepository
) {
    suspend fun runAsync(concluidas: Boolean): List<Meta> {
        return metaRepository.recuperaMetasByConcluidas(concluidas)
    }
}
