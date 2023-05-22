package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.MetaRepository
import br.com.appcasal.domain.model.Meta

class DeletaMetaUseCase(
    private val metaRepository: MetaRepository
) {
    suspend fun runAsync(meta: Meta): Boolean {
        return metaRepository.deletaMeta(meta = meta)
    }
}
