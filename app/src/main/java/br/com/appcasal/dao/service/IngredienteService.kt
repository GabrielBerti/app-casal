package br.com.appcasal.dao.service

import br.com.appcasal.dao.dto.network.request.IngredienteRequestDTO
import br.com.appcasal.dao.dto.network.response.IngredienteResponseDTO
import retrofit2.Response
import retrofit2.http.*

interface IngredienteService {

    @GET("/api/ingredientes/{receitaId}")
    suspend fun recuperaIngredientesByReceitaId(
        @Path("receitaId") receitaId: Long
    ): Response<List<IngredienteResponseDTO>>

    @POST("/api/ingredientes")
    suspend fun insereIngrediente(@Body request: List<IngredienteRequestDTO>): Response<IngredienteResponseDTO>

    @PUT("/api/ingredientes/{id}")
    suspend fun alteraIngrediente(
        @Body request: IngredienteRequestDTO,
        @Path("id") id: Long
    ): Response<IngredienteResponseDTO>

    @DELETE("/api/ingredientes/{id}")
    suspend fun deletaIngrediente(@Path("id") id: Long): Response<Void>
}
