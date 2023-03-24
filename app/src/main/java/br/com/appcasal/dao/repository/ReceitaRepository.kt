package br.com.appcasal.dao.repository

import br.com.appcasal.dao.datasource.remote.ReceitaRemoteDataSource
import br.com.appcasal.dao.dto.network.response.ReceitaResponseDTO
import br.com.appcasal.domain.model.Ingrediente
import br.com.appcasal.domain.model.Receita

class ReceitaRepository(
    private val receitaRemoteDataSource: ReceitaRemoteDataSource
) {
    suspend fun recuperaReceitas(): List<Receita> {
        val result = receitaRemoteDataSource.recuperaReceitas()

        return result.map { mapFrom(it) } // TODO fazer mapfrom
    }

    suspend fun insereReceita(receita: Receita): Receita {
        val result = receitaRemoteDataSource.insereReceita(receita)

        return mapFrom(result)  // TODO fazer mapfrom
    }

    suspend fun alteraReceita(receita: Receita): Receita {
        val result = receitaRemoteDataSource.alteraReceita(receita)

        return mapFrom(result)  // TODO fazer mapfrom
    }

    suspend fun deletaReceita(receita: Receita): Boolean {
        return receitaRemoteDataSource.deletaReceita(receita)
    }

    fun mapFrom(receitaResponseDTO: ReceitaResponseDTO) =
        Receita(
            id = receitaResponseDTO.id!!,
            nome = receitaResponseDTO.nome!!,
            descricao = receitaResponseDTO.descricao ?: "",
            ingredientes =  receitaResponseDTO.ingredientes?.map { Ingrediente.mapFrom(it) }
        ) // TODO colocar o map from dentro da classe


}
