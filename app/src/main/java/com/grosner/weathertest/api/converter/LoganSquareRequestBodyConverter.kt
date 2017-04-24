package com.grosner.weathertest.api.converter

import com.bluelinelabs.logansquare.ConverterUtils.parameterizedTypeOf
import com.bluelinelabs.logansquare.LoganSquare
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Converter
import java.io.IOException
import java.lang.reflect.Type

internal class LoganSquareRequestBodyConverter(private val type: Type?) : Converter<Any, RequestBody> {

    @Throws(IOException::class)
    override fun convert(value: Any): RequestBody {
        // Check for generics
        if (type is java.lang.reflect.ParameterizedType) {
            val rawType = type.rawType
            if (rawType !== List::class.java && rawType !== Map::class.java) {
                return RequestBody.create(MEDIA_TYPE, LoganSquare.serialize<Any>(value,
                        parameterizedTypeOf(type)))
            }
        }

        // For general cases, use the central LoganSquare serialization method
        return RequestBody.create(MEDIA_TYPE, LoganSquare.serialize(value))
    }

    companion object {

        private val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")
    }
}
