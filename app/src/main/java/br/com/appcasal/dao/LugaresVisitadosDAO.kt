package br.com.appcasal.dao

import androidx.room.*
import br.com.appcasal.domain.model.LugarVisitado

@Dao
interface LugaresVisitadosDAO {

    @Query("SELECT * FROM LugarVisitado WHERE viagemId == :viagemId")
    fun buscaLugaresVisitadosByViagem(viagemId: Long) : MutableList<LugarVisitado>

    @Query("DELETE FROM LugarVisitado WHERE viagemId == :viagemId")
    fun deleteLugaresVisitadosByViagem(viagemId: Long)

    @Insert
    fun adiciona(lugarVisitado: LugarVisitado)

    @Delete
    fun remove(lugarVisitado: LugarVisitado)

    @Update
    fun altera(lugarVisitado: LugarVisitado)
}