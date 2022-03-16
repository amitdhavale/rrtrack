package asset.trak.modelsrrtrack

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "masterLocation")
@Parcelize
data class MasterLocation(
    @PrimaryKey(autoGenerate = false)
    var LocID: Int,
    val CreatedBy: String?,
    val CreatedOn: String?,
    val Description: String?,
    val DisplayName: String?,
    val ImageURL: String?,
    val InactiveOn: String?,
    val LocBarcode: String?,
    val LocQRCode: String?,
    val LocRFID: String?,
    val ModifiedBy: String?,
    val ModifiedOn: String?,
    val Name: String?,
    val ParentLocID: Int?
):Parcelable