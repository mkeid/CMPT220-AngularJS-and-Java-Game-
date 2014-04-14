public class Item {
	// Declaration of properties
	private int id;
    private String name;
    private String description;
    private int cost;

    // Constructor
	public Item(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	// Getters
	public int getId() { return this.id; }
	public String getName() { return this.name; }
	public String geDescription() { return this.description; }
    public int getCost() { return this.cost; }

    // Setters
    public void setCost(int cost) { this.cost = cost; }
	
	// Display methods
	public String toString() { return this.name + " | " + this.description; }
}