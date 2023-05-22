package br.com.appcasal.domain.model

import br.com.appcasal.dao.dto.network.response.ResumoResponseDTO
import java.math.BigDecimal

class Resumo(
    val saldoBiel: BigDecimal? = BigDecimal.ZERO,
    val saldoMari: BigDecimal? = BigDecimal.ZERO
) {

    companion object {
        fun mapTo(resumoResponseDTO: ResumoResponseDTO): Resumo =
            Resumo(
                saldoBiel = resumoResponseDTO.saldoBiel,
                saldoMari = resumoResponseDTO.saldoMari
            )
    }

}