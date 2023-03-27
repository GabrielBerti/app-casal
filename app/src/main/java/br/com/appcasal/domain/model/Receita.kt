package br.com.appcasal.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Receita(
    val id: Long = 0L,
    val nome: String,
    val descricao: String,
    val ingredientes: List<Ingrediente>?
): Parcelable