package asset.trak.model;

public class InventoryMasterApi{
    public String scanId;
    public String deviceId;
    public int locationId;
    public String scanOn;
    public int registered;
    public int notRegistered;
    public int notFound;
    public int foundOfDiffLocation;

    public InventoryMasterApi(String scanId, String deviceId, int locationId, String scanOn, int registered, int notRegistered, int notFound, int foundOfDiffLocation) {
        this.scanId = scanId;
        this.deviceId = deviceId;
        this.locationId = locationId;
        this.scanOn = scanOn;
        this.registered = registered;
        this.notRegistered = notRegistered;
        this.notFound = notFound;
        this.foundOfDiffLocation = foundOfDiffLocation;
    }
}
