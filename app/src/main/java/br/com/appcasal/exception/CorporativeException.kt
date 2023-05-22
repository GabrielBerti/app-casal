package br.com.appcasal.exception

open class CorporativeException(
    override val message: String? = null,
    val code: Int? = null,
    cause: Throwable? = null): Exception(message, cause) {

    interface CorporativeExceptionFactory {
        fun toCorporativeException(cause: Throwable? = null): CorporativeException
    }

}
