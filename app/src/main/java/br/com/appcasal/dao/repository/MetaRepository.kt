package br.com.appcasal.dao.repository

import br.com.appcasal.dao.datasource.remote.MetaRemoteDataSource
import br.com.appcasal.dao.dto.network.response.MetaResponseDTO
import br.com.appcasal.domain.model.Meta

class MetaRepository(
    private val metaRemoteDataSource: MetaRemoteDataSource,
) {
    suspend fun recuperaMetas(): List<Meta> {
        val result = metaRemoteDataSource.recuperaMetas()
        return result.map { mapFrom(it) } // TODO fazer mapfrom
    }

    suspend fun recuperaMetasByConcluidas(concluidas: Boolean): List<Meta> {
        val result = metaRemoteDataSource.recuperaMetasByConcluidas(concluidas)
        return result.map { mapFrom(it) } // TODO fazer mapfrom
    }

    suspend fun insereMeta(meta: Meta): Meta {
        val result = metaRemoteDataSource.insereMeta(meta)
        return mapFrom(result)  // TODO fazer mapfrom
    }

    suspend fun alteraMeta(meta: Meta): Meta {
        val result = metaRemoteDataSource.alteraMeta(meta)
        return mapFrom(result)  // TODO fazer mapfrom
    }

    suspend fun deletaMeta(meta: Meta): Boolean {
        return metaRemoteDataSource.deletaMeta(meta)
    }

    fun mapFrom(metaResponseDTO: MetaResponseDTO) = Meta(id = metaResponseDTO.id!!,
        descricao = metaResponseDTO.descricao!!,
        concluido = metaResponseDTO.concluido!!
    ) // TODO colocar o map from dentro da classe meta



}
