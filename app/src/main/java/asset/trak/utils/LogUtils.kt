package asset.trak.utils

import android.util.Log
/**
 * Create log handler
 * #logTag Pass log TAG
 * #message Pass log message
 */
object LogUtils {

    fun logV(logTag: String, message: String) {
        Log.v(logTag, message)
    }

    fun logD(logTag: String, message: String) {
        Log.d(logTag, message)
    }

    fun logI(logTag: String, message: String) {
        Log.i(logTag, message)
    }

    fun logW(logTag: String, message: String) {
        Log.w(logTag, message)
    }

    fun logE(logTag: String, message: String) {
        Log.e(logTag, message)
    }
}