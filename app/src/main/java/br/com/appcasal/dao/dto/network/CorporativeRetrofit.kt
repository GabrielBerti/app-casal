package br.com.appcasal.dao.dto.network


import android.content.Context
import com.google.gson.*
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


private class AddHeaderInterceptor(val context: Context, val apiKey: String? = null) : Interceptor {
    private val mockAuthorization =
        "{\"ownership\": \"1\",\"currentAccount\": \"51\",\"cooperative\": \"3105\",\"password\": \"88888888\"}"

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

class CalendarTypeAdapter : TypeAdapter<Calendar?>() {
    private var format: DateFormat? = null

    init {
        format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        (format as SimpleDateFormat).timeZone = TimeZone.getTimeZone("UTC")
    }

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: Calendar?) {
        if (value == null) {
            out.nullValue()
        } else {
            val dateString = format?.format(value.time)
            out.value(dateString)
        }
    }

    @Throws(IOException::class)
    override fun read(inn: JsonReader): Calendar? {
        if (inn.peek() == JsonToken.NULL) {
            inn.nextNull()
            return null
        }
        return try {
            val dateString = inn.nextString()
            val calendar = Calendar.getInstance()
            calendar.timeZone = TimeZone.getTimeZone("UTC")
            calendar.time = format?.parse(dateString)
            calendar
        } catch (e: ParseException) {
            throw IOException(e)
        }
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
                .registerTypeAdapter(Calendar::class.java, CalendarTypeAdapter())
                .create()

        retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gsonConfig))
            .build()
    }
}
