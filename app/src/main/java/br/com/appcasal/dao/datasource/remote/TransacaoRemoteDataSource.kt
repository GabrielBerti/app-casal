package br.com.appcasal.dao.datasource.remote

import br.com.appcasal.dao.datasource.CallableDataSource
import br.com.appcasal.dao.dto.network.request.TransacaoRequestDTO
import br.com.appcasal.dao.dto.network.response.ResumoResponseDTO
import br.com.appcasal.dao.dto.network.response.TransacaoResponseDTO
import br.com.appcasal.dao.service.TransacaoService
import br.com.appcasal.domain.model.Transacao
import java.math.BigDecimal

class TransacaoRemoteDataSource(
    private val transacaoService: TransacaoService
): CallableDataSource {

    suspend fun recuperaTransacoes(): List<TransacaoResponseDTO> {
        val dto = transacaoService.recuperaTransacoes()
        return dto.body() ?: emptyList()
    }

    suspend fun recuperaResumo(): ResumoResponseDTO {
        val dto = transacaoService.recuperaResumo()
        return dto.body() ?: ResumoResponseDTO(BigDecimal.ZERO, BigDecimal.ZERO)
    }

    suspend fun insereTransacao(transacao: Transacao): TransacaoResponseDTO {
        val dto = transacaoService.insereTransacao(TransacaoRequestDTO.mapFrom(transacao))
        return dto.body() ?: TransacaoResponseDTO()
    }

    suspend fun alteraTransacao(transacao: Transacao): TransacaoResponseDTO {
        val dto = transacaoService.alteraTransacao(TransacaoRequestDTO.mapFrom(transacao), transacao.id)
        return dto.body() ?: TransacaoResponseDTO()
    }

    suspend fun deletaTransacao(transacao: Transacao): Boolean {
        transacaoService.deletaTransacao(transacao.id)
        return true
    }

    suspend fun deletaTodasTransacoes(): Boolean {
        transacaoService.deletaTodasTransacoes()
        return true
    }
}
