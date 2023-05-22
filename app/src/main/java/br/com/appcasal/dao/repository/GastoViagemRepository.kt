package br.com.appcasal.dao.repository

import br.com.appcasal.dao.datasource.remote.GastoViagemRemoteDataSource
import br.com.appcasal.domain.model.GastoViagem
import br.com.appcasal.domain.model.Viagem

class GastoViagemRepository(
    private val gastoViagemRemoteDataSource: GastoViagemRemoteDataSource
) {

    suspend fun recuperaGastosViagemByViagemId(viagem: Viagem): List<GastoViagem> {
        val result = gastoViagemRemoteDataSource.recuperaGastosViagemByViagemId(viagem)
        return result.map { GastoViagem.mapFrom(it)}
    }

    suspend fun insereGastosViagem(gastoViagem: GastoViagem, viagem: Viagem): GastoViagem {
        val result = gastoViagemRemoteDataSource.insereGastosViagem(gastoViagem, viagem)
        return GastoViagem.mapFrom(result)
    }

    suspend fun alteraGastosViagem(gastoViagem: GastoViagem, viagem: Viagem): GastoViagem {
        val result = gastoViagemRemoteDataSource.alteraGastosViagem(gastoViagem, viagem)
        return GastoViagem.mapFrom(result)
    }

    suspend fun deletaGastosViagem(gastoViagem: GastoViagem): Boolean {
        return gastoViagemRemoteDataSource.deletaGastosViagem(gastoViagem)
    }

}
