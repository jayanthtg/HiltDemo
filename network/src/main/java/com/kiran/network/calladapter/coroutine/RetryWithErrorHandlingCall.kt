package com.kiran.network.calladapter.coroutine

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetryWithErrorHandlingCall<R>(
    private val delegated: Call<R>,
    private val maxRetries: Int
): Call<R> by delegated {

    override fun execute(): Response<R> {
        return delegated.execute()
    }

    override fun enqueue(callback: Callback<R>) {
        delegated.enqueue(RetryWithErrorHandlingCallback(delegated, callback, maxRetries))
    }

    override fun clone(): Call<R> {
        return RetryWithErrorHandlingCall(delegated.clone(), maxRetries)
    }
}