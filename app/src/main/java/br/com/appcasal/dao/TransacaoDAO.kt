package br.com.appcasal.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.appcasal.domain.model.Transacao

@Dao
interface TransacaoDAO {

    @Query("SELECT * FROM Transacao")
    fun buscaTodos() : List<Transacao>

    @Insert
    fun adiciona(transacao: Transacao)

    @Delete
    fun remove(transacao: Transacao)

    @Query("delete from Transacao")
    fun removeAll()

    @Update
    fun altera(transacao: Transacao)
}
