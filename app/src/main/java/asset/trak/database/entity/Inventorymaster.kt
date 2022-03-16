package asset.trak.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tblInventorymaster")
data class Inventorymaster (
    @PrimaryKey(autoGenerate = true)
    var _id: Int?=null,
    var scanID: String = "",
    var deviceId: String? = null,
    var deviceIdCount: Int? = null,
    var scanOn: String? = null,
    var foundOnLocation: Int? = null,
    var foundOfDiffLocation: Int? = null,
    var registered: Int? = null,
    var notRegistered: Int? = null,
    var notFound: Int? = null,
    var locationId: Int? = null,
    var status: String? = null,
    var scanDate:String?=null,
    var scanStartDatetime:String?=null,
    var scanEndDatetime:String?=null,
    var noOfAssetsScanned:Int?=null,
    var scannedBy:String?=null





/*  "scanID": "21FD7574-D17E-422A-9DA0-0215411C0011",
        "scanDate": "2022-03-09T21:11:49.187Z",
        "locationId": 1,
        "scanStartDatetime": "2022-03-09T21:11:49.187Z",
        "scanEndDatetime": "2022-03-09T21:11:49.187Z",
        "noOfAssetsScanned": 2,
        "foundOnLocation": 2,
        "notFound": 2,
        "foundOfDiffLocation": 2,
        "notRegistered": 2,
        "scannedBy": "31FD7574-D17E-422A-9DA0-0215411C0011"*/
)