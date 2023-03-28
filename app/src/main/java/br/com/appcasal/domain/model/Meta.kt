package br.com.appcasal.domain.model

import br.com.appcasal.dao.dto.network.response.MetaResponseDTO

class Meta(
    val id: Long = 0L,
    val descricao: String,
    var concluido: Boolean
) {
    companion object {
        fun mapFrom(metaResponseDTO: MetaResponseDTO) =
            Meta(
                id = metaResponseDTO.id ?: 0,
                descricao = metaResponseDTO.descricao ?: "",
                concluido = metaResponseDTO.concluido ?: false
            )
    }
}