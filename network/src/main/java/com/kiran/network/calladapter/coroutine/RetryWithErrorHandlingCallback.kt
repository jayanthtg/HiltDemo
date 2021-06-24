package com.kiran.network.calladapter.coroutine

import com.kiran.network.calladapter.error.HttpExceptionMapper
import com.kiran.network.calladapter.error.ServerException
import com.kiran.network.calladapter.error.ServerExceptionsMapper
import retrofit2.*
import java.io.IOException
import java.util.concurrent.atomic.AtomicInteger

class RetryWithErrorHandlingCallback<T>(
    private val call: Call<T>,
    private val delegate: Callback<T>,
    private val maxRetries: Int): Callback<T> {
    private val retryCount: AtomicInteger = AtomicInteger(0)

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            delegate.onResponse(call, response)
        } else {
            handleFailure(call, HttpException(response))
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        handleFailure(call, t)
    }

    private fun handleFailure(call: Call<T>, t: Throwable) {
        if (isRetryableException(t) && retryCount.incrementAndGet() <= maxRetries) {
            retryCall()
        } else {
            delegate.onFailure(call, mapExceptionOfCall(call, t))
        }
    }

    /**
     * Discuss and Implement
     */
    private fun isRetryableException(throwable: Throwable): Boolean {
        /**
         * Kind == NETWORK
         *
         * Kind == HTTP -> 500,502,503,504,403,401
         */
        return true
    }

    private fun retryCall() {
        call.clone().enqueue(this)
    }

    /**
     * Handling the annotation process 'ExceptionsMapper'
     */
    private fun mapExceptionOfCall(call: Call<T>, t: Throwable): Exception {
        val retrofitInvocation = call.request().tag(Invocation::class.java)
        val annotation =
            retrofitInvocation?.method()?.getAnnotation(ServerExceptionsMapper::class.java)
        val mapper = try {
            annotation?.value?.java?.constructors?.first()
                ?.newInstance(retrofitInvocation.arguments()) as HttpExceptionMapper
        } catch (e: Exception) {
            null
        }
        return mapToServerException(t, mapper)
    }

    private fun mapToServerException(
        remoteException: Throwable,
        httpExceptionsMapper: HttpExceptionMapper? = null
    ): Exception {
        return when (remoteException) {
            is IOException -> ServerException.networkError(remoteException)
            is HttpException -> httpExceptionsMapper?.map(remoteException)
                ?: getServerException(remoteException)
            else -> ServerException.unexpectedError(remoteException)
        }
    }

    private fun getServerException(httpException: HttpException): Exception {
        val response = httpException.response()
        response?.let {
            return ServerException.httpError(httpException, response)
        }
        return ServerException.unexpectedError(httpException)
    }
}