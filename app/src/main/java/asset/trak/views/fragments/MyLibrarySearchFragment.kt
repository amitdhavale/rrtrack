package asset.trak.views.fragments

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigator
import asset.trak.database.daoModel.BookAndAssetData
import asset.trak.modelsrrtrack.AssetMain
import asset.trak.utils.Constants
import asset.trak.views.adapter.OnResultClickListener
import asset.trak.views.adapter.ResultAdapter
import asset.trak.views.baseclasses.BaseFragment
import asset.trak.views.module.InventoryViewModel
import com.markss.rfidtemplate.R
import com.markss.rfidtemplate.application.Application
import com.markss.rfidtemplate.locate_tag.LocateOperationsFragment
import com.markss.rfidtemplate.locate_tag.SingleTagLocateFragment
import com.markss.rfidtemplate.rfid.RFIDController
import kotlinx.android.synthetic.main.fragment_my_library_search.*



class MyLibrarySearchFragment : BaseFragment(R.layout.fragment_my_library_search),
    OnResultClickListener {

    private lateinit var resultAdapter: ResultAdapter
    private var listBook = ArrayList<AssetMain>()
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivBack.setOnClickListener {
            getBackToPreviousFragment()
        }
        setAdaptor()
        searchView.queryHint = Html.fromHtml("<font color = #D3D3D3>" + getResources().getString(R.string.search) + "</font>");

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                resultAdapter.filter.filter(newText)
                return true
            }
        })

    }

    override fun onGoalClick(bookAttributes: AssetMain) {

        Application.locateTag = bookAttributes.AssetRFID
        RFIDController.accessControlTag =bookAttributes.AssetRFID
        Application.PreFilterTag =bookAttributes.AssetRFID
        Application.comefrom ="show"



        replaceFragment(requireActivity().supportFragmentManager, LocateOperationsFragment(), R.id.content_frame)
    }

    private fun setAdaptor() {
        listBook.clear()
        listBook.addAll(Application.roomDatabaseBuilder?.getBookDao()?.getBooks() ?: emptyList())

//        for (i in 0 until listBook.size)  {
//            var locationName=Application.roomDatabaseBuilder?.getBookDao()?.getLocationName(listBook.get(i).assetCatalogue.locationId?:0)
//            listBook[i].assetCatalogue?.locationName= locationName?.locationName?:""
//            var category=Application.roomDatabaseBuilder?.getBookDao()?.getCatgeoryName(listBook.get(i).assetCatalogue.categoryId?:0)
//            listBook[i].assetCatalogue?.categoryName= category?.categoryName?:""
//            var subcategory=Application.roomDatabaseBuilder?.getBookDao()?.getSubCatgeoryName(listBook.get(i).assetCatalogue.subCategoryId?:0)
//            listBook[i].assetCatalogue?.categoryName= subcategory?.subCategoryName?:""
//
//        }



//        Log.e("ss",""+listBook[0].assetCatalogue.imagePathFile)

        resultAdapter = ResultAdapter(requireContext(), this, listBook,true)
        rvResult.adapter = resultAdapter
    }


}


