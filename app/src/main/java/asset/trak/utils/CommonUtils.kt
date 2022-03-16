package asset.trak.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.telephony.TelephonyManager
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import com.markss.rfidtemplate.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object Constants {
    val LastSyncTs="LastSyncTs"
    val DeviceId="DeviceId"
    var AssetClassId = 0
    var CategoryId="CategoryId"
    var SubCategoryId="SubCategoryId"
    var BookData="bookData"
    var AssetTitle="assetTitle"
    var CategoryTitle="categoryTitle"
    var SubCategoryTitle="subCategoryTitle"
    var LocationId=0
    var ScanId=0
    var PrefenceFileName = "myprefs"
    var globalInventory="global"
    var locationInventory="location"


    fun isInternetAvailable(context: Context): Boolean {
        var isConnected: Boolean = false // Initial Value
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }

    object InventoryStatus{
        const val PENDING="Pending"
        const val COMPLETED="Completed"
    }

    fun getIMEI(activity: Context): String? {
        val telephonyManager = activity
            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.deviceId
    }

    fun setTitleImage(context: Context, text: String?): BitmapDrawable {
        val bm = BitmapFactory.decodeResource(context.resources, R.drawable.square_bg)
        val paint = Paint()
      //  paint.setStyle(Style.FILL)
        //paint.setColor(Color.BLACK)
        paint.setTextSize(20f)
        val canvas = Canvas(bm)
        canvas.drawText(text.toString(), 0f, bm.height / 2f, paint)
        return BitmapDrawable(bm)
    }

    @SuppressLint("CheckResult")
    fun convertBitmapToFile(context: Context, bitmap: Bitmap, filename: String): String {

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream)
            val mediaByteArray = stream.toByteArray()

            try {
                val myDir = context.filesDir

                val path = "$myDir/media/"
                val secondFile = File("$myDir/media/", filename)

                if (!secondFile.parentFile.exists()) {
                    secondFile.parentFile.mkdirs()
                }
                secondFile.createNewFile()

                val fos = FileOutputStream(secondFile)
                fos.write(mediaByteArray)
                fos.flush()
                fos.close()
                return "$path/$filename"

            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        return ""
    }

    fun enableUserInteraction(requireActivity: FragmentActivity) {
        requireActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    fun disableUserInteraction(requireActivity: FragmentActivity) {
        requireActivity.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }




//    fun convertBitmapToFile(bitmap: Bitmap, context: Context,fileName:String): File? {
//        try {
//            // Create a file to write bitmap data
//            val fileName = fileName+"/"+System.currentTimeMillis()
//            var fos: OutputStream
//          //  val file: File
//            val root = Environment.getExternalStorageDirectory().toString()
//            val imagesDir = File(root+File.separator + "AssetTrack")
//            imagesDir.mkdirs()
////          val imagesDir = Environment.getExternalStorageDirectory(
////                Environment.DIRECTORY_PICTURES
////            ).toString() + File.separator + "AssetTrack"
//
//          //  file = File(imagesDir)
//
//            if (!imagesDir.exists()) {
//                imagesDir.mkdir()
//            }
//
//            val image = File(imagesDir, "$fileName.png")
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                val resolver: ContentResolver = context.getContentResolver()
//                val contentValues = ContentValues()
//                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
//                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
//                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/AssetTrack")
//                val imageUri =
//                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//                fos = resolver.openOutputStream(imageUri!!)!!
//
//            } else {
//                fos = FileOutputStream(image)
//            }
//
//            Log.d("test", "convertBitmapToFile: "+image)
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
//            return image
//
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
//        return null
//    }

}