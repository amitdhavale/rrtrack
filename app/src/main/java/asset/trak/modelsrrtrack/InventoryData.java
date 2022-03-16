package asset.trak.modelsrrtrack;

import com.google.gson.annotations.SerializedName;

public class InventoryData{
    @SerializedName("ScanID")
    public String scanID;
    @SerializedName("ScanDate")
    public String scanDate;
    @SerializedName("LocID")
    public int locID;
    @SerializedName("ScanStartDatetime")
    public String scanStartDatetime;
    @SerializedName("ScanEndDatetime")
    public String scanEndDatetime;
    @SerializedName("NoofAssetsScanned")
    public int noofAssetsScanned;
    @SerializedName("FoundForLoc")
    public int foundForLoc;
    @SerializedName("NotFoundForLoc")
    public int notFoundForLoc;
    @SerializedName("FoundForOtherLoc")
    public int foundForOtherLoc;
    @SerializedName("NotRegistered")
    public int notRegistered;
    @SerializedName("ScannedBy")
    public String scannedBy;
    @SerializedName("LocType")
    public String locType;
    @SerializedName("DeviceID")
    public String deviceID;
}