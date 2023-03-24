package br.com.appcasal.dao

import androidx.room.*
import br.com.appcasal.domain.model.GastoViagem

@Dao
interface GastosViagemDAO {

    @Query("SELECT * FROM GastoViagem WHERE viagemId == :viagemId")
    fun buscaGastosViagemByViagem(viagemId: Long) : MutableList<GastoViagem>

    @Query("DELETE FROM GastoViagem WHERE viagemId == :viagemId")
    fun deleteGastosViagemByViagem(viagemId: Long)

    @Insert
    fun adiciona(gastoViagem: GastoViagem)

    @Delete
    fun remove(gastoViagem: GastoViagem)

    @Update
    fun altera(gastoViagem: GastoViagem)
}