package asset.trak.modelsrrtrack

data class LastSyncResponse(
    val `data`: LastSyncData?=LastSyncData(),
    val statuscode: Int?
)