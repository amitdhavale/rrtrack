package asset.trak.modelsrrtrack

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventoryScan")
data class InventoryScan(
    val CreatedBy: String?,
    val CreatedOn: String?,
    val FoundForLoc: Int?,
    val FoundForOtherLoc: Int?,
    val InactiveOn: String?,
    val LocID: Int?,
    val ModifiedBy: String?,
    val ModifiedOn: String?,
    val NoofAssetsScanned: Int?,
    val NotFoundForLoc: Int?,
    val NotRegistered: Int?,
    val ScanDate: String?,
    val ScanEndDatetime: String?,
    val ScanID: String?,
    val ScanStartDatetime: String?,
    val ScannedBy: String?
){
    @PrimaryKey(autoGenerate = false)
    var id:Int=0
}
