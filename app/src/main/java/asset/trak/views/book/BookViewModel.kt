package asset.trak.views.book

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import asset.trak.database.entity.BookAttributes
import asset.trak.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val mbookRepository: BookRepository) : ViewModel() {
    private  var mGetBookResponse: List<BookAttributes>?=null

//    fun getAllBooks(): List<BookAttributes>? {
//        viewModelScope.launch {
//            mGetBookResponse= mbookRepository.getBookData()
//            Log.d("SampleAPI", "data1viewmodel::${mGetBookResponse}")
//        }
//        return mGetBookResponse
//    }
}