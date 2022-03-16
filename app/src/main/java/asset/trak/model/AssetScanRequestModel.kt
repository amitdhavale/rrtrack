package asset.trak.model

 class AssetScanRequestModel {
     val assetScan: List<AssetScan> = listOf()
     val inventoryMaster: List<InventoryMaster> = listOf()
 }

 class AssetScan {
    val newLocationId: Int = 0
    val rfidTag: String? = null
    val scanId: String? = null
}

 class InventoryMaster {
    val deviceId: String? = null
    val foundOfDiffLocation: Int = 0
    val locationId: Int = 0
    val notFound: Int = 0
    val notRegistered: Int = 0
    val registered: Int = 0
    val scanId: String? = null
    val scanOn: String? = null
}