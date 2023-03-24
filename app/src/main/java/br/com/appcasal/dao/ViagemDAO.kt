package br.com.appcasal.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.appcasal.domain.model.Viagem

@Dao
interface ViagemDAO {

    @Query("SELECT * FROM Viagem")
    fun buscaTodos() : List<Viagem>

    @Insert
    fun adiciona(viagem: Viagem)

    @Delete
    fun remove(viagem: Viagem)

    @Query("delete from Viagem")
    fun removeAll()

    @Update
    fun altera(viagem: Viagem)
}
