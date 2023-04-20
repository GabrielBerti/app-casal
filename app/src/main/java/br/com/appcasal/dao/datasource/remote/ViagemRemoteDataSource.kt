package br.com.appcasal.dao.datasource.remote

import br.com.appcasal.dao.datasource.CallableDataSource
import br.com.appcasal.dao.dto.network.request.ViagemRequestDTO
import br.com.appcasal.dao.dto.network.response.ViagemResponseDTO
import br.com.appcasal.dao.service.ViagemService
import br.com.appcasal.domain.model.Viagem

class ViagemRemoteDataSource(
    private val viagemService: ViagemService
): CallableDataSource {

    suspend fun recuperaViagens(search: String?): List<ViagemResponseDTO> {
        val dto = viagemService.recuperaViagens(search)

        return dto.body() ?: emptyList()
    }

    suspend fun insereViagem(viagem: Viagem): ViagemResponseDTO {
        val dto = viagemService.insereViagem(ViagemRequestDTO.mapFrom(viagem))

        return dto.body() ?: ViagemResponseDTO()
    }

    suspend fun alteraViagem(viagem: Viagem): ViagemResponseDTO {
        val dto = viagemService.alteraViagem(ViagemRequestDTO.mapFrom(viagem), viagem.id)

        return dto.body() ?: ViagemResponseDTO()
    }

    suspend fun deletaViagem(viagem: Viagem): Boolean {
        viagemService.deletaViagem(viagem.id)
        return true
    }
}
