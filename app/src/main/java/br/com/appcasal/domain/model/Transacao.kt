package br.com.appcasal.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity
class Transacao (
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val valor: BigDecimal,
    val descricao: String = "",
    val tipo: Tipo,
    val data: String ) //Calendar = Calendar.getInstance())