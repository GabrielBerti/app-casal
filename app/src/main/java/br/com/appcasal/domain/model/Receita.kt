package br.com.appcasal.domain.model

import java.io.Serializable

class Receita(
    val id: Long = 0L,
    val nome: String,
    val descricao: String,
    val ingredientes: List<Ingrediente>?
): Serializable