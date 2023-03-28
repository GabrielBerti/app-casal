package br.com.appcasal.dao.dto.network.request

import br.com.appcasal.domain.model.GastoViagem
import br.com.appcasal.domain.model.Viagem
import java.math.BigDecimal

data class GastoViagemRequestDTO(
    val id: Long? = null,
    val valor: BigDecimal? = null,
    val descricao: String? = null,
    var viagem: Viagem? = null
) {
    companion object {
        fun mapFrom(gastoViagem: GastoViagem, viagem: Viagem?) =
            GastoViagemRequestDTO(
                id = gastoViagem.id,
                valor = gastoViagem.valor,
                descricao = gastoViagem.descricao,
                viagem = viagem
            )
    }
}
