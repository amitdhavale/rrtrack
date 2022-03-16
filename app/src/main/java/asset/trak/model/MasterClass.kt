package asset.trak.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName ="MasterClass" )
data class MasterClass(
    @PrimaryKey(autoGenerate = false)
      val ClassID:Int,
      val Name:String?,
      val Description:String?,
      val DisplayName:String?,
      val ImageURL:String?,
      val CreatedBy:String?,
      val CreatedOn:String?,
      val ModifiedBy:String?,
      val ModifiedOn:String?,
      val InactiveOn:String?
)