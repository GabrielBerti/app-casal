package br.com.appcasal.dao

import androidx.room.*
import br.com.appcasal.domain.model.Ingrediente

@Dao
interface IngredienteDAO {

    @Query("SELECT * FROM Ingrediente WHERE receitaId == :receitaId")
    fun buscaIngredientesByReceita(receitaId: Long) : MutableList<Ingrediente>

    @Query("DELETE FROM Ingrediente WHERE receitaId == :receitaId")
    fun deleteIngredientesByReceita(receitaId: Long)

    @Query("UPDATE Ingrediente SET marcado = 0")
    fun desmarcaTodosIngredientes()

    @Insert
    fun adiciona(ingrediente: Ingrediente)

    @Delete
    fun remove(ingrediente: Ingrediente)

    @Update
    fun altera(ingrediente: Ingrediente)
}