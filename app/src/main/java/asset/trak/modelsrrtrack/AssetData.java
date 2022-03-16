package asset.trak.modelsrrtrack;

import com.google.gson.annotations.SerializedName;

public class AssetData{
    @SerializedName("AssetID")
    public String assetID;
    @SerializedName("LocID")
    public int locID;
    @SerializedName("AssetRFID")
    public String assetRFID;
}

