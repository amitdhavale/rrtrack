package asset.trak.networklayer.retrofitutils

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Converter

internal class JSONRequestBodyConverter<T> private constructor() : Converter<T, RequestBody> {

    override fun convert(value: T): RequestBody {
        return value.toString().toRequestBody(MEDIA_TYPE)
    }

    companion object {
        @JvmField
        val INSTANCE = JSONRequestBodyConverter<Any>()
        private val MEDIA_TYPE = "ApplicationRfid/json; charset=UTF-8".toMediaType()
    }
}
