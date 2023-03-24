package br.com.appcasal.domain.model

import java.math.BigDecimal

class GastoViagem(
   val id: Long = 0L,
    val valor: BigDecimal,
    val descricao: String,
    val viagemId: Long
)