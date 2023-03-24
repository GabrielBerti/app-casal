package br.com.appcasal.dao.dto.network.response

data class ReceitaResponseDTO(
    val id: Long? = null,
    val nome: String? = null,
    val descricao: String? = null,
    val ingredientes: List<IngredienteResponseDTO>? = null
)
