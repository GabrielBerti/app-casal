package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.MetaRepository
import br.com.appcasal.domain.model.Meta

class GetMetasUseCase(
    private val metaRepository: MetaRepository
) {
    suspend fun runAsync(concluidas: Boolean?, search: String?): List<Meta> {
        return metaRepository.recuperaMetas(concluidas, search)
    }
}
