package asset.trak.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "tblScanTag")
@Parcelize
data class ScanTag (
    @PrimaryKey(autoGenerate = true)
    var id:Int =0,
    var scanId: String? = null,
    var locationId: Int? = null,
    var rfidTag: String? = null,
    var isSelected: Boolean =false

):Parcelable
{


}