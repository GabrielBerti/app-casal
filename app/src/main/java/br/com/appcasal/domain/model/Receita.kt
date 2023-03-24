package br.com.appcasal.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Receita (
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val nome: String,
    val descricao: String
)