package asset.trak.modelsrrtrack

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "masterVendor")
data class MasterVendor(
    val AccountGroup: String?,
    val City: String?,
    val CreatedBy: String?,
    val CreatedOn: String?,
    val InactiveOn: String?,
    val ModifiedBy: String?,
    val ModifiedOn: String?,
    val VendorCode: String?,
    val VendorID: String?,
    val VendorName: String?
){
    @PrimaryKey(autoGenerate = false)
    var id:Int=0
}