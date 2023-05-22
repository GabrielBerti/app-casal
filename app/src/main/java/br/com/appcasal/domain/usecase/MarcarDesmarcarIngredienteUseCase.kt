package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.IngredienteRepository

class MarcarDesmarcarIngredienteUseCase(
    private val ingredienteRepository: IngredienteRepository
) {
    suspend fun runAsync(idIngrediente: Long, marcado: Boolean): Boolean {
        return ingredienteRepository.marcarDesmarcarIngrediente(idIngrediente, !marcado)
    }
}
