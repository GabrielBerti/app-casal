package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.LugarVisitadoRepository
import br.com.appcasal.domain.model.LugarVisitado

class DeletaLugarVisitadoUseCase(
    private val lugarVisitadoRepository: LugarVisitadoRepository
) {
    suspend fun runAsync(lugarVisitado: LugarVisitado): Boolean {
        return lugarVisitadoRepository.deletaLugarVisitado(lugarVisitado)
    }
}
