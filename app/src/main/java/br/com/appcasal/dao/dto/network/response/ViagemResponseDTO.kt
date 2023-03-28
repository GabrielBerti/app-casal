package br.com.appcasal.dao.dto.network.response

import br.com.appcasal.domain.model.GastoViagem
import br.com.appcasal.domain.model.LugarVisitado

data class ViagemResponseDTO(
    val id: Long? = null,
    val local: String? = null,
    val dataInicio: String? = null,
    val dataFim: String? = null,
    val nota: Double? = null,
    val lugaresVisitados: List<LugarVisitado>? = null,
    val gastosViagem: List<GastoViagem>? = null
)
