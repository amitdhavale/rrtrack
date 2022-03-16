package asset.trak.database.entity

import androidx.room.*

@Entity(tableName = "tblSubCategoryMaster")





data class SubCategoryMaster (

    @PrimaryKey(autoGenerate = false)
    var id: Int? = null,
    var subCategoryName: String? = null,
    var SyncFlag: Int? = null,
    var CreatedOn: String? = null,
    var ModifiedOn: String? = null,
    var SubCategoryAssetCount: Int? = null,
) {
    var iconName : String? = null

    override fun toString(): String {
        return subCategoryName.toString()
    }
}


