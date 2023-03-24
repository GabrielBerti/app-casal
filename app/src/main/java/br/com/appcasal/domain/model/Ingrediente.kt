package br.com.appcasal.domain.model

import br.com.appcasal.dao.dto.network.response.IngredienteResponseDTO

class Ingrediente(
    val id: Long = 0L,
    val descricao: String,
    var marcado: Boolean
) {
    companion object {
        fun mapFrom(ingredienteResponseDTO: IngredienteResponseDTO) =
            Ingrediente(
                // TODO tirar !!
                id = ingredienteResponseDTO.id!!,
                descricao = ingredienteResponseDTO.descricao!!,
                marcado = ingredienteResponseDTO.marcado!!
            )
    }
}