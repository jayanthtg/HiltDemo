package com.kiran.network.calladapter.error

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ServerException(
    message: String?,
    val exception: Throwable,
    val response: Response<*>? = null,
    val kind: ExceptionKind
) : Exception(message) {

    companion object {
        fun httpError(exception: HttpException, response: Response<*>): ServerException {
            val message = response.code().toString() + " " + response.message()
            return ServerException(message, exception = exception, kind = ExceptionKind.HTTP, response = response)
        }

        fun networkError(exception: IOException): ServerException {
            return ServerException(exception.message, exception = exception, kind = ExceptionKind.NETWORK)
        }

        fun unexpectedError(exception: Throwable): ServerException {
            return ServerException(exception.message, exception = exception, kind = ExceptionKind.UNEXPECTED)
        }
    }

    /**
     * HTTP response body converted to specified `type`. `null` if there is no
     * response.
     *
     * @throws IOException if unable to convert the body to the specified `type`.
     */
    /*@Throws(IOException::class)
    fun <T> getErrorBodyAs(type: Class<T>, converter: Converter<ResponseBody, T>?): T? {
        if (response?.errorBody() == null) {
            return null
        }
        return converter?.convert(response.errorBody()!!)
    }*/
}