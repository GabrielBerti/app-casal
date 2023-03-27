package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.IngredienteRepository

class DesmarcarTodosIngredientesUseCase(
    private val ingredienteRepository: IngredienteRepository
) {
    suspend fun runAsync(receitaId: Long): Boolean {
        return ingredienteRepository.desmarcarTodosIngredientes(receitaId)
    }
}
