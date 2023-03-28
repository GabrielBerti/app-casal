package br.com.appcasal.dao.dto.network.request

import br.com.appcasal.domain.model.LugarVisitado
import br.com.appcasal.domain.model.Viagem

data class LugarVisitadoRequestDTO(
    val id: Long? = null,
    val nome: String? = null,
    val nota: Double? = null,
    var viagem: Viagem? = null
) {
    companion object {
        fun mapFrom(lugarVisitado: LugarVisitado, viagem: Viagem?) =
            LugarVisitadoRequestDTO(
                id = lugarVisitado.id,
                nome = lugarVisitado.nome,
                nota = lugarVisitado.nota,
                viagem = viagem
            )
    }
}
