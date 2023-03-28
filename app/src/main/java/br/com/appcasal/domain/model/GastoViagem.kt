package br.com.appcasal.domain.model

import android.os.Parcelable
import br.com.appcasal.dao.dto.network.response.GastoViagemResponseDTO
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
class GastoViagem(
    val id: Long = 0L,
    val valor: BigDecimal,
    val descricao: String
) : Parcelable {
    companion object {
        fun mapFrom(gastoViagemResponseDTO: GastoViagemResponseDTO) =
            GastoViagem(
                id = gastoViagemResponseDTO.id ?: 0,
                valor = gastoViagemResponseDTO.valor ?: BigDecimal.ZERO,
                descricao = gastoViagemResponseDTO.descricao ?: ""
            )
    }
}