package br.com.appcasal.dao.dto.network.request

import br.com.appcasal.domain.model.Ingrediente

data class IngredienteRequestDTO(
    val id: Long? = null,
    val descricao: String? = null,
    var marcado: Boolean? = null
) {
    companion object {
        fun mapFrom(ingrediente: Ingrediente) =
            IngredienteRequestDTO(
                id = ingrediente.id,
                descricao = ingrediente.descricao,
                marcado = ingrediente.marcado
            )
    }
}
