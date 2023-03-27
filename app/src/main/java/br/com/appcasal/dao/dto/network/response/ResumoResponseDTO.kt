package br.com.appcasal.dao.dto.network.response

import java.math.BigDecimal

data class ResumoResponseDTO(
    val saldoBiel: BigDecimal? = BigDecimal.ZERO,
    val saldoMari: BigDecimal? = BigDecimal.ZERO
)
