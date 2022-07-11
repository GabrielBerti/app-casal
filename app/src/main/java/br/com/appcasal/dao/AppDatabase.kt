package br.com.appcasal.dao

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.appcasal.dao.converter.Converters
import br.com.appcasal.model.Meta
import br.com.appcasal.model.Transacao

@Database(entities = [Transacao::class, Meta::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transacaoDao(): TransacaoDAO

    abstract fun metaDao(): MetaDAO

    companion object {
        fun instancia(context: Context): AppDatabase {
            val MIGRATION_1_2: Migration = object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    // Remove a tabela antiga
                   // database.execSQL("ALTER TABLE transacao DROP COLUMN categoria")
                    //database.execSQL("ALTER TABLE transacao ADD COLUMN fsdf TEXT")
                    // cria nova tabela
                    database.execSQL(
                        "CREATE TABLE Meta (id INTEGER NOT NULL, descricao TEXT NOT NULL, concluido INTEGER NOT NULL, " +
                                "PRIMARY KEY(id))"
                    )
                }
            }

            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "app_casal.db"
            ).allowMainThreadQueries()
                .addMigrations(MIGRATION_1_2)
                .build()
        }
    }
}