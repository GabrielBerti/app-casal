package br.com.appcasal.dao

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.appcasal.dao.converter.Converters
import br.com.appcasal.model.Ingrediente
import br.com.appcasal.model.Meta
import br.com.appcasal.model.Receita
import br.com.appcasal.model.Transacao

@Database(entities = [Transacao::class, Meta::class, Receita::class, Ingrediente::class], version = 4)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transacaoDao(): TransacaoDAO
    abstract fun metaDao(): MetaDAO
    abstract fun receitaDao(): ReceitaDAO
    abstract fun ingredienteDao(): IngredienteDAO

    companion object {
        fun instancia(context: Context): AppDatabase {
            val MIGRATION_3_4: Migration = object : Migration(3, 4) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    // Remove a tabela antiga
                   // database.execSQL("ALTER TABLE transacao DROP COLUMN categoria")
                    //database.execSQL("ALTER TABLE transacao ADD COLUMN fsdf TEXT")
                    // cria nova tabela
                    database.execSQL(
                        "CREATE TABLE Meta (id INTEGER NOT NULL, descricao TEXT NOT NULL, concluido INTEGER NOT NULL, " +
                                "PRIMARY KEY(id))"
                    )

                    database.execSQL("DROP TABLE Receita")

                    database.execSQL(
                        "CREATE TABLE Receita (id INTEGER NOT NULL, nome TEXT NOT NULL, descricao TEXT NOT NULL, " +
                                "PRIMARY KEY(id))"
                    )

                    database.execSQL(
                        "CREATE TABLE Ingrediente (id INTEGER NOT NULL, descricao TEXT NOT NULL, receita_id INTEGER NOT NULL," +
                                "PRIMARY KEY(id) ,FOREIGN KEY(receita_id) REFERENCES Receita(id))"
                    )
                }
            }

            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "app_casal.db"
            ).allowMainThreadQueries()
                .addMigrations(MIGRATION_3_4)
                .build()
        }
    }
}