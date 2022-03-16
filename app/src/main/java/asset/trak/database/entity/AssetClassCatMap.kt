package asset.trak.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "tblAssetClassCatMap")
@Parcelize
data class AssetClassCatMap(
    @PrimaryKey(autoGenerate = false)
    val id: Int? = null,
    var classificationId: Int? = null,
    var categoryId: Int? = null,

    ): Parcelable