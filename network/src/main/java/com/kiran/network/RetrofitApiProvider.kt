package com.kiran.network

inline fun <reified T> buildAPI(networkKit: INetworkKit, hostName: String): T {
    return networkKit.provideRetrofit(hostName)
        .create(T::class.java)
}