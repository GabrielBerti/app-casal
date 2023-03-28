package br.com.appcasal.domain.usecase

import br.com.appcasal.dao.repository.ViagemRepository
import br.com.appcasal.domain.model.Viagem

class GetViagensUseCase(
    private val viagemRepository: ViagemRepository
) {
    suspend fun runAsync(): List<Viagem> {
        return viagemRepository.recuperaViagens()
    }
}
