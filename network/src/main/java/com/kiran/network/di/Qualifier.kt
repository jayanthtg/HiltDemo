package com.kiran.network.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CoroutineJsonNetworkProvider

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CoroutineXMLNetworkProvider