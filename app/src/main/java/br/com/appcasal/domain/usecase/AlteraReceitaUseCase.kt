package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.ReceitaRepository
import br.com.appcasal.domain.model.Receita

class AlteraReceitaUseCase(
    private val receitaRepository: ReceitaRepository
) {
    suspend fun runAsync(receita: Receita): Receita {
        return receitaRepository.alteraReceita(receita)
    }
}
