package asset.trak.views.fragments

import android.os.Bundle
import android.view.View
import asset.trak.database.daoModel.BookAndAssetData
import asset.trak.utils.Constants

import asset.trak.views.baseclasses.BaseFragment
import com.bumptech.glide.Glide
import com.markss.rfidtemplate.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_result.*

@AndroidEntryPoint
class ResultFragment : BaseFragment(R.layout.fragment_result) {
    private var listBook = ArrayList<BookAndAssetData>()
    var bookAttributes:BookAndAssetData?=null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()
        btnStartSearching.setOnClickListener {
            getBackToPreviousFragment()
        }

    }


    private fun setData() {
        val bundle = arguments
        if (bundle != null) {
            val   bookAttributes = bundle.getParcelable<BookAndAssetData>(Constants.BookData)
            tvTitleBook.text=bookAttributes?.assetCatalogue?.assetName
            tvAuthor.text=bookAttributes?.bookAttributes!!.author
            Glide.with(requireContext())
                .load(bookAttributes?.assetCatalogue!!.imagePath)
                .placeholder(R.color.light_gray)
                .into(ivBook)
        }



    }



}



