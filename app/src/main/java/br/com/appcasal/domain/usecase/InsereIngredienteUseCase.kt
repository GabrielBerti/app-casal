package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.IngredienteRepository
import br.com.appcasal.domain.model.Ingrediente
import br.com.appcasal.domain.model.Receita

class InsereIngredienteUseCase(
    private val ingredienteRepository: IngredienteRepository
) {
    suspend fun runAsync(ingredientes: List<Ingrediente>, receita: Receita): Ingrediente {
        return ingredienteRepository.insereIngrediente(ingredientes = ingredientes, receita)
    }
}
