
public class TravelLogItem extends LogItem {
	private Location location;
	private String log;
	
	// Constructor
	public TravelLogItem(Location location) {
		this.location = location;
	}
	
	// Getters
	public Location getLocation() {
		return this.location;
	}
	
	public String toString() {
		if(log == null) log = "> "+this.location.toString();
		return log;
	}
}
