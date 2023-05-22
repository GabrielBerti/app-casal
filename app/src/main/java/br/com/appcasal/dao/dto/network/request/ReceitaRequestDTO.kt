package br.com.appcasal.dao.dto.network.request

import br.com.appcasal.domain.model.Receita

data class ReceitaRequestDTO(
    val id: Long? = null,
    val nome: String? = null,
    val descricao: String? = null
) {
    companion object {
        fun mapFrom(receita: Receita) = ReceitaRequestDTO(
            id = receita.id,
            descricao = receita.descricao,
            nome = receita.nome,
        )
    }
}
