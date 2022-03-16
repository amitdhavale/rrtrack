package asset.trak.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "samplingArticles")
data class SamplingArticles(
    @PrimaryKey(autoGenerate = false)
    val AssetID: String,
    val VendorBarcode: String?,
    val Supplier: String?,
    val SampleType: Int?,
    val Season: Int?,
    val SampleNature: Int?,
    val StyleNo: String?,
    val Color: String?,
    val AddressedTo: String?,
    val OfficeLocation: String?,
    val CourierBillDetails: String?,
    val CreatedBy: String?,
    val CreatedOn: String?,
    val ModifiedBy: String?,
    val ModifiedOn: String?,
    val InactiveOn: String?
)
