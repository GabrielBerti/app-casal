package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.LugarVisitadoRepository
import br.com.appcasal.domain.model.LugarVisitado
import br.com.appcasal.domain.model.Viagem

class AlteraLugarVisitadoUseCase(
    private val lugarVisitadoRepository: LugarVisitadoRepository
) {
    suspend fun runAsync(lugarVisitado: LugarVisitado, viagem: Viagem): LugarVisitado {
        return lugarVisitadoRepository.alteraLugarVisitado(lugarVisitado, viagem)
    }
}
