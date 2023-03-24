package br.com.appcasal.dao

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.appcasal.dao.converter.Converters
import br.com.appcasal.domain.model.*

@Database(
    entities = [Transacao::class, Meta::class, Receita::class, Ingrediente::class, Viagem::class, GastoViagem::class, LugarVisitado::class],
    version = 9
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transacaoDao(): TransacaoDAO
    abstract fun metaDao(): MetaDAO
    abstract fun receitaDao(): ReceitaDAO
    abstract fun ingredienteDao(): IngredienteDAO
    abstract fun viagemDao(): ViagemDAO
    abstract fun gastosViagemDao(): GastosViagemDAO
    abstract fun lugaresVisitadosDao(): LugaresVisitadosDAO

    companion object {
        fun instancia(context: Context): AppDatabase {
            val MIGRATION_8_9: Migration = object : Migration(8, 9) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    // Remove a tabela antiga
                    // database.execSQL("ALTER TABLE transacao DROP COLUMN categoria")
                    //databas e.execSQL("ALTER TABLE transacao ADD COLUMN fsdf TEXT")
                    // cria nova tabela
                    /*database.execSQL(
                        "CREATE TABLE Meta (id INTEGER NOT NULL, descricao TEXT NOT NULL, concluido INTEGER NOT NULL, " +
                                "PRIMARY KEY(id))"
                    )*/

                    database.execSQL(
                        "CREATE TABLE IF NOT EXISTS Receita (id INTEGER NOT NULL, nome TEXT NOT NULL, descricao TEXT NOT NULL, " +
                                "PRIMARY KEY(id))"
                    )

                   database.execSQL(
                       "CREATE TABLE IF NOT EXISTS Ingrediente (id INTEGER NOT NULL, descricao TEXT NOT NULL, marcado INTEGER NOT NULL, receitaId INTEGER NOT NULL, " +
                               "PRIMARY KEY(id), FOREIGN KEY(receitaId) REFERENCES Receita(id))"
                   )

                    database.execSQL("CREATE INDEX IF NOT EXISTS 'index_Ingrediente_receitaId' ON 'Ingrediente' ('receitaId')")

                    database.execSQL("DROP TABLE IF EXISTS Viagem")
                    database.execSQL(
                        "CREATE TABLE IF NOT EXISTS Viagem (id INTEGER NOT NULL, local TEXT NOT NULL, dataInicio TEXT NOT NULL, dataFim TEXT NOT NULL, nota INTEGER NOT NULL, " +
                                "PRIMARY KEY(id))"
                    )

                    database.execSQL(
                        "CREATE TABLE IF NOT EXISTS GastoViagem (id INTEGER NOT NULL, valor REAL NOT NULL, descricao TEXT NOT NULL, viagemId INTEGER NOT NULL, " +
                                "PRIMARY KEY(id), FOREIGN KEY(viagemId) REFERENCES Viagem(id))"
                    )

                    database.execSQL("CREATE INDEX IF NOT EXISTS 'index_GastoViagem_viagemId' ON 'GastoViagem' ('viagemId')")


                    database.execSQL(
                        "CREATE TABLE IF NOT EXISTS LugarVisitado (id INTEGER NOT NULL, nome TEXT NOT NULL, legal INTEGER NOT NULL, viagemId INTEGER NOT NULL, " +
                                "PRIMARY KEY(id), FOREIGN KEY(viagemId) REFERENCES Viagem(id))"
                    )

                    database.execSQL("CREATE INDEX IF NOT EXISTS 'index_LugarVisitado_viagemId' ON 'LugarVisitado' ('viagemId')")

                }
            }

            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "app_casal.db"
            ).allowMainThreadQueries()
                .addMigrations(MIGRATION_8_9)
                .build()
        }
    }
}