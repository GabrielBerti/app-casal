package br.com.appcasal.dao.dto.network.response

import java.math.BigDecimal

data class GastoViagemResponseDTO(
    val id: Long? = null,
    val valor: BigDecimal? = null,
    val descricao: String? = null
)
