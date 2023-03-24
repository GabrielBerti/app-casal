package br.com.appcasal.dao.datasource.remote

import br.com.appcasal.dao.datasource.CallableDataSource
import br.com.appcasal.dao.dto.network.request.MetaRequestDTO
import br.com.appcasal.dao.dto.network.response.MetaResponseDTO
import br.com.appcasal.dao.service.MetaService
import br.com.appcasal.domain.model.Meta

class MetaRemoteDataSource(
    private val metaService: MetaService
): CallableDataSource {

    suspend fun recuperaMetas(): List<MetaResponseDTO> {
        val dto = metaService.recuperaMetas()

        return dto.body() ?: emptyList()
    }

    suspend fun recuperaMetasByConcluidas(concluidas: Boolean): List<MetaResponseDTO> {
        val dto = metaService.recuperaMetasByConcluidas(concluidas)

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
        val x = metaService.deletaMeta(meta.id)


        return true
    }
}
