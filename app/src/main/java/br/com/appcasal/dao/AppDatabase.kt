package br.com.appcasal.dao

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.appcasal.dao.converter.Converters
import br.com.appcasal.model.*

@Database(
    entities = [Transacao::class, Meta::class, Receita::class, Ingrediente::class, Viagem::class, GastosViagem::class, LugaresVisitados::class],
    version = 6
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
            val MIGRATION_5_6: Migration = object : Migration(5, 6) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    // Remove a tabela antiga
                    // database.execSQL("ALTER TABLE transacao DROP COLUMN categoria")
                    //databas e.execSQL("ALTER TABLE transacao ADD COLUMN fsdf TEXT")
                    // cria nova tabela
                    /*database.execSQL(
                        "CREATE TABLE Meta (id INTEGER NOT NULL, descricao TEXT NOT NULL, concluido INTEGER NOT NULL, " +
                                "PRIMARY KEY(id))"
                    )*/

                    database.execSQL("DROP TABLE Receita")

                    database.execSQL(
                        "CREATE TABLE Receita (id INTEGER NOT NULL, nome TEXT NOT NULL, descricao TEXT NOT NULL, " +
                                "PRIMARY KEY(id))"
                    )

                   database.execSQL("DROP TABLE Ingrediente")
                   database.execSQL(
                       "CREATE TABLE Ingrediente (id INTEGER NOT NULL, descricao TEXT NOT NULL, marcado INTEGER NOT NULL, receitaId INTEGER NOT NULL, " +
                               "PRIMARY KEY(id), FOREIGN KEY(receitaId) REFERENCES Receita(id))"
                   )

                    database.execSQL("CREATE INDEX IF NOT EXISTS 'index_Ingrediente_receitaId' ON 'Ingrediente' ('receitaId')")

                    database.execSQL(
                        "CREATE TABLE GastosViagem (id INTEGER NOT NULL, valor REAL NOT NULL, descricao TEXT NOT NULL, viagemId INTEGER NOT NULL, " +
                                "PRIMARY KEY(id), FOREIGN KEY(viagemId) REFERENCES Viagem(id))"
                    )

                    database.execSQL("CREATE INDEX IF NOT EXISTS 'index_GastosViagem_viagemId' ON 'GastosViagem' ('viagemId')")


                    database.execSQL(
                        "CREATE TABLE LugaresVisitados (id INTEGER NOT NULL, nome TEXT NOT NULL, legal INTEGER NOT NULL, viagemId INTEGER NOT NULL, " +
                                "PRIMARY KEY(id), FOREIGN KEY(viagemId) REFERENCES Viagem(id))"
                    )

                    database.execSQL("CREATE INDEX IF NOT EXISTS 'index_LugaresVisitados_viagemId' ON 'LugaresVisitados' ('viagemId')")

                }
            }

            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "app_casal.db"
            ).allowMainThreadQueries()
                .addMigrations(MIGRATION_5_6)
                .build()
        }
    }
}