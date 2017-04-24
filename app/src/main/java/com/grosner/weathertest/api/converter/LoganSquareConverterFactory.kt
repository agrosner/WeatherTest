package com.grosner.weathertest.api.converter


import com.bluelinelabs.logansquare.ConverterUtils.isSupported
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


/**
 * A [converter][Converter.Factory] which uses LoganSquare for JSON.

 * @see <a>https://github.com/bluelinelabs/LoganSquare</a>
 */
class LoganSquareConverterFactory private constructor() : Converter.Factory() {

    override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?,
                                       retrofit: Retrofit?): Converter<ResponseBody, *>? {
        if (type is Class<*> && String::class.java.name == type.name) {
            return Converter<ResponseBody, String>(ResponseBody::string)
        }
        return if (isSupported(type)) LoganSquareResponseBodyConverter(type) else null
    }

    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<Annotation>?,
                                      methodAnnotations: Array<Annotation>?,
                                      retrofit: Retrofit?): Converter<*, RequestBody>? {
        return if (isSupported(type)) LoganSquareRequestBodyConverter(type) else null
    }

    companion object {
        /**
         * Create an instance. Encoding to JSON and decoding from JSON will use UTF-8.

         * @return A [Converter.Factory] configured to serve LoganSquare converters
         */
        fun create() = LoganSquareConverterFactory()
    }
}
