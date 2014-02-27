
public class Location {
	// Declaration of properties
	private int id;
	private String description;
	private Item item;
	private String name;
	private int visitCount;
	
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
	
	// Setters
	public void incrementVisitCount() { this.visitCount++; }
	
	// Display methods
	public String toString() { return this.name + " | " + this.description; }
}
