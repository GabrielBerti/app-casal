package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.LugarVisitadoRepository
import br.com.appcasal.domain.model.LugarVisitado
import br.com.appcasal.domain.model.Viagem

class GetLugarVisitadoByViagemUseCase(
    private val lugarVisitadoRepository: LugarVisitadoRepository
) {
    suspend fun runAsync(viagem: Viagem): List<LugarVisitado> {
        return lugarVisitadoRepository.recuperaLugaresVisitadosByViagemId(viagem)
    }
}
