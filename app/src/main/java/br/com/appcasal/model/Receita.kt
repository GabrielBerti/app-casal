package br.com.appcasal.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Receita (
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val nome: String,
    val descricao: String
)