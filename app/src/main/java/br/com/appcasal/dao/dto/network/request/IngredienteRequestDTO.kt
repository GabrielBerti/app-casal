package br.com.appcasal.dao.dto.network.request

import br.com.appcasal.domain.model.Ingrediente
import br.com.appcasal.domain.model.Receita

data class IngredienteRequestDTO(
    val id: Long? = null,
    val descricao: String? = null,
    var marcado: Boolean? = null,
    var receita: Receita? = null
) {
    companion object {
        fun mapFrom(ingrediente: Ingrediente, receita: Receita?) =
            IngredienteRequestDTO(
                id = ingrediente.id,
                descricao = ingrediente.descricao,
                marcado = ingrediente.marcado,
                receita = receita
            )
    }
}
