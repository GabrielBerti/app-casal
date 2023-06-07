package br.com.appcasal.domain.model

import android.os.Parcelable
import br.com.appcasal.dao.dto.network.response.ViagemResponseDTO
import br.com.appcasal.util.extension.adicionaHorasFuso
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class Viagem(
    val id: Long = 0L,
    val local: String,
    val dataInicio: Calendar,
    val dataFim: Calendar,
    val nota: Double?,
    var lugaresVisitados: List<LugarVisitado>,
    var gastosViagens: List<GastoViagem>
) : Parcelable {
    companion object {
        fun mapFrom(viagemResponseDTO: ViagemResponseDTO) =
            Viagem(
                id = viagemResponseDTO.id ?: 0,
                local = viagemResponseDTO.local ?: "",
                dataInicio = viagemResponseDTO.dataInicio?.adicionaHorasFuso()
                    ?: Calendar.getInstance(),
                dataFim = viagemResponseDTO.dataFim?.adicionaHorasFuso() ?: Calendar.getInstance(),
                nota = viagemResponseDTO.nota,
                lugaresVisitados = viagemResponseDTO.lugaresVisitados ?: listOf(),
                gastosViagens = viagemResponseDTO.gastosViagem ?: listOf()
            )
    }
}