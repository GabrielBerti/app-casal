package br.com.appcasal.util.extension

import java.text.SimpleDateFormat
import java.util.*

fun Calendar.formataParaBrasileiro() : String{
    val formatoBrasileiro = "dd/MM/yyyy"
    val format = SimpleDateFormat(formatoBrasileiro)
    return format.format(this.time)
}

fun Calendar.adicionaHorasFuso(): Calendar {
    val defaultTimeZone = TimeZone.getDefault()

    // Obtenha o deslocamento em milissegundos entre o fuso horário e o UTC
    val offsetInMillis = this.timeInMillis.let { defaultTimeZone.getOffset(it) }
    val offsetInHours = offsetInMillis.div((1000 * 60 * 60))

    if (offsetInHours < 0) {
        this.add(Calendar.HOUR_OF_DAY, offsetInHours * -1)
    }

    return this
}
