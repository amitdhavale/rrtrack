package asset.trak.networklayer

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkCapabilities.NET_CAPABILITY_NOT_METERED
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "NetworkMonitor"

/**
 * A simple class that monitors the state of the network and provides LiveData
 * updates when the network and/or network capabilities change.
 */
@Singleton
class NetworkMonitor @Inject constructor(
    @ApplicationContext appContext: Context
) : LiveData<NetworkMonitor.NetworkType>() {

    private val mConnMgr = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val mHandler = Handler(Looper.getMainLooper())

    /**
     * The type of networks we track
     */
    enum class NetworkType {
        NONE, METERED, NONMETERED
    }

    private var mCurrentNetworkType = NetworkType.NONE
    private var mCurrentNetwork: Network? = null
    private var mPreviousNetwork: Network? = null
    private var mTimerRunning = true

    override fun onActive() {
        // Reset all state to the "initial" values
        mCurrentNetworkType = NetworkType.NONE
        mCurrentNetwork = null
        mPreviousNetwork = null
        mTimerRunning = true
        /*
        Testing shows that an (almost) immediate callback is generated if there is
        an active network. However, no callback is generated if there is no active
        network. To detect the "no network" condition without generating a false
        negative/positive event (i.e., set a default network state prior to a callback),
        we allow 500 ms to elapse before declaring there is no network. To ensure we
        don't have multi-thread race condition, we serialize the timeout and Network
        responses through the same handler.
        */
        mHandler.postDelayed(mTimeoutRunnable, 500L)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mConnMgr.registerDefaultNetworkCallback(mNetworkCallback, mHandler)
        } else {
            if (mTimerRunning) {
                mTimerRunning = false
                mHandler.removeCallbacks(mTimeoutRunnable)
            }
        }
    }

    override fun onInactive() {
        if (mTimerRunning) {
            mTimerRunning = false
            mHandler.removeCallbacks(mTimeoutRunnable)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        mConnMgr.unregisterNetworkCallback(mNetworkCallback)
    }

    private val mTimeoutRunnable = Runnable {
        mTimerRunning = false
        value = mCurrentNetworkType
    }

    private val mNetworkCallback = object: NetworkCallback() {
        override fun onAvailable(network: Network) {
            if (mTimerRunning) {
                mTimerRunning = false
                mHandler.removeCallbacks(mTimeoutRunnable)
            }
            mPreviousNetwork = mCurrentNetwork
            mCurrentNetwork = network
        }

        override fun onCapabilitiesChanged(network: Network, caps: NetworkCapabilities) {
            if (network == mCurrentNetwork) {
                val oldNetworkType = mCurrentNetworkType
                if (caps.hasCapability(NET_CAPABILITY_INTERNET)) {
                    if (caps.hasCapability(NET_CAPABILITY_NOT_METERED)) {
                        mCurrentNetworkType = NetworkType.NONMETERED
                    } else {
                        mCurrentNetworkType = NetworkType.METERED
                    }
                } else {
                    mCurrentNetwork = null
                    mCurrentNetworkType = NetworkType.NONE
                }
                if (mPreviousNetwork != mCurrentNetwork || oldNetworkType != mCurrentNetworkType) {
                    mPreviousNetwork = mCurrentNetwork
                    value = mCurrentNetworkType
                }
            }
        }

        override fun onLost(network: Network) {
            if (network == mCurrentNetwork) {
                mCurrentNetwork = null
                if (mCurrentNetworkType != NetworkType.NONE) {
                    mCurrentNetworkType = NetworkType.NONE
                    value = mCurrentNetworkType
                }
            }
        }
    }
}
