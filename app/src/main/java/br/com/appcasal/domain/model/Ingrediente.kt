package br.com.appcasal.domain.model

import android.os.Parcelable
import br.com.appcasal.dao.dto.network.response.IngredienteResponseDTO
import kotlinx.android.parcel.Parcelize

@Parcelize
class Ingrediente(
    val id: Long = 0L,
    val descricao: String,
    var marcado: Boolean,
) : Parcelable {
    companion object {
        fun mapFrom(ingredienteResponseDTO: IngredienteResponseDTO) =
            Ingrediente(
                id = ingredienteResponseDTO.id ?: 0,
                descricao = ingredienteResponseDTO.descricao ?: "",
                marcado = ingredienteResponseDTO.marcado ?: false,
            )
    }
}