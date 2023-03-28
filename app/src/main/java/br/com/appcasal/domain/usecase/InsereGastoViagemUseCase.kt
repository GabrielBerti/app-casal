package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.GastoViagemRepository
import br.com.appcasal.domain.model.GastoViagem
import br.com.appcasal.domain.model.Viagem

class InsereGastoViagemUseCase(
    private val gastoViagemRepository: GastoViagemRepository
) {
    suspend fun runAsync(gastoViagem: GastoViagem, viagem: Viagem): GastoViagem {
        return gastoViagemRepository.insereGastosViagem(gastoViagem, viagem)
    }
}
