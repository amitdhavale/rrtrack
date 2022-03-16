package asset.trak.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import asset.trak.utils.compressimage.Compressor
import asset.trak.utils.compressimage.constraint.format
import asset.trak.utils.compressimage.constraint.quality
import java.io.File
import java.util.*

fun IntRange.random() =
    Random().nextInt((endInclusive + 1) - start) + start
suspend fun compressImage(activity: Activity?, actualImageFile: File?): File {
    try {
        val valueOf1mb = 1048576.0
        Log.e("File Size", (actualImageFile?.length()?.div(valueOf1mb)).toString())

        if (actualImageFile?.length() ?: 0 < (1 * valueOf1mb))
            return actualImageFile!!

        val qualityNeeded = when {
            actualImageFile?.length() ?: 0 < (1 * valueOf1mb) -> 100
            actualImageFile?.length() ?: 0 < (2 * valueOf1mb) -> 70
            actualImageFile?.length() ?: 0 < (3 * valueOf1mb) -> 60
            actualImageFile?.length() ?: 0 < (4 * valueOf1mb) -> 50
            actualImageFile?.length() ?: 0 < (5 * valueOf1mb) -> 45
            actualImageFile?.length() ?: 0 < (6 * valueOf1mb) -> 40
            actualImageFile?.length() ?: 0 < (10 * valueOf1mb) -> 15
            actualImageFile?.length() ?: 0 > (10 * valueOf1mb) -> 10
            else -> 100
        }

        //mb approximate
        val resultFile: File = Compressor.compress(activity as Context, actualImageFile!!) {
            quality(qualityNeeded)
            format(Bitmap.CompressFormat.JPEG)
        }

        Log.e("File Size New", (resultFile.length().div(valueOf1mb)).toString())

        return resultFile ?: File("")
    } catch (e: Exception) {
        return actualImageFile!!
    }
}
