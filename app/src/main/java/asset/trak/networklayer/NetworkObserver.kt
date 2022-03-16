package asset.trak.networklayer

import org.json.JSONObject

interface NetworkObserver<T> {

    /**
     * Invoked if the server rejected the request
     */
    fun onError(responseCode: Int, errorBody: JSONObject?)

    /**
     * Invoked if the request could not be completed (e.g., DNS error, Timeout)
     */
    fun onException(t: Throwable)

    /**
     * Invoked if the request was successful.
     */
    fun onSuccess(data: T?)

}
