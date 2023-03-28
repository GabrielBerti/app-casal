package br.com.appcasal.dao.service

import br.com.appcasal.dao.dto.network.request.ViagemRequestDTO
import br.com.appcasal.dao.dto.network.response.ViagemResponseDTO
import retrofit2.Response
import retrofit2.http.*

interface ViagemService {

    @GET("/api/viagens")
    suspend fun recuperaViagens(): Response<List<ViagemResponseDTO>>

    @POST("/api/viagens")
    suspend fun insereViagem(@Body request: ViagemRequestDTO): Response<ViagemResponseDTO>

    @PUT("/api/viagens/{id}")
    suspend fun alteraViagem(
        @Body request: ViagemRequestDTO,
        @Path("id") id: Long
    ): Response<ViagemResponseDTO>

    @DELETE("/api/viagens/{id}")
    suspend fun deletaViagem(@Path("id") id: Long): Response<Void>
}
