package asset.trak.networklayer.retrofitutils

import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class JSONConverterFactory private constructor() : Converter.Factory() {
    override fun requestBodyConverter(type: Type, parameterAnnotations: Array<Annotation>,
            methodAnnotations: Array<Annotation>, retrofit: Retrofit): Converter<*, RequestBody>? {

        var converter: Converter<*, RequestBody>? = null
        if (type === JSONArray::class.java || type === JSONObject::class.java) {
            converter = JSONRequestBodyConverter.INSTANCE
        }
        return converter
    }

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>,
            retrofit: Retrofit): Converter<ResponseBody, *>? {
        var converter: Converter<ResponseBody, *>? = null
        if (type === JSONArray::class.java) {
            converter = JSONResponseBodyConverters.JSONArrayResponseBodyConverter.INSTANCE
        } else if (type === JSONObject::class.java) {
            converter = JSONResponseBodyConverters.JSONObjectResponseBodyConverter.INSTANCE
        }
        return converter
    }



    companion object {
        /**
         * A [converter][Converter.Factory] for JSONArray and JSONObject types
         * to `ApplicationRfid/json` bodies.
         */
        fun create(): JSONConverterFactory {
            return JSONConverterFactory()
        }
    }
}
