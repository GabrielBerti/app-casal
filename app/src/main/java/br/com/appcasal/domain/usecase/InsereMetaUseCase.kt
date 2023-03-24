package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.MetaRepository
import br.com.appcasal.domain.model.Meta

class InsereMetaUseCase(
    private val metaRepository: MetaRepository
) {
    suspend fun runAsync(meta: Meta): Meta {
        return metaRepository.insereMeta(meta = meta)
    }
}
