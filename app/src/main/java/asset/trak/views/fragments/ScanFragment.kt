package asset.trak.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import asset.trak.database.daoModel.BookAndAssetData
import asset.trak.views.baseclasses.BaseFragment
import com.bumptech.glide.Glide
import com.markss.rfidtemplate.R
import com.markss.rfidtemplate.application.Application
import com.markss.rfidtemplate.common.ResponseHandlerInterfaces
import com.markss.rfidtemplate.home.MainActivity
import com.markss.rfidtemplate.inventory.InventoryListItem
import com.markss.rfidtemplate.rfid.RFIDController
import com.markss.rfidtemplate.settings.AdvancedOptionItemFragment
import com.markss.rfidtemplate.settings.AntennaSettingsFragment
import com.zebra.rfid.api3.RFIDResults
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_scan.*
import kotlinx.android.synthetic.main.itme_result.*
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class ScanFragment : BaseFragment(R.layout.fragment_scan),
    ResponseHandlerInterfaces.ResponseTagHandler, ResponseHandlerInterfaces.TriggerEventHandler,
    ResponseHandlerInterfaces.ResponseStatusHandler {
    //private  lateinit var antennaSettingsFragment:AntennaSettingsFragment
    private var listOfAsset = ArrayList<BookAndAssetData>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lt_book_info.visibility=View.GONE
        lblClearRFID.setOnClickListener {
            etRfid.text?.clear()
            lt_book_info.visibility=View.GONE
        }
       // AntennaSettingsFragment.powerLevels.set(0,30)
     //    antennaSettingsFragment=AntennaSettingsFragment.newInstance()
      //  antennaSettingsFragment.isStaticSettingsChanged(30)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun handleTagResponse(inventoryListItem: InventoryListItem?, isAddedToList: Boolean) {
        lt_book_info.visibility=View.VISIBLE
        Log.d("callManyTimer","callManyTimer");
        if (isAddedToList) {
            if (!Application.TAG_LIST_MATCH_MODE) {
                etRfid.setText("")
                etRfid.setText(inventoryListItem!!.tagID)
                listOfAsset.clear()

             //   Log.d("ScanFragment", "handleTagResponse:TagID is null ${inventoryListItem.tagID ?: "0"}")

            if(inventoryListItem?.tagID==null)
                {
                    Log.d("ScanFragment", "handleTagResponse:TagID is null ")
                }
                else
                {
//                    listOfAsset.addAll(
//                        Application.roomDatabaseBuilder.getBookDao()
//                            .getBookForRFID(inventoryListItem?.tagID)
//                    )

                    if(listOfAsset.isNullOrEmpty())
                    {
                        tvTitle.text = ""
                        tvAuthor.text = ""
                        tvTag.text = ""
                    }
                    else
                    {
                        val locationName = Application.roomDatabaseBuilder?.getBookDao()
                            ?.getLocationName(listOfAsset.get(0).assetCatalogue.locationId ?: 0)
                        listOfAsset[0].assetCatalogue?.locationName = locationName?.locationName ?: ""
                        tvTitle.text = listOfAsset[0].assetCatalogue.assetName
                        tvAuthor.text = listOfAsset[0].bookAttributes?.author


                        if(listOfAsset[0].assetCatalogue.subCategoryName.equals("") || listOfAsset[0].assetCatalogue.subCategoryName.isNotBlank())
                        {
                            tvCategory.text = listOfAsset[0].assetCatalogue.categoryName
                        }
                        else
                        {
                            tvCategory.text ="${listOfAsset[0].assetCatalogue.categoryName} - ${listOfAsset[0].assetCatalogue.subCategoryName}"
                        }
                        tvTag.text = locationName?.locationName
                        Glide.with(requireActivity())
                            .load(File(listOfAsset[0].assetCatalogue.imagePathFile.toString()))
                            .placeholder(R.color.light_gray)
                            .fitCenter()
                            .error(R.drawable.ic_not_found_error)
                            .into(ivBook)
                    }
                }
            }
        }
    }

    override fun triggerPressEventRecieved() {
        Log.d("test", "test2")
        if (!RFIDController.mIsInventoryRunning && activity != null) {

            lifecycleScope.launch {
                val activity = activity as MainActivity?
                activity?.inventoryStartOrStop()
            }

        }

    }

    override fun triggerReleaseEventRecieved() {
        Log.d("test", "test3")
        if (RFIDController.mIsInventoryRunning == true && activity != null) {
            //RFIDController.mInventoryStartPending = false;
            lifecycleScope.launch {
                val activity = activity as MainActivity?
                activity?.inventoryStartOrStop()
            }
        }

    }

    override fun handleStatusResponse(results: RFIDResults?) {

    }
}