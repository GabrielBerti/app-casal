package br.com.appcasal.dao.datasource.remote

import br.com.appcasal.dao.datasource.CallableDataSource
import br.com.appcasal.dao.dto.network.request.MetaRequestDTO
import br.com.appcasal.dao.dto.network.response.MetaResponseDTO
import br.com.appcasal.dao.service.MetaService
import br.com.appcasal.domain.model.Meta

class MetaRemoteDataSource(
    private val metaService: MetaService
): CallableDataSource {

    suspend fun recuperaMetas(concluidas: Boolean?, search: String?): List<MetaResponseDTO> {
        val dto = metaService.recuperaMetas(concluidas, search)

        return dto.body() ?: emptyList()
    }

    suspend fun insereMeta(meta: Meta): MetaResponseDTO {
        val dto = metaService.insereMeta(MetaRequestDTO.mapFrom(meta))

        return dto.body() ?: MetaResponseDTO()
    }

    suspend fun alteraMeta(meta: Meta): MetaResponseDTO {
        val dto = metaService.alteraMeta(MetaRequestDTO.mapFrom(meta), meta.id)

        return dto.body() ?: MetaResponseDTO()
    }

    suspend fun deletaMeta(meta: Meta): Boolean {
        metaService.deletaMeta(meta.id)
        return true
    }
}
