package asset.trak.views.fragments;

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import asset.trak.database.entity.SubCategoryMaster
import asset.trak.utils.Constants
import asset.trak.views.adapter.SubCategoryAdapter
import asset.trak.views.baseclasses.BaseFragment

import com.markss.rfidtemplate.R
import com.markss.rfidtemplate.application.Application
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_shelves.*
import kotlinx.android.synthetic.main.fragment_shelves.btnRecordInventory
import kotlinx.android.synthetic.main.fragment_shelves.ivBack
import kotlinx.android.synthetic.main.fragment_shelves.tvAddNewBook

@AndroidEntryPoint
class SubCategoryFragment : BaseFragment(R.layout.fragment_shelves) {
    private lateinit var navController: NavController
    private lateinit var subCategoryAdapter: SubCategoryAdapter
    private var listOfSubCategories = ArrayList<SubCategoryMaster>()
    var catId:Int?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

       setAdaptor()

        ivBack.setOnClickListener {
            navController.popBackStack()
        }


        btnRecordInventory.setOnClickListener {
         //   navController.navigate(R.id.navRecordInventoryFragment)
        }
        tvAddNewBook.setOnClickListener {
          //  navController.navigate(R.id.navAddBookFragment)

        }




    }
    fun onSubCategoryClick(subcategory: SubCategoryMaster) {
        val bundle = Bundle()
        bundle.putInt(Constants.SubCategoryId, subcategory.id?:0)
        bundle.putInt(Constants.CategoryId, catId?:0)
        bundle.putString(Constants.CategoryTitle,subcategory.subCategoryName?:"")
    //    navController.navigate(R.id.navMyLibrarySearchFragment, bundle)

    }


        private fun setAdaptor() {
        listOfSubCategories.clear()

        val bundle = arguments
        if (bundle != null) {


            if(bundle.getString(Constants.CategoryTitle)!=null&& bundle.getString(Constants.CategoryTitle)!!.isNotEmpty())
                tvMyLibrary.text=bundle.getString(Constants.CategoryTitle)



            catId = bundle.getInt(Constants.CategoryId)?:0
            val id = Application.roomDatabaseBuilder?.getBookDao()?.getCatSubCatMapByCatId(catId?: 0)
            var list = arrayListOf<Int>()
            for (i in id!!) {
                list.add(i?.subCategoryId?:0)

            }

            listOfSubCategories.addAll(Application.roomDatabaseBuilder?.getBookDao()?.getSubCategoriesByCatIds(list)?: arrayListOf())
        }


        for(category in listOfSubCategories)
        {
            val assetCategoryCount= Application.roomDatabaseBuilder?.getBookDao()?.getSubCategoryAssetCount(category.id?:0)
            category.SubCategoryAssetCount=assetCategoryCount
        }

        subCategoryAdapter = SubCategoryAdapter(this, listOfSubCategories)
        rvSubCategories.adapter = subCategoryAdapter
    }





}