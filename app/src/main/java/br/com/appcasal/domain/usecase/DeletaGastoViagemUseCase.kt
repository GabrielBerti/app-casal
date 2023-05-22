package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.GastoViagemRepository
import br.com.appcasal.domain.model.GastoViagem

class DeletaGastoViagemUseCase(
    private val gastoViagemRepository: GastoViagemRepository
) {
    suspend fun runAsync(gastoViagem: GastoViagem): Boolean {
        return gastoViagemRepository.deletaGastosViagem(gastoViagem)
    }
}
