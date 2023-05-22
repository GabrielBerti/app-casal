package br.com.appcasal.dao.datasource

import br.com.appcasal.dao.dto.network.response.MobileResponseDTO
import retrofit2.Response

interface CallableDataSource {
    suspend fun <T> call(
        apiCall: suspend () -> Response<MobileResponseDTO<T>>
    ): MobileResponseDTO<T>? {
        val response = apiCall.invoke()
        if (!response.isSuccessful) {
            throw Exception()
        }

        return response.body()
    }
}
