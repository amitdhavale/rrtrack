package asset.trak.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import asset.trak.modelsrrtrack.AssetData;
import asset.trak.modelsrrtrack.InventoryData;

public class AssetSyncRequestDataModel {
//    public ArrayList<AssetScanApi> assetScan=new ArrayList<>();
//    public ArrayList<InventoryMasterApi> inventoryMaster=new ArrayList<>();

    @SerializedName("AssetData")
    public ArrayList<AssetData> assetData=new ArrayList<AssetData>();
    @SerializedName("InventoryData")
    public InventoryData inventoryData=new InventoryData();

}
