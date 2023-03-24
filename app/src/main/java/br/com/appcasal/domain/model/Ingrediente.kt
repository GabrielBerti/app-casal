package br.com.appcasal.domain.model

class Ingrediente(
    val id: Long = 0L,
    val descricao: String,
    var marcado: Boolean,
    val receitaId: Long
)