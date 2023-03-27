package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.IngredienteRepository
import br.com.appcasal.domain.model.Ingrediente
import br.com.appcasal.domain.model.Receita

class AlteraIngredienteUseCase(
    private val ingredienteRepository: IngredienteRepository
) {
    suspend fun runAsync(ingrediente: Ingrediente, receita: Receita): Ingrediente {
        return ingredienteRepository.alteraIngrediente(ingrediente = ingrediente, receita)
    }
}
