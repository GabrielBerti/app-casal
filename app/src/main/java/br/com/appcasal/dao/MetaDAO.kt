package br.com.appcasal.dao

import androidx.room.*
import br.com.appcasal.model.Meta

@Dao
interface MetaDAO {

    @Query("SELECT * FROM Meta order by concluido")
    fun buscaTodos() : List<Meta>

    @Query("SELECT * FROM Meta where concluido = 1")
    fun buscaConcluidas() : List<Meta>

    @Query("SELECT * FROM Meta where concluido = 0")
    fun buscaNaoConcluidas() : List<Meta>

    @Insert
    fun adiciona(meta: Meta)

    @Delete
    fun remove(meta: Meta)

    @Query("delete from Meta")
    fun removeAll()

    @Update
    fun altera(meta: Meta)

}