package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.IngredienteRepository
import br.com.appcasal.domain.model.Ingrediente

class DeletaIngredienteUseCase(
    private val ingredienteRepository: IngredienteRepository
) {
    suspend fun runAsync(ingrediente: Ingrediente): Boolean {
        return ingredienteRepository.deletaIngrediente(ingrediente = ingrediente)
    }
}
