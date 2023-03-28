package br.com.appcasal.dao.repository

import br.com.appcasal.dao.datasource.remote.ReceitaRemoteDataSource
import br.com.appcasal.domain.model.Receita

class ReceitaRepository(
    private val receitaRemoteDataSource: ReceitaRemoteDataSource
) {
    suspend fun recuperaReceitas(): List<Receita> {
        val result = receitaRemoteDataSource.recuperaReceitas()

        return result.map { Receita.mapFrom(it) }
    }

    suspend fun insereReceita(receita: Receita): Receita {
        val result = receitaRemoteDataSource.insereReceita(receita)

        return Receita.mapFrom(result)
    }

    suspend fun alteraReceita(receita: Receita): Receita {
        val result = receitaRemoteDataSource.alteraReceita(receita)

        return Receita.mapFrom(result)
    }

    suspend fun deletaReceita(receita: Receita): Boolean {
        return receitaRemoteDataSource.deletaReceita(receita)
    }

}
