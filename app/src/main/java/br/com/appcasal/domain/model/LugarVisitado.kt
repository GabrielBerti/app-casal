package br.com.appcasal.domain.model

import android.os.Parcelable
import br.com.appcasal.dao.dto.network.response.LugarVisitadoResponseDTO
import kotlinx.android.parcel.Parcelize

@Parcelize
class LugarVisitado(
    val id: Long = 0L,
    val nome: String,
    val nota: Double
) : Parcelable {
    companion object {
        fun mapFrom(lugarVisitadoResponseDTO: LugarVisitadoResponseDTO) =
            LugarVisitado(
                id = lugarVisitadoResponseDTO.id ?: 0,
                nome = lugarVisitadoResponseDTO.nome ?: "",
                nota = lugarVisitadoResponseDTO.nota ?: 0.0,
            )
    }
}