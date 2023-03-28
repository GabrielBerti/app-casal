package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.IngredienteRepository
import br.com.appcasal.domain.model.Ingrediente
import br.com.appcasal.domain.model.Receita

class GetIngredienteByReceitaUseCase(
    private val ingredienteRepository: IngredienteRepository
) {
    suspend fun runAsync(receita: Receita): List<Ingrediente> {
        return ingredienteRepository.recuperaIngredientesByReceitaId(receita)
    }
}
