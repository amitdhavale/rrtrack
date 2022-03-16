package asset.trak.networklayer.retrofitutils

import org.json.JSONException
import org.json.JSONObject

object RetrofitUtils {

    fun convertStringToJsonObject(string: String?) : JSONObject? {
        var json: JSONObject? = null
        if (string != null) {
            try {
                json = JSONObject(string)
            } catch (ex: JSONException) {
                // error body was not a JSONObject; just return null
            }
        }
        return json
    }
}