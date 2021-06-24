package com.kiran.network.calladapter.error

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ServerExceptionsMapper(val value: KClass<out HttpExceptionMapper>)