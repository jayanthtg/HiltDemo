package com.kiran.network.calladapter.coroutine

import com.kiran.network.calladapter.Retry
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class RetryWithErrorHandleCallAdapterFactory : CallAdapter.Factory() {
    private var retryValue: Int = 0

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java
            || returnType !is ParameterizedType
            || returnType.actualTypeArguments.size != 1) {
            return null
        }
        getRetry(annotations)?.let {
            retryValue = it.value
        }
        val adapter = retrofit.nextCallAdapter(this, returnType, annotations)
        return RetryWithErrorHandleCallAdapter(
            adapter, retryValue
        )
    }

    private fun getRetry(annotations: Array<out Annotation>) =
        annotations.filterIsInstance<Retry>()
            .firstOrNull()
}