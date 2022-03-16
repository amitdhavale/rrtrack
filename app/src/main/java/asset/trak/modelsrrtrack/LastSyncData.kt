package asset.trak.modelsrrtrack

import asset.trak.database.entity.Inventorymaster

data class LastSyncData(
    val AssetMain: List<AssetMain>? = listOf(),
    val InventoryScan: List<InventoryScan>? = listOf(),
    val MasterCategory: List<Any>? = listOf(),
    val MasterClass: List<Any>? = listOf(),
    val MasterLocation: List<MasterLocation>? = listOf(),
    val MasterSharedLookup: List<Any>? = listOf(),
    val MasterSubCategory: List<Any>? = listOf(),
    val MasterVendor: List<MasterVendor>? = listOf(),
    val SamplingArticles: List<Any>? = listOf(),
    val Inventorymaster: List<Inventorymaster>? = listOf()
)