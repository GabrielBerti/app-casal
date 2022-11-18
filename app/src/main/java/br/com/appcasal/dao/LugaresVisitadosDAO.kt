package br.com.appcasal.dao

import androidx.room.*
import br.com.appcasal.model.Ingrediente
import br.com.appcasal.model.LugaresVisitados

@Dao
interface LugaresVisitadosDAO {

    @Query("SELECT * FROM LugaresVisitados WHERE viagemId == :viagemId")
    fun buscaLugaresVisitadosByViagem(viagemId: Long) : MutableList<LugaresVisitados>

    @Query("DELETE FROM LugaresVisitados WHERE viagemId == :viagemId")
    fun deleteLugaresVisitadosByViagem(viagemId: Long)

    @Query("UPDATE LugaresVisitados SET legal = :isLegal")
    fun alteraLugarVisitadoLegal(isLegal: Boolean)

    @Insert
    fun adiciona(lugarVisitado: LugaresVisitados)

    @Delete
    fun remove(lugarVisitado: LugaresVisitados)

    @Update
    fun altera(lugarVisitado: LugaresVisitados)
}