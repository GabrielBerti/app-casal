package br.com.appcasal.domain.model

import android.os.Parcelable
import br.com.appcasal.dao.dto.network.response.ReceitaResponseDTO
import kotlinx.android.parcel.Parcelize

@Parcelize
class Receita(
    val id: Long = 0L,
    val nome: String,
    val descricao: String,
    val ingredientes: List<Ingrediente>?
): Parcelable {
    companion object {
        fun mapFrom(receitaResponseDTO: ReceitaResponseDTO) =
            Receita(
                id = receitaResponseDTO.id ?: 0,
                nome = receitaResponseDTO.nome ?: "",
                descricao = receitaResponseDTO.descricao ?: "",
                ingredientes = receitaResponseDTO.ingredientes ?: listOf()
            )
    }
}