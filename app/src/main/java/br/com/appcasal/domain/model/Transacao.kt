package br.com.appcasal.domain.model

import java.math.BigDecimal

class Transacao (
    val id: Long = 0L,
    val valor: BigDecimal,
    val descricao: String = "",
    val tipo: Tipo,
    val data: String ) //Calendar = Calendar.getInstance())