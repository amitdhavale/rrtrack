package asset.trak.views.listener

import asset.trak.database.entity.AssetClassification


interface OnAssetRegisClickListener {
    fun onItemClick(data: AssetClassification)
}