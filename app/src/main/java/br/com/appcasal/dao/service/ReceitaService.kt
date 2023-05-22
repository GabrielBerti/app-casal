package br.com.appcasal.dao.service

import br.com.appcasal.dao.dto.network.request.ReceitaRequestDTO
import br.com.appcasal.dao.dto.network.response.ReceitaResponseDTO
import retrofit2.Response
import retrofit2.http.*

interface ReceitaService {

    @GET("/api/receitas")
    suspend fun recuperaReceitas(@Query("search") search: String?): Response<List<ReceitaResponseDTO>>

    @POST("/api/receitas")
    suspend fun insereReceita(@Body request: ReceitaRequestDTO): Response<ReceitaResponseDTO>

    @PUT("/api/receitas/{id}")
    suspend fun alteraReceita(
        @Body request: ReceitaRequestDTO,
        @Path("id") id: Long
    ): Response<ReceitaResponseDTO>

    @DELETE("/api/receitas/{id}")
    suspend fun deletaReceita(@Path("id") id: Long): Response<Void>
}
