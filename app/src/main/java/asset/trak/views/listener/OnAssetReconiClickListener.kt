package asset.trak.views.listener

import asset.trak.database.entity.AssetClassification


interface OnAssetReconiClickListener {
    fun onItemClick(data: AssetClassification)
}