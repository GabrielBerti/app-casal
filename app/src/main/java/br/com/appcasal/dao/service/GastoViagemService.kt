package br.com.appcasal.dao.service

import br.com.appcasal.dao.dto.network.request.GastoViagemRequestDTO
import br.com.appcasal.dao.dto.network.response.GastoViagemResponseDTO
import retrofit2.Response
import retrofit2.http.*

interface GastoViagemService {

    @GET("/api/gastos-viagem/{viagemId}")
    suspend fun recuperaGastosViagemByViagemId(
        @Path("viagemId") viagemId: Long
    ): Response<List<GastoViagemResponseDTO>>

    @POST("/api/gastos-viagem")
    suspend fun insereGastosViagem(@Body request: GastoViagemRequestDTO): Response<GastoViagemResponseDTO>

    @PUT("/api/gastos-viagem/{id}")
    suspend fun alteraGastosViagem(
        @Body request: GastoViagemRequestDTO,
        @Path("id") id: Long
    ): Response<GastoViagemResponseDTO>

    @DELETE("/api/gastos-viagem/{id}")
    suspend fun deletaGastosViagem(@Path("id") id: Long): Response<Void>
}
