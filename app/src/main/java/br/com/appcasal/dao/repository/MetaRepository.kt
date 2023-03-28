package br.com.appcasal.dao.repository

import br.com.appcasal.dao.datasource.remote.MetaRemoteDataSource
import br.com.appcasal.domain.model.Meta

class MetaRepository(
    private val metaRemoteDataSource: MetaRemoteDataSource,
) {
    suspend fun recuperaMetas(): List<Meta> {
        val result = metaRemoteDataSource.recuperaMetas()
        return result.map { Meta.mapFrom(it) }
    }

    suspend fun recuperaMetasByConcluidas(concluidas: Boolean): List<Meta> {
        val result = metaRemoteDataSource.recuperaMetasByConcluidas(concluidas)
        return result.map { Meta.mapFrom(it) }
    }

    suspend fun insereMeta(meta: Meta): Meta {
        val result = metaRemoteDataSource.insereMeta(meta)
        return Meta.mapFrom(result)
    }

    suspend fun alteraMeta(meta: Meta): Meta {
        val result = metaRemoteDataSource.alteraMeta(meta)
        return Meta.mapFrom(result)
    }

    suspend fun deletaMeta(meta: Meta): Boolean {
        return metaRemoteDataSource.deletaMeta(meta)
    }
}
