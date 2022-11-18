package br.com.appcasal.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Viagem::class,
            parentColumns = ["id"],
            childColumns = ["viagemId"]
        )
    ]
)
class GastosViagem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val valor: BigDecimal,
    val descricao: String,
    @ColumnInfo(index = true)
    val viagemId: Long
)