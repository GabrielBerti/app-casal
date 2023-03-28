package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.ViagemRepository
import br.com.appcasal.domain.model.Viagem

class InsereViagemUseCase(
    private val viagemRepository: ViagemRepository
) {
    suspend fun runAsync(viagem: Viagem): Viagem {
        return viagemRepository.insereViagem(viagem)
    }
}
