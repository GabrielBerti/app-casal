package br.com.appcasal.dao.datasource.remote

import br.com.appcasal.dao.datasource.CallableDataSource
import br.com.appcasal.dao.dto.network.request.GastoViagemRequestDTO
import br.com.appcasal.dao.dto.network.response.GastoViagemResponseDTO
import br.com.appcasal.dao.service.GastoViagemService
import br.com.appcasal.domain.model.GastoViagem
import br.com.appcasal.domain.model.Viagem

class GastoViagemRemoteDataSource(
    private val gastoViagemService: GastoViagemService
): CallableDataSource {

    suspend fun recuperaGastosViagemByViagemId(viagem: Viagem): List<GastoViagemResponseDTO> {
        val dto = gastoViagemService.recuperaGastosViagemByViagemId(viagem.id)
        return dto.body() ?: emptyList()
    }

    suspend fun insereGastosViagem(gastoViagem: GastoViagem, viagem: Viagem): GastoViagemResponseDTO {
        val dto = gastoViagemService.insereGastosViagem(GastoViagemRequestDTO.mapFrom(gastoViagem, viagem))
        return dto.body() ?: GastoViagemResponseDTO()
    }

    suspend fun alteraGastosViagem(gastoViagem: GastoViagem, viagem: Viagem): GastoViagemResponseDTO {
        val dto = gastoViagemService.alteraGastosViagem(GastoViagemRequestDTO.mapFrom(gastoViagem, viagem), gastoViagem.id)
        return dto.body() ?: GastoViagemResponseDTO()
    }

    suspend fun deletaGastosViagem(gastoViagem: GastoViagem): Boolean {
        gastoViagemService.deletaGastosViagem(gastoViagem.id)
        return true
    }
}
