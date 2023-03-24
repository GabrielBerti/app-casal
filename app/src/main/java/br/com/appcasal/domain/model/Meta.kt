package br.com.appcasal.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Meta(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val descricao: String,
    var concluido: Boolean
)