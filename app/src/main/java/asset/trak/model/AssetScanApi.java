package asset.trak.model;

public class AssetScanApi{
    public String rfidTag;
    public String scanId;
    public int newLocationId;

    public String getRfidTag() {
        return rfidTag;
    }

    public void setRfidTag(String rfidTag) {
        this.rfidTag = rfidTag;
    }

    public String getScanId() {
        return scanId;
    }

    public void setScanId(String scanId) {
        this.scanId = scanId;
    }

    public int getNewLocationId() {
        return newLocationId;
    }

    public void setNewLocationId(int newLocationId) {
        this.newLocationId = newLocationId;
    }
}