package br.com.appcasal.dao.dto.network.request

import br.com.appcasal.domain.model.Tipo
import br.com.appcasal.domain.model.Transacao
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

data class TransacaoRequestDTO(
    val id: Long? = null,
    val valor: BigDecimal? = null,
    val descricao: String? = null,
    val tipo: Tipo? = null,
    val data: String? = null
) {
    companion object {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

        fun mapFrom(transacao: Transacao) = TransacaoRequestDTO(
            id = transacao.id,
            valor = transacao.valor,
            descricao = transacao.descricao,
            tipo =  transacao.tipo,
            data = dateToString(transacao.data)
        )

        fun dateToString(data: Calendar): String {
            return format.format(data.time)
        }
    }


}
