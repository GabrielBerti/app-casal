package br.com.appcasal.dao.service

import br.com.appcasal.dao.dto.network.request.MetaRequestDTO
import br.com.appcasal.dao.dto.network.request.MobileRequestDTO
import br.com.appcasal.dao.dto.network.response.MetaResponseDTO
import br.com.appcasal.dao.dto.network.response.MobileResponseDTO
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface MetaService {

    @GET("/api/metas")
    suspend fun recuperaMetas(): Response<List<MetaResponseDTO>>

    @GET("/api/metas/filterByConcluidas/{concluidas}")
    suspend fun recuperaMetasByConcluidas(@Path("concluidas") concluidas: Boolean): Response<List<MetaResponseDTO>>

    @POST("/api/metas")
    suspend fun insereMeta(@Body request: MetaRequestDTO): Response<MetaResponseDTO>

    @PUT("/api/metas/{id}")
    suspend fun alteraMeta(
        @Body request: MetaRequestDTO,
        @Path("id") id: Long
    ): Response<MetaResponseDTO>

    @DELETE("/api/metas/{id}")
    suspend fun deletaMeta(@Path("id") id: Long): Response<Void>
}
