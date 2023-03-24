package br.com.appcasal.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Viagem::class,
            parentColumns = ["id"],
            childColumns = ["viagemId"]
        )
    ]
)
class LugarVisitado(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val nome: String,
    val legal: Int,
    @ColumnInfo(index = true)
    val viagemId: Long
)