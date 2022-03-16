package asset.trak.database.daoModel

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import asset.trak.database.entity.AssetCatalogue
import asset.trak.database.entity.BookAttributes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookAndAssetData(@Embedded var assetCatalogue: AssetCatalogue, @Relation(parentColumn = "id",entityColumn = "id")
    var bookAttributes: BookAttributes?=null
):Parcelable

