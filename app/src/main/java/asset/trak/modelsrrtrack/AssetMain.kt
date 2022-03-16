package asset.trak.modelsrrtrack

import androidx.room.Entity
import androidx.room.PrimaryKey

/* "AssetID": "21FD7574-D17E-422A-9DA0-0215411C5897",
        "AssetRFID": null,
        "ScanID": 1,
        "ScanDate": null,
        "Class": "Apparels",
        "OfficeLocation": "Agra",
        "EntryDate": "2022-02-27T21:11:49.187Z",
        "ExitDate": null,
        "VendorBarcode": "SID002",
        "Supplier": "Landpoint",
        "SampleType": "Garment",
        "SampleNature": "Testing",
        "Season": "Spring Summer 22",
        "Color": "7516",
        "StyleNo": "5271",
        "AddressedTo": "Nikhil M",
        "CourierBillDetails": "626804795"*/
@Entity(tableName = "assetMain")
data class AssetMain(
    @PrimaryKey(autoGenerate = false)
    val AssetID: String,
    val AssetRFID: String?,
    var ScanID: String?,
    val ScanDate: String?,
    val Class:String?,
    var Location:String?,
    val EntryDate: String?,
    val ExitDate: String?,
    val VendorBarcode:String?,
    val Supplier:String?,
    val SampleType:String?,
    val SampleNature:String?,
    val Season:String?,
    val Color:String?,
    val StyleNo:String?,
    val AddressedTo:String?,
    val CourierBillDetails:String?,
    var LocationId:Int,
    var isSelected:Boolean,
    var inventorySyncFlag: Int=0,
//    val CategoryID: String?,
//    val ClassID: String?,
//    val CreatedBy: String?,
//    val CreatedOn: String?,
//    val InactiveOn: String?,
//    val LocID: Int?,
//    val ModifiedBy: String?,
//    val ModifiedOn: String?,
//    val SubCategoryID: String?
)