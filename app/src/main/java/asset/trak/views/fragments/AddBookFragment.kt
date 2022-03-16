package asset.trak.views.fragments;

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import asset.trak.database.entity.*

import asset.trak.repository.BookRepository
import asset.trak.utils.*
import asset.trak.views.adapter.CategorySpinnerAdapter
import asset.trak.views.adapter.SearchTagAdapter
import asset.trak.views.adapter.SubCategoryArrayAdapter
import asset.trak.views.baseclasses.BaseFragment
import com.bumptech.glide.Glide
import com.markss.rfidtemplate.R
import com.markss.rfidtemplate.application.Application

import com.markss.rfidtemplate.common.ResponseHandlerInterfaces.*
import com.markss.rfidtemplate.home.MainActivity
import com.markss.rfidtemplate.inventory.InventoryListItem
import com.markss.rfidtemplate.rfid.RFIDController
import com.zebra.rfid.api3.RFIDResults
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_book.*
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint

class AddBookFragment : BaseFragment(R.layout.fragment_add_book),ResponseTagHandler, TriggerEventHandler, ResponseStatusHandler {
    private lateinit var searchTagAdapter: SearchTagAdapter
    private var catId: Int? = 0
    private var catSubCatId: Int? = 0
    private var locationPosition: Int = 0
    private var tags: String? = null

    @Inject
    lateinit var mProfileRepository: BookRepository

    private var category = ArrayList<Selection>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        category = ArrayList()
        category.add(Selection("Political"))
        category.add(Selection("Law"))
        category.add(Selection("Admin"))
        searchTagAdapter = SearchTagAdapter(requireContext(), category)
        rvSearchTag.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvSearchTag.itemAnimator = DefaultItemAnimator()
        rvSearchTag.adapter = searchTagAdapter
        tvUploadPicture.setOnClickListener {
            val fragment = TakePictureDialog()
            fragment.show(requireActivity().supportFragmentManager, fragment.tag)
        }
        ivBack.setOnClickListener {
            getBackToPreviousFragment()


        }

        val myListAdapter = CategorySpinnerAdapter(requireActivity(), Application.roomDatabaseBuilder?.getBookDao()?.getCategoriesMasterList()?: arrayListOf())
        spinnerCategory.adapter = myListAdapter

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                val category = spinnerCategory.selectedItem as CategoryMaster
                catId=category.id?:0
                val id = Application.roomDatabaseBuilder?.getBookDao()?.getCatSubCatMapByCatId(category.id ?: 0)
                var list = arrayListOf<Int>()
                for (i in 0 until id?.size!!) {
                    list.add(id?.get(i)?.categoryId?:0)
                }

//
                val myListAdapter = SubCategoryArrayAdapter(requireActivity(),
                    Application.roomDatabaseBuilder?.getBookDao()?.getSubCategoriesByCatIds(list)?: arrayListOf())
                spinnerSubCatgeory.adapter = myListAdapter

                spinnerSubCatgeory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long)
                    {
                        val subCategory = spinnerSubCatgeory.selectedItem as SubCategoryMaster
                        catSubCatId=subCategory.id?:0
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // can leave this empty
            }
        }

        spinnerLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long)
            {
                locationPosition = spinnerLocation.getItemIdAtPosition(position).toInt()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        buttonAdd.setOnClickListener {
            if (TextUtils.isEmpty(etRfid.text.toString())) {
                toastMessage("please enter Rfid")
                etRfid.requestFocus()
            } else if (TextUtils.isEmpty(etBookTitle.text.toString())) {
                toastMessage("please enter book title")
                etBookTitle.requestFocus()
            } else if (TextUtils.isEmpty(etDescription.text.toString())) {
                toastMessage("please enter description")
                etDescription.requestFocus()
            } else if (TextUtils.isEmpty(etAuthor.text.toString())) {
                toastMessage("please enter author")
                etAuthor.requestFocus()
            } else if (TextUtils.isEmpty(etIsbn.text.toString())) {
                toastMessage("please enter Isbn no")
                etIsbn.requestFocus()
            } else {



                //remove id
//                val data= AssetCatalogue(etBookTitle.text.toString(),pictureFilePath?:""
//                    ,"Hindi",
//                    Constants.AssetClassId,catId,catSubCatId,locationPosition,etRfid.text.toString(),searchTagAdapter.getTagValue(),
//                    0,Date().toString(),Date().toString(),0)
//
//
//                val assetData= Application.roomDatabaseBuilder?.getBookDao()?.addAssetCatalogue(data)
//
//                Application.roomDatabaseBuilder?.getBookDao()?.addBookAttributes(BookAttributes(assetData?.toInt(),"","Shivam",etAuthor.text.toString(),"English",200,"", etIsbn.text.toString(),etIsbn.text.toString()))
//

                toastMessage(getString(R.string.add_successfully))
                getBackToPreviousFragment()

            }


        }


        mProfileRepository.mTakePickAction.observe(viewLifecycleOwner,
            {
                if (it != null && it.isNotEmpty()) {
                    if (it.equals("Camera", false)) {
                        getCameraFeature()
                    }
                    if (it.equals("Gallery", false)) {
                        getGalleryFeature()
                    }
                }
            })
    }

    private fun getGalleryFeature() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkSelfPermissionsRW()) {
                requestPermissionRW()
            } else {
                showGalleryMediaFiles()
            }
        } else {
            showGalleryMediaFiles()
        }
    }


    private fun requestPermissionRW() {
        requestPermissions(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), READ_EXTERNAL_STORAGE_REQUEST
        )
    }


    private fun showNoAccess() {
        toastMessage(resources.getString(R.string.permissions_must_be_required))
    }
    private fun goToSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireActivity().packageName, null)
        ).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { intent ->
            startActivity(intent)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        when (requestCode) {
            READ_EXTERNAL_STORAGE_REQUEST -> {
                val permissionWrite = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val permissionRead = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (permissionWrite && permissionRead) {
                    showGalleryMediaFiles()
                } else {
                    val showRationale =
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    if (showRationale) {
                        showNoAccess()
                    } else {
                        goToSettings()
                    }
                }
                return
            }
            REQUEST_IMAGE_CAPTURE -> {
                val permissionCamera = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (permissionCamera) {
                    dispatchTakePictureIntent()
                } else {
                    val showRationale =
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            Manifest.permission.CAMERA
                        )
                    if (showRationale) {
                        showNoAccess()
                    } else {
                        goToSettings()
                    }
                }
                return
            }
        }
    }


    private fun checkSelfPermissionsRW(): Boolean {
        val result1 = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val result2 = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
    }


    private fun showGalleryMediaFiles() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE)
    }


    private fun checkSelfPermissionsCamera(): Boolean {
        val result1 =
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        return result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun openCameraMediaStore() {
        if (checkSelfPermissionsCamera()) {
            dispatchTakePictureIntent()
        } else {
            requestPermissionCamera()
        }
    }


    private fun dispatchTakePictureIntent() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            cameraIntent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true)
            if (cameraIntent.resolveActivity(requireContext().packageManager) != null) {
                var pictureFile: File? = null
                try {
                    pictureFile = getPictureFile()
                } catch (ex: IOException) {
                    toastMessage("Photo file can't be created, please try again")
                    return
                }
                val photoURI = FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().packageName + ".provider",
                    pictureFile
                )
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
            }
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    private var pictureFilePath: String? = null
    private val READ_EXTERNAL_STORAGE_REQUEST = 0x1047
    private val REQUEST_IMAGE_CAPTURE = 0x1048
    private val RESULT_LOAD_IMAGE = 1

    @Throws(IOException::class)
    private fun getPictureFile(): File {
        val simpleDateFormat = SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault())
        val timeStamp: String = simpleDateFormat.format(Date())
        val pictureFile = "Library$timeStamp"
        val storageDir: File =
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val image: File = File.createTempFile(pictureFile, ".jpg", storageDir)
        pictureFilePath = image.absolutePath
        return image
    }

    private fun requestPermissionCamera() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_IMAGE_CAPTURE)
    }


    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }


    private fun getCameraFeature() {
        if (checkCameraHardware(requireContext())) {
            openCameraMediaStore()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {

            val cameraImageFile = File(pictureFilePath!!)
            if (cameraImageFile.exists()) {
                renderImage(pictureFilePath!!)


                //mConnectRepository.mCamImageFilePath = pictureFilePath
            }
        }





        if (requestCode == RESULT_LOAD_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            userIV.setImageURI(selectedImageUri)
            pictureFilePath = ImageFilePath.getPath(requireContext(), selectedImageUri)
            mProfileRepository.mTakePickAction.value = " "
            LogUtils.logD("Gallery", "selected image=" + pictureFilePath)
        }

    }

    private fun renderImage(imagePath: String?) {
        lifecycleScope.launch {
            pictureFilePath = compressImage(requireActivity(), File(imagePath)).absolutePath ?: ""

            Glide.with(requireContext())
                .load(pictureFilePath)
                .placeholder(R.color.light_gray)
                .into(userIV)

            mProfileRepository.mTakePickAction.value = " "

        }

    }


    override fun handleTagResponse(inventoryListItem: InventoryListItem?, isAddedToList: Boolean) {


        if (isAddedToList) {
            Log.d("test", "testresponse${isAddedToList}")
            Log.d("test", "testresponse${inventoryListItem!!.tagID}")
            if (!Application.TAG_LIST_MATCH_MODE) {
                etRfid.setText("")
                Log.d("test", "testresponse${inventoryListItem!!.tagID}")
                etRfid.setText(inventoryListItem!!.tagID)

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
        Log.d("test", "test4")

    }







}