package asset.trak.networklayer

import asset.trak.database.entity.LastSyncObject
import asset.trak.model.AssetSyncRequestDataModel
import asset.trak.modelsrrtrack.LastSyncResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/*https://resqqa.ril.com/RFIDService/AssetTracker/api/assetsync?LastSyncDateTime=2022-03-08%2019:35:31.463*/
interface AssetTrakAPIInterface {

    //AssetTracker/api/assetsync
    @GET("api/assetsync")
    suspend fun geLastSync(@QueryMap hashMap: Map<String, String>): Response<LastSyncResponse>

    @POST("api/assetsync")
    suspend fun posAssetSync(@Body body:RequestBody): Response<ResponseBody>
}