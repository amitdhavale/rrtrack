package asset.trak.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import asset.trak.database.entity.AssetClassification
import asset.trak.utils.Constants
import asset.trak.views.adapter.AssetRegistrationAdapter
import asset.trak.views.baseclasses.BaseFragment
import asset.trak.views.listener.OnAssetRegisClickListener
import asset.trak.views.module.InventoryViewModel
import com.markss.rfidtemplate.R
import com.markss.rfidtemplate.application.Application
import kotlinx.android.synthetic.main.fragment_asset_regi.*

//click

class AssetRegisterFragment : BaseFragment(R.layout.fragment_asset_regi) ,
    OnAssetRegisClickListener {
    private var listOfAsset = ArrayList<AssetClassification>()
    private val inventoryViewModel: InventoryViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            Log.d("AssetRegisterFragment", "onViewCreated Size: ${inventoryViewModel.listBookAttributes.size}")
            if (!inventoryViewModel.listBookAttributes.isNullOrEmpty()) {
                Application.bookDao?.addBookAttributeList(inventoryViewModel.listBookAttributes)

            }
        }
        catch (e:Exception)
        {
            Log.d("AssetRegisterFragment", "Error onViewCreated: ${e.message}")
            e.printStackTrace()
        }

        buttonscan.setOnClickListener {
            replaceFragment(
                requireActivity().supportFragmentManager, ScanFragment(),
                R.id.content_frame
            )        }

        ivBack.setOnClickListener {
            getBackToPreviousFragment()

        }

        listOfAsset.clear()
        listOfAsset.addAll(Application.roomDatabaseBuilder?.getBookDao()?.getAssetClassficationMasterList() ?: emptyList())


        for(asset in listOfAsset)
        {
            val assetCount=Application.roomDatabaseBuilder?.getBookDao()?.getAssetCount(asset.id?:0)
            asset.AssetCount=assetCount
        }


        val assRegisterAdapter= AssetRegistrationAdapter(requireContext(),this,listOfAsset)
        rvAssetRegistration.adapter=assRegisterAdapter



    }

    override fun onItemClick(data: AssetClassification) {

        Constants.AssetClassId=data.id?:1
        val bundle = Bundle()
        bundle.putString(Constants.AssetTitle, data.className)
        replaceFragment(
            requireActivity().supportFragmentManager, MyLibrarySearchFragment(),
            R.id.content_frame
        )

    }


}