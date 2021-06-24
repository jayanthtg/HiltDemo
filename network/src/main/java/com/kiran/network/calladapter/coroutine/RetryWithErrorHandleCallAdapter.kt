package com.kiran.network.calladapter.coroutine

import retrofit2.Call
import retrofit2.CallAdapter

class RetryWithErrorHandleCallAdapter<R, T>(
    private val callAdapter: CallAdapter<R, T>,
    private val maxRetries: Int = 0
) : CallAdapter<R, T> by callAdapter {

    override fun adapt(call: Call<R>): T {
        return callAdapter.adapt(RetryWithErrorHandlingCall(call, maxRetries))
    }
}