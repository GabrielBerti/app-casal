package br.com.appcasal.dao.dto.network.request

import br.com.appcasal.domain.model.Meta

data class MetaRequestDTO(
    val id: Long? = null,
    val descricao: String? = null,
    val concluido: Boolean? = null
) {
    companion object {
        fun mapFrom(meta: Meta) = MetaRequestDTO(
            id = meta.id,
            descricao = meta.descricao,
            concluido = meta.concluido
        )
    }
}
