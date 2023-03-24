package br.com.appcasal.dao

import androidx.room.*
import br.com.appcasal.domain.model.Receita

@Dao
interface ReceitaDAO {

    @Query("SELECT * FROM Receita WHERE id == :receitaId")
    fun buscaReceitaById(receitaId: Long) : List<Receita>

    @Query("SELECT *FROM Receita WHERE id = (SELECT MAX( id ) FROM Receita)")
    fun buscaUltimaReceitaInserida() : Receita

    @Query("SELECT * FROM Receita order by nome")
    fun buscaTodos() : List<Receita>

    @Insert
    fun adiciona(receita: Receita)

    @Delete
    fun remove(receita: Receita)

    @Query("delete from Receita")
    fun removeAll()

    @Update
    fun altera(receita: Receita)
}