package asset.trak.database.entity

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import asset.trak.utils.ImageFilePath
import asset.trak.utils.UriConverters
import kotlinx.android.parcel.Parcelize





@Entity(tableName = "tblAssetCatalogue")


@Parcelize
data class AssetCatalogue(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var assetName: String? = null,
    var imagePath: String? = null,
    var imagePathFile: String? = null,

    var description: String? = null,
    var assetClassId: Int? = null,
    var categoryId: Int? = null,
    var subCategoryId: Int? = null,
    var locationId: Int? = null,
    var rfidTag: String? = null,
    var searchTags: String? = null,
    var inventorySyncFlag: Int? = null,
    var createdOn: String? = null,
    var modifiedOn: String? = null,
    var inventorySyncOn:Int?=null,
    var inventoryScanId:String?=null,
    var Image:Int?=null,
    var locationName: String ="",
    var categoryName: String ="",
    var subCategoryName: String ="",
    var isSelected: Boolean =false,
    var imageUrl: String? = null,
    var imageId:String?=null


) : Parcelable {



}