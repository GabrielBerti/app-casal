package br.com.appcasal.dao.repository

import br.com.appcasal.dao.datasource.remote.TransacaoRemoteDataSource
import br.com.appcasal.domain.model.Resumo
import br.com.appcasal.domain.model.Transacao

class TransacaoRepository(
    private val transacaoRemoteDataSource: TransacaoRemoteDataSource
) {
    suspend fun recuperaTransacoes(): List<Transacao> {
        val result = transacaoRemoteDataSource.recuperaTransacoes()
        return result.map { Transacao.mapFrom(it) }
    }

    suspend fun recuperaResumo(): Resumo {
        val result = transacaoRemoteDataSource.recuperaResumo()
        return Resumo.mapTo(result)
    }

    suspend fun insereTransacao(transacao: Transacao): Transacao {
        val result = transacaoRemoteDataSource.insereTransacao(transacao)
        return Transacao.mapFrom(result)
    }

    suspend fun alteraTransacao(transacao: Transacao): Transacao {
        val result = transacaoRemoteDataSource.alteraTransacao(transacao)
        return Transacao.mapFrom(result)
    }

    suspend fun deletaTransacao(transacao: Transacao): Boolean {
        return transacaoRemoteDataSource.deletaTransacao(transacao)
    }

    suspend fun deletaTodasTransacoes(): Boolean {
        return transacaoRemoteDataSource.deletaTodasTransacoes()
    }

}
