package br.com.appcasal.dao.dto.network.request

import br.com.appcasal.domain.model.Tipo
import br.com.appcasal.domain.model.Transacao
import java.math.BigDecimal

data class TransacaoRequestDTO(
    val id: Long? = null,
    val valor: BigDecimal? = null,
    val descricao: String? = null,
    val tipo: Tipo? = null,
    val data: String? = null
) {
    companion object {
        fun mapFrom(transacao: Transacao) = TransacaoRequestDTO(
            id = transacao.id,
            valor = transacao.valor,
            descricao = transacao.descricao,
            tipo =  transacao.tipo,
            data = transacao.data
        )
    }
}
