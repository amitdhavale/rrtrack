package asset.trak.database.entity

import androidx.room.PrimaryKey


data class Selection(

    var rfidTags: String? = null,
    var isSelected: Boolean =false

)
