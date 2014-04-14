
public class ErrorLogItem extends LogItem {
	private String errorMessage;
	
	// Constructor 
	public ErrorLogItem(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	// Getters
	public String getErrorMessage() {
		return this.errorMessage;
	}
}
