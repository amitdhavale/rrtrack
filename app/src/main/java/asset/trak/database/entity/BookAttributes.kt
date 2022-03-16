package asset.trak.database.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import asset.trak.database.entity.AssetCatalogue
import kotlinx.android.parcel.Parcelize

//@Entity(tableName = "tblBookAttributes")

@Entity(tableName = "tblBookAttributes",
    foreignKeys = [ForeignKey(entity = AssetCatalogue::class,
        parentColumns = ["id"],
        childColumns = ["id"],
        onDelete = ForeignKey.CASCADE)],
    indices = [Index("id")]
)

@Parcelize
data class BookAttributes(
    @PrimaryKey(autoGenerate = false)
    var id: String ="",
    var edition: String? = null,
    var publisher: String? = null,
    var author: String? = null,
    var language: String? = null,
    var numberOfPages: Int? = null,
    var Publish: String? = null,
    var isbN10: String? = null,
    var isbN13: String? = null):Parcelable
{
//    @PrimaryKey(autoGenerate = true)
//    var idDb: Int? = null
}