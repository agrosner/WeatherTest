package com.bluelinelabs.logansquare

import com.grosner.weathertest.api.converter.LoganSquareConverterFactory
import okhttp3.ResponseBody
import java.lang.reflect.Type

/**
 * Utility class for the [LoganSquareConverterFactory]. This resides in LoganSquare's
 * main package in order to take advantage of the package-visible ConcreteParameterizedType class, which is essential
 * to the support of generic classes in the Retrofit converter.
 */
object ConverterUtils {

    fun isSupported(type: Type?): Boolean {
        if (type is Class<*> && ResponseBody::class.java == type) {
            return true
        }
        // Check ordinary Class
        if (type is Class<*> && !LoganSquare.supports(type)) {
            return false
        }

        // Check LoganSquare's ParameterizedType
        if (type is ParameterizedType<*> && !LoganSquare.supports(type)) {
            return false
        }

        // Check target types of java.lang.reflect.ParameterizedType
        if (type is java.lang.reflect.ParameterizedType) {
            val pt = type
            val typeArguments = pt.actualTypeArguments
            val firstType = typeArguments[0]

            val rawType = pt.rawType
            if (rawType === Map::class.java) {
                // LoganSquare only handles Map objects with String keys and supported types
                val secondType = typeArguments[1]
                if (firstType !== String::class.java || !isSupported(secondType)) {
                    return false
                }

            } else if (rawType === List::class.java) {
                // LoganSquare only handles List objects of supported types
                if (!isSupported(firstType)) {
                    return false
                }

            } else {
                // Check for generics
                return LoganSquare.supports(parameterizedTypeOf(type))
            }
        }

        return true
    }

    fun parameterizedTypeOf(type: Type): ParameterizedType<Any> {
        return ParameterizedType.ConcreteParameterizedType<Any>(type)
    }
}