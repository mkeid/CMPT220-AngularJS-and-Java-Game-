
public class Location {
	// Declaration of properties
	private int id;
	private String description;
	private Item item;
	private String name;
	private int visitCount;

    // Linked Stuff
    private Location northLocation;
    private Location eastLocation;
    private Location southLocation;
    private Location westLocation;
	
	// Constructor
	public Location(int id, String name, String description, Item item) {
		 this.id = id;
		 this.description = description;
		 this.item = item;
		 this.name = name;
		 this.visitCount = 0;
	}

	// Getters
	public int getId() { return this.id; }
	public Item getItem() { return this.item; }
	public String getName() { return this.name; }
	public int getVisitCount() { return this.visitCount; }

    public Location getNorthLocation() { return this.northLocation; }
    public Location getEastLocation() { return this.eastLocation; }
    public Location getSouthLocation() { return this.southLocation; }
    public Location getWestLocation() { return this.westLocation; }

    // Setters
    public void setNearbyLocations(Location northLocation, Location eastLocation, Location southLocation, Location westLocation) {
        this.northLocation = northLocation;
        this.eastLocation = eastLocation;
        this.southLocation = southLocation;
        this.westLocation = westLocation;
    }
	
	// Other
	public void incrementVisitCount() { this.visitCount++; }
	
	// Display methods
	public String toString() { 
		if(this.visitCount == 1 && this.item != null) return this.name + " | you picked up '"+this.item.getName()+"'";
		return this.name + " | " + this.description; }
	}