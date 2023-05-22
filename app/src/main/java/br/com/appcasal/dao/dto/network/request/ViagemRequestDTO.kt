package br.com.appcasal.dao.dto.network.request

import br.com.appcasal.domain.model.Viagem
import java.text.SimpleDateFormat
import java.util.*

data class ViagemRequestDTO(
    val id: Long? = null,
    val local: String? = null,
    val dataInicio: String? = null,
    val dataFim: String? = null,
    val nota: Double? = null
) {
    companion object {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

        fun mapFrom(viagem: Viagem) = ViagemRequestDTO(
            id = viagem.id,
            local = viagem.local,
            dataInicio = dateToString(viagem.dataInicio),
            dataFim = dateToString(viagem.dataFim),
            nota = viagem.nota
        )

        fun dateToString(data: Calendar): String {
            return format.format(data.time)
        }
    }
}
