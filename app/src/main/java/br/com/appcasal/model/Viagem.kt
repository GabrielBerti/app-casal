package br.com.appcasal.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Viagem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val pais: String,
    val cidade: String,
    val dataInicio: String,
    val dataFim: String
)