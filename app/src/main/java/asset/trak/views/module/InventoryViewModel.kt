package asset.trak.views.module

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import asset.trak.database.entity.BookAttributes
import asset.trak.modelsrrtrack.LastSyncData
import asset.trak.modelsrrtrack.LastSyncResponse
import asset.trak.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(private val bookRepository: BookRepository) :
    ViewModel() {
    private var mLastSyncData = MutableLiveData<LastSyncResponse>()
    private var mAssetSyncData = MutableLiveData<Int>()
     var listBookAttributes:ArrayList<BookAttributes> = ArrayList()

    fun getLastSync(syncTime: String?): LiveData<LastSyncResponse> {
        viewModelScope.launch {
            val data = bookRepository.getLastSync(syncTime)
            data?.apply {
                mLastSyncData.value = this.value
            }
        }
        return mLastSyncData
    }

    fun postAssetSync(body: RequestBody): LiveData<Int> {
        viewModelScope.launch {
            val data = bookRepository.postAssetSync(body)
            data?.apply {
                mAssetSyncData.value = this.value
            }
        }
        return mAssetSyncData
    }
}