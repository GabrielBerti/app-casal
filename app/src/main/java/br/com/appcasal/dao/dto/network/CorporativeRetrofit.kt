package br.com.appcasal.dao.dto.network

import android.content.Context
import br.com.appcasal.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.reactivex.*
import okhttp3.*
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.jvm.Throws

private class AddHeaderInterceptor(val context: Context, val apiKey: String? = null) : Interceptor {
//  TODO temporary until solves authorization
    private val mockAuthorization= "{\"ownership\": \"1\",\"currentAccount\": \"51\",\"cooperative\": \"3105\",\"password\": \"88888888\"}"

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
//        val security = Security(context)
//        val token = security.retrieve(Security.PreferencesKey.USER_TOKEN)
//        if (token != "") {
//            builder.addHeader("Authorization", "Bearer $token")
//        }
//        if (apiKey != null) {
//            builder.addHeader("user-key", apiKey)
//        }
        //builder.addHeader("Authorization", mockAuthorization)
        return chain.proceed(builder.build())
    }

}

class JavaDateTypeAdapter : TypeAdapter<Date>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Date?) {
        if (value == null) out.nullValue() else out.value(value.time)
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader?): Date? {
        return if (`in` != null) {
            val vl = `in`.nextLong()
            Date(vl)
        } else null
    }
}

class CorporativeRetrofit(
    context: Context,
    baseUrl: String,
    gson: Gson? = null,
    apiKey: String? = null
) {
    val retrofit: Retrofit
    private val gsonConfig: Gson

    init {

        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client =
            OkHttpClient.Builder()
                .addInterceptor(AddHeaderInterceptor(context, apiKey))
                .addInterceptor(logInterceptor)
                .callTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build()

        gsonConfig = gson
            ?: GsonBuilder()
                .registerTypeAdapter(Date::class.java, JavaDateTypeAdapter())
                .create()

        retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gsonConfig))
            .build()
    }
}
