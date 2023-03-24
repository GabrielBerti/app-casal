package br.com.appcasal.dao.datasource.remote

import br.com.appcasal.dao.datasource.CallableDataSource
import br.com.appcasal.dao.dto.network.request.ReceitaRequestDTO
import br.com.appcasal.dao.dto.network.response.ReceitaResponseDTO
import br.com.appcasal.dao.service.ReceitaService
import br.com.appcasal.domain.model.Receita

class ReceitaRemoteDataSource(
    private val receitaService: ReceitaService
): CallableDataSource {

    suspend fun recuperaReceitas(): List<ReceitaResponseDTO> {
        val dto = receitaService.recuperaReceitas()

        return dto.body() ?: emptyList()
    }

    suspend fun insereReceita(receita: Receita): ReceitaResponseDTO {
        val dto = receitaService.insereReceita(ReceitaRequestDTO.mapFrom(receita))

        return dto.body() ?: ReceitaResponseDTO()
    }

    suspend fun alteraReceita(receita: Receita): ReceitaResponseDTO {
        val dto = receitaService.alteraReceita(ReceitaRequestDTO.mapFrom(receita), receita.id)

        return dto.body() ?: ReceitaResponseDTO()
    }

    suspend fun deletaReceita(receita: Receita): Boolean {
        receitaService.deletaReceita(receita.id)
        return true
    }
}
