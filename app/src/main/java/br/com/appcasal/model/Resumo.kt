package br.com.appcasal.model


import java.math.BigDecimal

class Resumo(private val transacoes: List<Transacao>) {

    val saldoBiel get() = somaPor(Tipo.BIEL)

    val saldoMari get() = somaPor(Tipo.MARI)

    val total get() = saldoBiel.subtract(saldoMari)

    private fun somaPor(tipo: Tipo) : BigDecimal {
        var somaDeTransacoesPeloTipo = transacoes
                .filter { it.tipo == tipo }
                .sumByDouble { it.valor.toDouble() }

        /*if(tipo.name.equals("DESPESA")){
            val tipoReceita: Tipo = Tipo.RECEITA

            val somaDeTransacoesReceita = transacoes
                .filter { it.tipo == tipoReceita }
                .sumByDouble { it.valor.toDouble() }

            if(somaDeTransacoesReceita >= 0){
                somaDeTransacoesPeloTipo = somaDeTransacoesReceita - somaDeTransacoesPeloTipo
            }  else {
                somaDeTransacoesPeloTipo = somaDeTransacoesReceita - somaDeTransacoesPeloTipo
            }

        }*/
        return BigDecimal(somaDeTransacoesPeloTipo)
    }
}