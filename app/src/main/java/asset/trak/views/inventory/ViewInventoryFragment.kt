package asset.trak.views.inventory

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import asset.trak.database.entity.Inventorymaster
import asset.trak.database.entity.LocationMaster
import asset.trak.modelsrrtrack.MasterLocation
import asset.trak.utils.Constants
import asset.trak.utils.Constants.disableUserInteraction
import asset.trak.views.baseclasses.BaseFragment
import asset.trak.views.module.InventoryViewModel
import com.markss.rfidtemplate.R
import com.markss.rfidtemplate.application.Application
import com.markss.rfidtemplate.application.Application.roomDatabaseBuilder
import com.markss.rfidtemplate.rapidread.RapidReadFragment
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.android.synthetic.main.fragment_view_inventory.*
import java.text.SimpleDateFormat
import java.util.*


class ViewInventoryFragment(val isFromWhat: String) : BaseFragment(R.layout.fragment_view_inventory) {

    private var barCodeName: String=""
    private var listOfLocations = ArrayList<LocationMaster>()
    private var currLocId =0
    private var currMasterLocation: MasterLocation?=null
    var sharedPreference: SharedPreferences?= null
    private val inventoryViewModel: InventoryViewModel by activityViewModels()
    var deviceId="A"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreference =  requireActivity().getSharedPreferences(Constants.PrefenceFileName,
            Context.MODE_PRIVATE)
        deviceId = sharedPreference?.getString(Constants.DeviceId,"A").toString()
        initialisation()
        setAdaptor()
        listeners()
        Log.d("tag1212121", "onViewCreated: ${Application.isReconsiled}")
        if(Application.isReconsiled)
        {
          getLastSync()
        }
    }

    private fun getLastSync() {
        progressBar.visibility = View.VISIBLE
        disableUserInteraction(requireActivity())
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val syncTime = sharedPreference?.getString(Constants.LastSyncTs, "2022-02-08")
        val currSyncTime = sdf.format(Date())
        val deviceId = Settings.Secure.getString(requireActivity().contentResolver, Settings.Secure.ANDROID_ID)
        Toast.makeText(activity, syncTime, Toast.LENGTH_SHORT)
            .show()

        inventoryViewModel.getLastSync(syncTime).observe(viewLifecycleOwner) {

            if (it != null && it.statuscode == 200 && it.data != null)  {
                it.data.let {
                    if (!it.AssetMain.isNullOrEmpty()) {
                        Application.bookDao?.addAssetMain(it.AssetMain)
                    }

                    if (!it.InventoryScan.isNullOrEmpty()) {
                        Application.bookDao?.addInventoryScan(it.InventoryScan)
                    }

                    if (!it.MasterLocation.isNullOrEmpty()) {
                        Application.bookDao?.addMasterLocation(it.MasterLocation)
                    }


                    if (!it.MasterVendor.isNullOrEmpty()) {
                        Application.bookDao?.addMasterVendor(it.MasterVendor)
                    }

                    if(!it.Inventorymaster.isNullOrEmpty()){
                        Application.bookDao?.addInventoryMaster(it.Inventorymaster)
                    }
                }
                Application.isReconsiled = false
            }
            //save last sync time in sp
            var editor = sharedPreference?.edit()
            editor?.putString(Constants.LastSyncTs, currSyncTime)
            editor?.putString(Constants.DeviceId, deviceId)
            editor?.commit()
            progressBar.visibility = View.INVISIBLE
            Constants.enableUserInteraction(requireActivity())
            Toast.makeText(activity, "Saved SynTime in sp:$currSyncTime", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun initialisation() {
        listOfLocations.clear()
        listOfLocations.addAll(roomDatabaseBuilder.getBookDao().getLocationMasterList())
    }

    private fun setAdaptor() {
        val listOfItems = ArrayList<String>()
        listOfItems.add(0,"Select Location")
        listOfLocations.forEach {
            listOfItems.add(it.locationName ?: "")
        }

        etRfid.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if(s.toString().trim().isEmpty())
                {
                    tvLocation.text=""
                    FancyToast.makeText(
                        requireActivity(),
                        "Please Enter Barcode.",
                        FancyToast.LENGTH_LONG,
                        FancyToast.WARNING,
                        false
                    ).show()
                }
                else if(s.toString().length >=4)
                {
                    barCodeName= s.toString().trim()
                    //here
                    currMasterLocation = Application.bookDao.getLocationMasterDataRR(barCodeName)
                    currMasterLocation?.let {
                        it.Name?.let {
                            tvLocation.text=it
                        }

                        currLocId=currMasterLocation!!.LocID
                        var lastScanId=""
                        var lastRecodedDate=""
                        var registeredAsPerLastScan=0
                        var newlyRegistered=0
                        if (currLocId != null) {
                            val invData =  roomDatabaseBuilder.getBookDao().getLastRecordedInventoryOfLocation(currLocId)
                            if(invData.count()>0){
                                lastRecodedDate= invData.get(0).scanOn.toString()
                                lastScanId=invData.get(0).scanID
                                //registeredAsPerLastScan= roomDatabaseBuilder.getBookDao().getCountLastScanRegistered(locId,lastScanId)
                                registeredAsPerLastScan= roomDatabaseBuilder.getBookDao().getCountOfRegisteredAsPerLastInventoryOfLocation(currLocId,lastScanId)
                                newlyRegistered= roomDatabaseBuilder.getBookDao().getCountNewlyRegisteredAfterLastScan(currLocId,lastScanId)

                            }else{
                                registeredAsPerLastScan=0
                                newlyRegistered= roomDatabaseBuilder.getBookDao().getCountLocationId(currLocId)
                            }
                        }


                        tvRegisteredCount.text = registeredAsPerLastScan.toString()
                        tvNewlyScanCount.text = newlyRegistered.toString()

                    }
                }
                else
                {
                    tvLocation.text=""
                    Log.d("tag1111", "afterTextChanged: Length ${s.length}")
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
               // tvSample.setText("Text is : "+s)

            }
        })
    }

    private fun listeners() {
        ivBackButton.setOnClickListener {
            getBackToPreviousFragment()
        }

        buttonscan.setOnClickListener {
            /*Add a Entry to Table with Pending Status*/

//            try {
//                if (!RFIDController.mConnectedReader.isConnected) {
//                    RFIDController.is_disconnection_requested = false
//                    try {
//                        RFIDController.mConnectedReader.connect()
//                    } catch (e: InvalidUsageException) {
//                        e.printStackTrace()
//                    } catch (e: OperationFailureException) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//            catch (e:Exception)
//            {
//                e.printStackTrace()
//            }

            requireActivity().hideKeyboard(it)
            if(currMasterLocation==null)
            {
                                FancyToast.makeText(
                    requireActivity(),
                    "Please Enter Valid Bar Code.",
                    FancyToast.LENGTH_LONG,
                    FancyToast.WARNING,
                    false
                ).show()
            }
            else
            {
                currLocId=currMasterLocation!!.LocID
                val pendingInventory  = roomDatabaseBuilder.getBookDao().getPendingInventoryScan(currLocId)

                val cnt = roomDatabaseBuilder.getBookDao().getInventoryMasterAllCount()
//
//            val inventoryLastItem: Inventorymaster = if (getListInventoryMaster.isNotEmpty())
//                getListInventoryMaster[getListInventoryMaster.size - 1] else Inventorymaster()
                //inventoryMaster.setStatus(asset.trak.utils.Constants.InventoryStatus.COMPLETED);
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
                val cal = Calendar.getInstance()
                val dateFormat = sdf.format(cal.time)
                if (pendingInventory.isEmpty()) {
                    Log.d("tag1212", "listeners: "+UUID.randomUUID().toString())
                    val inventoryMaster=Inventorymaster(
                        scanID = "A" + UUID.randomUUID().toString(),
                        deviceId = deviceId,
                        deviceIdCount = ((cnt ?: 0) + 1),
                        status = Constants.InventoryStatus.PENDING,
                        locationId = currLocId,
                        scanStartDatetime = dateFormat
                    )
                    roomDatabaseBuilder.getBookDao().addInventoryItem(inventoryMaster)
                    roomDatabaseBuilder.getBookDao().resetScanIdOfAssetsAtLocation(currLocId)
            }

                val fragment = RapidReadFragment()
                val bundle = Bundle()
                bundle.putParcelable(
                    "LocationData",
                   currMasterLocation
                )
                bundle.putString("INVENTORY_NAME",isFromWhat)
                bundle.putInt("totalRegistered", tvRegisteredCount.text.toString().toInt().plus(
                    tvNewlyScanCount.text.toString().toInt()
                ))

                fragment.arguments = bundle
                replaceFragment(
                    requireActivity().supportFragmentManager, fragment,
                    R.id.content_frame
                )

            }

        }
    }


    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }




}