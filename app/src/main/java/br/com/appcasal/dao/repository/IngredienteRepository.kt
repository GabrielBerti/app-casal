package br.com.appcasal.dao.repository

import br.com.appcasal.dao.datasource.remote.IngredienteRemoteDataSource
import br.com.appcasal.domain.model.Ingrediente
import br.com.appcasal.domain.model.Receita

class IngredienteRepository(
    private val ingredienteRemoteDataSource: IngredienteRemoteDataSource
) {

    suspend fun insereIngrediente(ingredientes: List<Ingrediente>, receita: Receita): Ingrediente {
        val result = ingredienteRemoteDataSource.insereIngrediente(ingredientes, receita)
        return Ingrediente.mapFrom(result)
    }

    suspend fun alteraIngrediente(ingrediente: Ingrediente, receita: Receita): Ingrediente {
        val result = ingredienteRemoteDataSource.alteraIngrediente(ingrediente, receita)
        return Ingrediente.mapFrom(result)
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
        return result.map { Ingrediente.mapFrom(it)}
    }

    suspend fun deletaIngrediente(ingrediente: Ingrediente): Boolean {
        return ingredienteRemoteDataSource.deletaIngrediente(ingrediente)
    }

}
