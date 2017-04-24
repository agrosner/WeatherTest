package com.grosner.weathertest.api.converter

import com.bluelinelabs.logansquare.ConverterUtils.parameterizedTypeOf
import com.bluelinelabs.logansquare.LoganSquare
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class LoganSquareResponseBodyConverter(private val type: Type?) : Converter<ResponseBody, Any> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): Any? {
        value.use { value ->
            // do not convert response body.
            if (type is Class<*>) {
                val name = type.name
                val other = ResponseBody::class.java.name
                if (name == other) {
                    return value
                }
            }

            val bs = value.source()
            bs.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
            val buffer = bs.buffer()
            //Clone the buffer and set it as input stream, so we do not wipe the actual error body
            val stream = buffer.clone().inputStream()
            if (type is Class<*>) {
                // Plain object conversion
                return LoganSquare.parse(stream, type)

            } else if (type is ParameterizedType) {
                val parameterizedType = type
                val typeArguments = parameterizedType.actualTypeArguments
                val firstType = typeArguments[0]

                val rawType = parameterizedType.rawType
                if (rawType === Map::class.java) {
                    return LoganSquare.parseMap(stream, typeArguments[1] as Class<*>)

                } else if (rawType === List::class.java) {
                    return LoganSquare.parseList(stream, firstType as Class<*>)

                } else {
                    // Generics
                    return LoganSquare.parse(stream, parameterizedTypeOf(type))
                }
            }
            return null
        }
    }
}
