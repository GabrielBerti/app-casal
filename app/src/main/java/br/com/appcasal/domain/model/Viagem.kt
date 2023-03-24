package br.com.appcasal.domain.model

class Viagem(
    val id: Long = 0L,
    val local: String,
    val dataInicio: String,
    val dataFim: String,
    val nota: Int
)