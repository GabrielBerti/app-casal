package br.com.appcasal.dao

import androidx.room.*
import br.com.appcasal.model.GastosViagem

@Dao
interface GastosViagemDAO {

    @Query("SELECT * FROM GastosViagem WHERE viagemId == :viagemId")
    fun buscaGastosViagemByViagem(viagemId: Long) : MutableList<GastosViagem>

    @Query("DELETE FROM GastosViagem WHERE viagemId == :viagemId")
    fun deleteGastosViagemByViagem(viagemId: Long)

    @Insert
    fun adiciona(gastoViagem: GastosViagem)

    @Delete
    fun remove(gastoViagem: GastosViagem)

    @Update
    fun altera(gastoViagem: GastosViagem)
}