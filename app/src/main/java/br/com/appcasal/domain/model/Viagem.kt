package br.com.appcasal.domain.model

import android.os.Parcelable
import br.com.appcasal.dao.dto.network.response.ViagemResponseDTO
import kotlinx.android.parcel.Parcelize

@Parcelize
class Viagem(
    val id: Long = 0L,
    val local: String,
    val dataInicio: String,
    val dataFim: String,
    val nota: Double,
    val lugaresVisitados: List<LugarVisitado>,
    val gastosViagens: List<GastoViagem>
): Parcelable {
    companion object {
        fun mapFrom(viagemResponseDTO: ViagemResponseDTO) =
            Viagem(
                id = viagemResponseDTO.id ?: 0,
                local = viagemResponseDTO.local ?: "",
                dataInicio = viagemResponseDTO.dataInicio ?: "",
                dataFim = viagemResponseDTO.dataFim ?: "",
                nota = viagemResponseDTO.nota ?: 0.0,
                lugaresVisitados = viagemResponseDTO.lugaresVisitados ?: listOf(),
                gastosViagens = viagemResponseDTO.gastosViagem ?: listOf()
            )
    }
}