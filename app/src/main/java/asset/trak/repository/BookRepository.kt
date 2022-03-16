package asset.trak.repository

import android.provider.Settings
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import asset.trak.database.BookDao
import asset.trak.database.entity.BookAttributes
import asset.trak.modelsrrtrack.LastSyncData
import asset.trak.modelsrrtrack.LastSyncResponse
import asset.trak.networklayer.AssetTrakAPIInterface
import com.markss.rfidtemplate.application.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton


private const val TAG = "BookRepository"

@Singleton
class BookRepository @Inject constructor(
    private var bookDao: BookDao,var assetTrakAPIInterface: AssetTrakAPIInterface
) {

    private lateinit var mbookResponse: List<BookAttributes>
    var mTakePickAction = MutableLiveData<String>()

    var mLastSync = MutableLiveData<LastSyncResponse>()
    var mAssetSync = MutableLiveData<Int>()
    private val mRetrofitException = MutableLiveData<Boolean>()


    suspend fun getBookData(): List<BookAttributes> {

        withContext(Dispatchers.Main)
        {
            try {
//               if (database != null) {
//                   bookDao = database.getBookDao()
//               }
                // mbookResponse = bookDao.getBooks()
            } catch (error: Throwable) {
                Log.d("SampleAPI", "data1::${error}")
            }
        }
        return mbookResponse
    }


    suspend fun getLastSync(syncTime: String?): LiveData<LastSyncResponse>? {

        try {
         //   val android_id = Settings.Secure.getString(Application.context.contentResolver, Settings.Secure.ANDROID_ID);

            val hashMap = HashMap<String, String>()
           // hashMap["LastSyncDateTime"] = syncTime.toString()
            hashMap["LastSyncDateTime"]="2022-03-08"
            //hashMap["CurrentDateTime"] = "2022-02-08"
          //  hashMap["MacId"] = android_id

            val response = assetTrakAPIInterface.geLastSync(hashMap)
            if (!response.isSuccessful) {
                return mLastSync
            }
            val data = response.body()
            mLastSync.value = data!!
            return mLastSync
        } catch (error: Throwable) {
            retrofitExceptionHandling(error)
        }
        return null
    }


    suspend fun postAssetSync(body:RequestBody): LiveData<Int>? {

        try {
            val response = assetTrakAPIInterface.posAssetSync(body)
            mAssetSync.value=response.code()
            return mAssetSync
        } catch (error: Throwable) {
            retrofitExceptionHandling(error)
        }
        return null
    }



    fun retrofitExceptionHandling(error: Throwable) {
        Log.e(TAG, "Exception: $error")
        when (error) {
            is SocketTimeoutException -> {
                mRetrofitException.postValue(true)
            }
            is IOException -> {
                mRetrofitException.postValue(true)
            }
            is Exception -> {
                mRetrofitException.postValue(true)
            }
        }
    }


}


