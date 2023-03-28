package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.LugarVisitadoRepository
import br.com.appcasal.domain.model.LugarVisitado
import br.com.appcasal.domain.model.Viagem

class InsereLugarVisitadoUseCase(
    private val lugarVisitadoRepository: LugarVisitadoRepository
) {
    suspend fun runAsync(lugarVisitado: LugarVisitado, viagem: Viagem): LugarVisitado {
        return lugarVisitadoRepository.insereLugarVisitado(lugarVisitado, viagem)
    }
}
