package br.com.appcasal.dao.repository

import br.com.appcasal.dao.datasource.remote.LugarVisitadoRemoteDataSource
import br.com.appcasal.domain.model.LugarVisitado
import br.com.appcasal.domain.model.Viagem

class LugarVisitadoRepository(
    private val lugarVisitadoRemoteDataSource: LugarVisitadoRemoteDataSource
) {

    suspend fun recuperaLugaresVisitadosByViagemId(viagem: Viagem): List<LugarVisitado> {
        val result = lugarVisitadoRemoteDataSource.recuperaLugaresVisitadosByViagemId(viagem)
        return result.map { LugarVisitado.mapFrom(it)}
    }

    suspend fun insereLugarVisitado(lugarVisitado: LugarVisitado, viagem: Viagem): LugarVisitado {
        val result = lugarVisitadoRemoteDataSource.insereLugarVisitado(lugarVisitado, viagem)
        return LugarVisitado.mapFrom(result)
    }

    suspend fun alteraLugarVisitado(lugarVisitado: LugarVisitado, viagem: Viagem): LugarVisitado {
        val result = lugarVisitadoRemoteDataSource.alteraLugarVisitado(lugarVisitado, viagem)
        return LugarVisitado.mapFrom(result)
    }

    suspend fun deletaLugarVisitado(lugarVisitado: LugarVisitado): Boolean {
        return lugarVisitadoRemoteDataSource.deletaLugarVisitado(lugarVisitado)
    }

}
