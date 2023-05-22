package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.ReceitaRepository
import br.com.appcasal.domain.model.Receita

class GetReceitasUseCase(
    private val receitaRepository: ReceitaRepository
) {
    suspend fun runAsync(search: String?): List<Receita> {
        return receitaRepository.recuperaReceitas(search)
    }
}
