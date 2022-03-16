package asset.trak.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavController
import asset.trak.database.daoModel.BookAndAssetData
import asset.trak.database.entity.CategoryMaster
import asset.trak.utils.Constants
import asset.trak.views.adapter.CategoriesAdapter
import asset.trak.views.adapter.OnResultClickListener
import asset.trak.views.adapter.ResultAdapter
import asset.trak.views.baseclasses.BaseFragment
import com.markss.rfidtemplate.R
import com.markss.rfidtemplate.application.Application
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_categories.*


@AndroidEntryPoint
class CategoriesFragment : BaseFragment(R.layout.fragment_categories) {
    private lateinit var navController: NavController
    private lateinit var categoriedAdapter: CategoriesAdapter
    private var listCategories = ArrayList<CategoryMaster>()
    private lateinit var resultAdapter: ResultAdapter
    private var listBook = ArrayList<BookAndAssetData>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdaptor()

        ivBack.setOnClickListener {
            getBackToPreviousFragment()
        }


        btnRecordInventory.setOnClickListener {
            replaceFragment(
                requireActivity().supportFragmentManager, MyLibrarySearchFragment(),
                R.id.content_frame
            )        }
        tvAddNewBook.setOnClickListener {
            replaceFragment(
                requireActivity().supportFragmentManager, AddBookFragment(),
                R.id.content_frame
            )
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText?.length!!.equals(0)) {
                    tvCategory.text = getString(R.string.lblCategory)
                    llBottomParent.visibility = View.VISIBLE
                    tvSearch.visibility = View.GONE
                    setAdaptor()
                } else {
                    tvCategory.text = getString(R.string.lblResults)
                    llBottomParent.visibility = View.GONE
                    tvSearch.visibility = View.VISIBLE
                    setResultAdaptor()
                    resultAdapter.filter.filter(newText)

                }
                return true
            }
        })

    }


    private fun setAdaptor() {

        val bundle = arguments
        if (bundle !=null) {

            if(bundle.getString(Constants.AssetTitle)!=null&& bundle.getString(Constants.AssetTitle)!!.isNotEmpty())
                tvMyLibrary.text=bundle.getString(Constants.AssetTitle)
        }


        listCategories.clear()

        if (Constants.AssetClassId==0)
        {
            listCategories.addAll(Application.roomDatabaseBuilder?.getBookDao()?.getCategoriesMasterList()?: arrayListOf())

        }else
        {  val id = Application.roomDatabaseBuilder?.getBookDao()?.getCategoryIdByClassId(Constants.AssetClassId ?: 0)
            var list = arrayListOf<Int>()
            for (i in 0 until id?.size!!) {
                list.add(id?.get(i)?.categoryId?:0)

                Log.e("list",""+list.size)
            }
            listCategories.addAll(Application.roomDatabaseBuilder?.getBookDao()?.getCategoryListByCategoriesIds(list)?: arrayListOf())



        }

        for(category in listCategories)
        {
            val assetCategoryCount=Application.roomDatabaseBuilder?.getBookDao()?.getCategoryAssetCount(category.id?:0)
            category.CategoryAssetCount=assetCategoryCount
        }



        categoriedAdapter = CategoriesAdapter(this, listCategories)
        rvCategories.adapter = categoriedAdapter
    }

    private fun setResultAdaptor() {
        listBook.clear()


        if (arguments == null) {
            //listBook.addAll(Application.roomDatabaseBuilder?.getBookDao()?.getBooks() ?: emptyList())
        } else {


            val categoryId = arguments?.getInt(Constants.CategoryId)
            listBook.addAll(Application.roomDatabaseBuilder?.getBookDao()?.getBooksCategory(categoryId ?: 1) ?: emptyList())
        }
        Log.d("data==",listBook.size.toString())
       // resultAdapter = ResultAdapter(requireContext(), this, listBook)
        rvCategories.adapter = resultAdapter
    }

    fun onCategoryClick(category: CategoryMaster) {


        val id = Application.roomDatabaseBuilder?.getBookDao()?.getCatSubCatMapByCatId(category.id?: 0)
        var list = arrayListOf<Int>()
        for (i in 0 until id?.size!!) {
            list.add(id?.get(i)?.categoryId?:0)
        }

        val subcategoryList= Application.roomDatabaseBuilder?.getBookDao()?.getSubCategoriesByCatIds(list)

        if(subcategoryList!=null&&subcategoryList?.isNotEmpty())
        {
            val bundle = Bundle()
            bundle.putInt(Constants.CategoryId, category.id?:0)
            bundle.putString(Constants.CategoryTitle, category.categoryName)

            val fragment = SubCategoryFragment()
            fragment.arguments = bundle
            replaceFragment(
                requireActivity().supportFragmentManager, fragment,
                R.id.content_frame
            )

        }
        else
        {
            val bundle = Bundle()
            bundle.putInt(Constants.CategoryId, category.id?:0)
            bundle.putString(Constants.CategoryTitle, category.categoryName)

            val fragment = MyLibrarySearchFragment()
            fragment.arguments = bundle
            replaceFragment(
                requireActivity().supportFragmentManager, fragment,
                R.id.content_frame
            )


        }



    }
//
//    override fun onGoalClick(bookCatalogue: BookAndAssetData) {
//        val bundle = Bundle()
//        bundle.putParcelable(Constants.BookData, bookCatalogue)
//        val fragment = SubCategoryFragment()
//        fragment.arguments = bundle
//        replaceFragment(
//            requireActivity().supportFragmentManager, fragment,
//            R.id.content_frame
//        )
//    }

}
