package br.com.appcasal.dao.repository

import br.com.appcasal.dao.datasource.remote.ViagemRemoteDataSource
import br.com.appcasal.domain.model.Viagem

class ViagemRepository(
    private val viagemRemoteDataSource: ViagemRemoteDataSource
) {
    suspend fun recuperaViagens(search: String?): List<Viagem> {
        val result = viagemRemoteDataSource.recuperaViagens(search)
        return result.map { Viagem.mapFrom(it) }
    }

    suspend fun insereViagem(viagem: Viagem): Viagem {
        val result = viagemRemoteDataSource.insereViagem(viagem)
        return Viagem.mapFrom(result)
    }

    suspend fun alteraViagem(viagem: Viagem): Viagem {
        val result = viagemRemoteDataSource.alteraViagem(viagem)
        return Viagem.mapFrom(result)
    }

    suspend fun deletaViagem(viagem: Viagem): Boolean {
        return viagemRemoteDataSource.deletaViagem(viagem)
    }

}
