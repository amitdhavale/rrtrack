package asset.trak.networklayer.retrofitutils

import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Converter
import java.io.IOException

internal class JSONResponseBodyConverters private constructor() {

    internal class JSONArrayResponseBodyConverter : Converter<ResponseBody, JSONArray> {
        @Throws(IOException::class)
        override fun convert(value: ResponseBody): JSONArray {
            return try {
                JSONArray(value.string())
            } catch (ex: JSONException) {
                throw IOException("Could not parse JSONArray")
            }
        }

        companion object {
            @kotlin.jvm.JvmField
            val INSTANCE = JSONArrayResponseBodyConverter()
        }
    }

    internal class JSONObjectResponseBodyConverter : Converter<ResponseBody, JSONObject> {
        @Throws(IOException::class)
        override fun convert(value: ResponseBody): JSONObject {
            return try {
                JSONObject(value.string())
            } catch (ex: JSONException) {
                throw IOException("Could not parse JSONObject")
            }
        }

        companion object {
            @kotlin.jvm.JvmField
            val INSTANCE = JSONObjectResponseBodyConverter()
        }
    }
}
