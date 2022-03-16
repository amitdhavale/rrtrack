package asset.trak.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "tblAssetClassification")
@Parcelize
data class AssetClassification(
    @PrimaryKey(autoGenerate = false)
    var id: Int? = null,
    var className: String? = null,
    var AssetCount: Int? = 0,

    ): Parcelable


{
    var iconName : String? = null
    var placeHolder : String? = null

}