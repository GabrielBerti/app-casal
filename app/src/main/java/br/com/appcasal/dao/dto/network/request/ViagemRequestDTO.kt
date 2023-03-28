package br.com.appcasal.dao.dto.network.request

import br.com.appcasal.domain.model.Viagem

data class ViagemRequestDTO(
    val id: Long? = null,
    val local: String? = null,
    val dataInicio: String? = null,
    val dataFim: String? = null,
    val nota: Double? = null
    //val lugaresVisitados: List<LugarVisitado>? = null,
   // val gastosViagem: List<GastoViagem>? = null
) {
    companion object {
        fun mapFrom(viagem: Viagem) = ViagemRequestDTO(
            id = viagem.id,
            local = viagem.local,
            dataInicio = viagem.dataInicio,
            dataFim = viagem.dataFim,
            nota = viagem.nota
            //lugaresVisitados = viagem.lugaresVisitados,
            //gastosViagem = viagem.gastosViagem
        )
    }
}
