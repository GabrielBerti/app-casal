package br.com.appcasal.dao.service

import br.com.appcasal.dao.dto.network.request.TransacaoRequestDTO
import br.com.appcasal.dao.dto.network.response.ResumoResponseDTO
import br.com.appcasal.dao.dto.network.response.TransacaoResponseDTO
import retrofit2.Response
import retrofit2.http.*

interface TransacaoService {

    @GET("/api/transacoes")
    suspend fun recuperaTransacoes(): Response<List<TransacaoResponseDTO>>

    @GET("/api/transacoes/recuperaResumo")
    suspend fun recuperaResumo(): Response<ResumoResponseDTO>

    @POST("/api/transacoes")
    suspend fun insereTransacao(@Body request: TransacaoRequestDTO): Response<TransacaoResponseDTO>

    @PUT("/api/transacoes/{id}")
    suspend fun alteraTransacao(
        @Body request: TransacaoRequestDTO,
        @Path("id") id: Long
    ): Response<TransacaoResponseDTO>

    @DELETE("/api/transacoes/{id}")
    suspend fun deletaTransacao(@Path("id") id: Long): Response<Void>

    @DELETE("/api/transacoes/deleteAll")
    suspend fun deletaTodasTransacoes(): Response<Void>
}
