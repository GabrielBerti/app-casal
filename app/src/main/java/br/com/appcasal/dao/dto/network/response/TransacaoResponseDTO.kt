package br.com.appcasal.dao.dto.network.response

import br.com.appcasal.domain.model.Tipo
import java.math.BigDecimal
import java.util.*

data class TransacaoResponseDTO(
    val id: Long? = null,
    val valor: BigDecimal? = null,
    val descricao: String? = null,
    val tipo: Tipo? = null,
    val data: Calendar? = null
)
