package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.ReceitaRepository
import br.com.appcasal.domain.model.Receita

class DeletaReceitaUseCase(
    private val receitaRepository: ReceitaRepository
) {
    suspend fun runAsync(receita: Receita): Boolean {
        return receitaRepository.deletaReceita(receita)
    }
}
