package asset.trak.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "tblLocationMaster")

@Parcelize
data class LocationMaster(
    @PrimaryKey(autoGenerate = false)
    var id: Int? = null,
    var locationName: String? = null,
    var rfidTag: String? = null,
    var SyncFlag: Int? = null,
    var CreatedOn: String? = null
) :Parcelable

{
    var iconName: String? = null

}
