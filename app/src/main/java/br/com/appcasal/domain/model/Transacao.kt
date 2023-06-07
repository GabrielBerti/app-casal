package br.com.appcasal.domain.model

import br.com.appcasal.dao.dto.network.response.TransacaoResponseDTO
import java.math.BigDecimal
import java.util.*

class Transacao(
    val id: Long = 0L,
    val valor: BigDecimal,
    val descricao: String = "",
    val tipo: Tipo,
    val data: Calendar = Calendar.getInstance()
) {
    companion object {
        fun mapFrom(transacaoResponseDTO: TransacaoResponseDTO): Transacao {
            transacaoResponseDTO.data?.add(Calendar.HOUR_OF_DAY, 3)

            return Transacao(
                id = transacaoResponseDTO.id ?: 0,
                valor = transacaoResponseDTO.valor ?: BigDecimal.ZERO,
                descricao = transacaoResponseDTO.descricao ?: "",
                tipo = transacaoResponseDTO.tipo ?: Tipo.BIEL,
                data = transacaoResponseDTO.data ?: Calendar.getInstance()
            )
        }

    }
}