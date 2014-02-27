
public class TravelLogItem extends LogItem {
	private Location location;
	
	// Constructor
	public TravelLogItem(Location location) {
		this.location = location;
	}
	
	// Getters
	public Location getLocation() {
		return this.location;
	}
	
	public String toString() {
		return "> "+this.location.toString();
	}
}
