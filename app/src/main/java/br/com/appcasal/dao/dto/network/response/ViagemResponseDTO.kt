package br.com.appcasal.dao.dto.network.response

import br.com.appcasal.domain.model.GastoViagem
import br.com.appcasal.domain.model.LugarVisitado
import java.util.*

data class ViagemResponseDTO(
    val id: Long? = null,
    val local: String? = null,
    val dataInicio: Calendar? = null,
    val dataFim: Calendar? = null,
    val nota: Double? = null,
    val lugaresVisitados: List<LugarVisitado>? = null,
    val gastosViagem: List<GastoViagem>? = null
)
