package br.com.appcasal.dao.dto.network.response

import br.com.appcasal.domain.model.Ingrediente

data class ReceitaResponseDTO(
    val id: Long? = null,
    val nome: String? = null,
    val descricao: String? = null,
    val ingredientes: List<Ingrediente>? = null
)
