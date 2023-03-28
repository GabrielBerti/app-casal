package br.com.appcasal.dao.service

import br.com.appcasal.dao.dto.network.request.LugarVisitadoRequestDTO
import br.com.appcasal.dao.dto.network.response.LugarVisitadoResponseDTO
import retrofit2.Response
import retrofit2.http.*

interface LugarVisitadoService {

    @GET("/api/lugares-visitados/{viagemId}")
    suspend fun recuperaLugaresVisitadosByViagemId(
        @Path("viagemId") viagemId: Long
    ): Response<List<LugarVisitadoResponseDTO>>

    @POST("/api/lugares-visitados")
    suspend fun insereLugarVisitado(@Body request: LugarVisitadoRequestDTO): Response<LugarVisitadoResponseDTO>

    @PUT("/api/lugares-visitados/{id}")
    suspend fun alteraLugarVisitado(
        @Body request: LugarVisitadoRequestDTO,
        @Path("id") id: Long
    ): Response<LugarVisitadoResponseDTO>

    @DELETE("/api/lugares-visitados/{id}")
    suspend fun deletaLugarVisitado(@Path("id") id: Long): Response<Void>
}
