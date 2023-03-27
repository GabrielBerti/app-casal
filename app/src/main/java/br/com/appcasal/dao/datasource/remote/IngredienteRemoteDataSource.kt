package br.com.appcasal.dao.datasource.remote

import br.com.appcasal.dao.datasource.CallableDataSource
import br.com.appcasal.dao.dto.network.request.IngredienteRequestDTO
import br.com.appcasal.dao.dto.network.response.IngredienteResponseDTO
import br.com.appcasal.dao.service.IngredienteService
import br.com.appcasal.domain.model.Ingrediente
import br.com.appcasal.domain.model.Receita

class IngredienteRemoteDataSource(
    private val ingredienteService: IngredienteService
): CallableDataSource {

    suspend fun insereIngrediente(ingredientes: List<Ingrediente>, receita: Receita): IngredienteResponseDTO {
        val dto = ingredienteService.insereIngrediente(ingredientes.map { IngredienteRequestDTO.mapFrom(it, receita) })

        return dto.body() ?: IngredienteResponseDTO()
    }

    suspend fun alteraIngrediente(ingrediente: Ingrediente, receita: Receita): IngredienteResponseDTO {
        val dto = ingredienteService.alteraIngrediente(IngredienteRequestDTO.mapFrom(ingrediente, receita), ingrediente.id)

        return dto.body() ?: IngredienteResponseDTO()
    }

    suspend fun recuperaIngredientesByReceitaId(receita: Receita): List<IngredienteResponseDTO> {
        val dto = ingredienteService.recuperaIngredientesByReceitaId(receita.id)

        return dto.body() ?: emptyList()
    }

    suspend fun deletaIngrediente(ingrediente: Ingrediente): Boolean {
        ingredienteService.deletaIngrediente(ingrediente.id)
        return true
    }
}
