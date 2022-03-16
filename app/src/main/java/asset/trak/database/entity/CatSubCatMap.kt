package asset.trak.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tblCatSubCatMap")
data class CatSubCatMap (
    @PrimaryKey(autoGenerate = false)
    var id: Int? = null,
    var categoryId: Int? = null,
    var subCategoryId: Int? = null)