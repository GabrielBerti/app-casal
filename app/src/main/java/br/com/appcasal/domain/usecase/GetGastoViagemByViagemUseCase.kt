package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.GastoViagemRepository
import br.com.appcasal.domain.model.GastoViagem
import br.com.appcasal.domain.model.Viagem

class GetGastoViagemByViagemUseCase(
    private val gastoViagemRepository: GastoViagemRepository
) {
    suspend fun runAsync(viagem: Viagem): List<GastoViagem> {
        return gastoViagemRepository.recuperaGastosViagemByViagemId(viagem)
    }
}
