package br.com.appcasal.model

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.*

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Receita::class,
            parentColumns = ["id"],
            childColumns = ["receitaId"]
        )
    ]
)class Ingrediente(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val descricao: String,
    @NonNull
    var marcado: Boolean,
    @NonNull
    @ColumnInfo(index = true)
    val receitaId: Long
)