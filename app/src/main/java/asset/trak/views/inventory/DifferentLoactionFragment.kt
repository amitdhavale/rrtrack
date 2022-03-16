package asset.trak.views.inventory

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import asset.trak.database.daoModel.BookAndAssetData
import asset.trak.database.entity.AssetCatalogue
import asset.trak.modelsrrtrack.AssetMain
import asset.trak.views.adapter.DifferntLocationAdapter
import com.markss.rfidtemplate.R
import com.markss.rfidtemplate.application.Application
import com.markss.rfidtemplate.application.Application.bookDao
import com.markss.rfidtemplate.application.Application.roomDatabaseBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_different_loaction.*
import kotlinx.android.synthetic.main.fragment_different_loaction.rvNotFound
import kotlinx.android.synthetic.main.fragment_different_loaction.searchView

@AndroidEntryPoint
class DifferentLoactionFragment(val locationId: Int) :
    Fragment(R.layout.fragment_different_loaction) {
    private lateinit var notFoundAdapter: DifferntLocationAdapter

    var listBook = ArrayList<AssetMain>()
    override fun onResume() {
        super.onResume()
        setAdaptor()
    }

    override fun onPause() {
        super.onPause()
        Log.d("DifferentLoactionFragment", "onPause: ")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdaptor()

        searchView.queryHint = Html.fromHtml("<font color = #D3D3D3>" + getResources().getString(R.string.search) + "</font>");

        tvSelectAll.setOnClickListener {
            if(tvSelectAll.getTag().equals("0"))
            {
                if(!listBook.isEmpty())
                {
                    tvSelectAll.visibility=View.VISIBLE
                    tvSelectAll.setTag("1")
                    tvSelectAll.text=getString(R.string.deselect_all)
                    for (i in 0 until listBook.size)  {
                        var locationName=roomDatabaseBuilder?.getBookDao()?.getLocationName(listBook.get(i).LocationId?:0)
                        listBook[i].Location= locationName?.locationName?:""
                        listBook[i].isSelected=true
                    }
                    notFoundAdapter = DifferntLocationAdapter(requireActivity(),requireActivity().supportFragmentManager ,listBook)
                    rvNotFound.adapter = notFoundAdapter
                    notFoundAdapter?.notifyDataSetChanged()
                }
                else
                {
                    tvSelectAll.visibility=View.GONE
                }
            }
            else
            {
                tvSelectAll.setTag("0")
                tvSelectAll.text=getString(R.string.select_all)
                for (i in 0 until listBook.size)  {
                    var locationName=roomDatabaseBuilder?.getBookDao()?.getLocationName(listBook.get(i).LocationId?:0)
                    listBook[i].Location= locationName?.locationName?:""
                    listBook[i].isSelected=false
                }
                notFoundAdapter = DifferntLocationAdapter(requireActivity(),requireActivity().supportFragmentManager ,listBook)
                rvNotFound.adapter = notFoundAdapter
                notFoundAdapter?.notifyDataSetChanged()
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                notFoundAdapter.filter.filter(newText)
                return true
            }
        })
    }

    private fun setAdaptor() {
        listBook.clear()
        val inventoryMasterList = bookDao.getPendingInventoryScan(locationId)

        if(inventoryMasterList.isEmpty()|| inventoryMasterList==null)
        {
        }
        else {
            val inventorymaster = inventoryMasterList.get(0)

            listBook.addAll(
                bookDao?.getAssetDifferentLoc(inventorymaster.scanID, locationId) ?: emptyList()
            )
            if(listBook.isEmpty())
            {
                tvSelectAll.visibility=View.GONE
            }

        }

        for (i in 0 until listBook.size)  {
            var locationName=roomDatabaseBuilder?.getBookDao()?.getLocationName(listBook.get(i).LocationId?:0)
            listBook[i].Location= locationName?.locationName?:""
      //      var category=roomDatabaseBuilder?.getBookDao()?.getCatgeoryName(listBook.get(i).assetCatalogue.categoryId?:0)
//            listBook[i].assetCatalogue?.categoryName= category?.categoryName?:""
//            var subcategory=roomDatabaseBuilder?.getBookDao()?.getSubCatgeoryName(listBook.get(i).assetCatalogue.subCategoryId?:0)
//            listBook[i].assetCatalogue?.categoryName= subcategory?.subCategoryName?:""
            listBook[i].isSelected=false
        }
        notFoundAdapter = DifferntLocationAdapter(requireContext(), requireActivity().supportFragmentManager,listBook)

        rvNotFound.adapter = notFoundAdapter


    }

    fun updateList() {
       setAdaptor()
    }

}