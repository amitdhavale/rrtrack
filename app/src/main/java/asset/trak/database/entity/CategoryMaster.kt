package asset.trak.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tblCategoryMaster")
data class CategoryMaster (
    @PrimaryKey(autoGenerate = false)

    @ColumnInfo(name = "Id")
    var id: Int? = null,
    var categoryName: String? = null,
    var SyncFlag: Int? = null,
    var CreatedOn: String? = null,
    var ModifiedOn: String? = null,
    var CategoryAssetCount: Int? = null

) {

    var iconName: String? = null

    override fun toString(): String {
        return categoryName.toString()
    }

}