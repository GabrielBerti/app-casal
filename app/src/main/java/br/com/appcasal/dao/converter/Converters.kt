package br.com.appcasal.dao.converter

import androidx.room.TypeConverter
import br.com.appcasal.domain.model.Tipo
import java.math.BigDecimal

class Converters {

    @TypeConverter
    fun deDouble(valor: Double?) : BigDecimal {
        return valor?.let { BigDecimal(valor.toString()) } ?: BigDecimal.ZERO
    }

    @TypeConverter
    fun deBigDecimalParaDouble(valor: BigDecimal?) : Double? {
        return valor?.let{ valor.toDouble() }
    }

    @TypeConverter
    fun deString(valor: String?) : Tipo {
        return if(valor == "BIEL") { Tipo.BIEL } else { Tipo.MARI }
    }

    @TypeConverter
    fun tipoParaString(tipo: Tipo?) : String {
        return if(tipo == Tipo.BIEL) { "BIEL" } else { "MARI" }
    }

    @TypeConverter
    fun deInteger(valor: Int?) : Boolean {
        return valor != 0
    }

    @TypeConverter
    fun booleanParaInteger(valor: Boolean) : Int {
        return if(valor) { 1 } else { 0 }
    }

}