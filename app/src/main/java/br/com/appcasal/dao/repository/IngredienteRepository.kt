package br.com.appcasal.dao.repository

import br.com.appcasal.dao.datasource.remote.IngredienteRemoteDataSource
import br.com.appcasal.dao.dto.network.response.IngredienteResponseDTO
import br.com.appcasal.domain.model.Ingrediente
import br.com.appcasal.domain.model.Receita

class IngredienteRepository(
    private val ingredienteRemoteDataSource: IngredienteRemoteDataSource
) {

    suspend fun insereIngrediente(ingredientes: List<Ingrediente>, receita: Receita): Ingrediente {
        val result = ingredienteRemoteDataSource.insereIngrediente(ingredientes, receita)
        return mapFrom(result)  // TODO fazer mapfrom
    }

    suspend fun alteraIngrediente(ingrediente: Ingrediente, receita: Receita): Ingrediente {
        val result = ingredienteRemoteDataSource.alteraIngrediente(ingrediente, receita)
        return mapFrom(result)  // TODO fazer mapfrom
    }

    suspend fun marcarDesmarcarIngrediente(idIngrediente: Long, marcado: Boolean): Boolean {
        ingredienteRemoteDataSource.marcarDesmarcarIngrediente(idIngrediente, marcado)
        return true
    }

    suspend fun desmarcarTodosIngredientes(receitaId: Long): Boolean {
        ingredienteRemoteDataSource.desmarcarTodosIngredientes(receitaId)
        return true
    }

    suspend fun recuperaIngredientesByReceitaId(receita: Receita): List<Ingrediente> {
        val result = ingredienteRemoteDataSource.recuperaIngredientesByReceitaId(receita)
        return result.map { mapFrom(it)} // TODO fazer mapfrom
    }

    suspend fun deletaIngrediente(ingrediente: Ingrediente): Boolean {
        return ingredienteRemoteDataSource.deletaIngrediente(ingrediente)
    }

    fun mapFrom(ingredienteResponseDTO: IngredienteResponseDTO) =
        Ingrediente(
            id = ingredienteResponseDTO.id!!,
            descricao = ingredienteResponseDTO.descricao!!,
            marcado = ingredienteResponseDTO.marcado!!
        ) // TODO colocar o map from dentro da classe meta


}
