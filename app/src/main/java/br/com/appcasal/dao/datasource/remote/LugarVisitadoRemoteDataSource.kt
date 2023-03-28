package br.com.appcasal.dao.datasource.remote

import br.com.appcasal.dao.datasource.CallableDataSource
import br.com.appcasal.dao.dto.network.request.LugarVisitadoRequestDTO
import br.com.appcasal.dao.dto.network.response.LugarVisitadoResponseDTO
import br.com.appcasal.dao.service.LugarVisitadoService
import br.com.appcasal.domain.model.LugarVisitado
import br.com.appcasal.domain.model.Viagem

class LugarVisitadoRemoteDataSource(
    private val lugarVisitadoService: LugarVisitadoService
): CallableDataSource {

    suspend fun recuperaLugaresVisitadosByViagemId(viagem: Viagem): List<LugarVisitadoResponseDTO> {
        val dto = lugarVisitadoService.recuperaLugaresVisitadosByViagemId(viagem.id)
        return dto.body() ?: emptyList()
    }

    suspend fun insereLugarVisitado(lugarVisitado: LugarVisitado, viagem: Viagem): LugarVisitadoResponseDTO {
        val dto = lugarVisitadoService.insereLugarVisitado(LugarVisitadoRequestDTO.mapFrom(lugarVisitado, viagem))
        return dto.body() ?: LugarVisitadoResponseDTO()
    }

    suspend fun alteraLugarVisitado(lugarVisitado: LugarVisitado, viagem: Viagem): LugarVisitadoResponseDTO {
        val dto = lugarVisitadoService.alteraLugarVisitado(LugarVisitadoRequestDTO.mapFrom(lugarVisitado, viagem), lugarVisitado.id)
        return dto.body() ?: LugarVisitadoResponseDTO()
    }

    suspend fun deletaLugarVisitado(lugarVisitado: LugarVisitado): Boolean {
        lugarVisitadoService.deletaLugarVisitado(lugarVisitado.id)
        return true
    }
}
