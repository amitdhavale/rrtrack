package asset.trak.utils.inter

import asset.trak.model.LocationUpdate

interface UpdateItemInterface {
    fun onUpdateItemCallback(locationData: LocationUpdate)

}