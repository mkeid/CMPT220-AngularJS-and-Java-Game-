import javax.swing.JButton;

public class DirectionalButton extends JButton {
	// Declaration of properties
	private static final long serialVersionUID = 1L;
	private float opacity;
	
	// Constructor
	public DirectionalButton(String title) { super(title); }
	
	// Getters
	public float getOpacity() { return this.opacity; }
	
	// Setters
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	    this.repaint();
	}
	
}
