import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class SimpleGame {
	private static JTextArea errorTextArea;
	private static JFrame frame;
	private static JPanel gridPanel;
	public static String name;
	private static JTextArea travelTextArea;
	
	// UI
	private static JLabel currentLocationLabel;
	private static JLabel scoreLabel;
	
	private static Item[] inventory;
	private static Location[][] map;
	private static int score;
	private static int selectedCol;
	private static int selectedRow;
	private static TravelLogItem[] travelLog;
	private static ErrorLogItem[] errorLog;

	public static void main(String [] args) {
		initWorld();
		initWindow();
	}
	
	private static void initWindow() {
		frame = new JFrame("Adventure Game");
		frame.setBackground(new Color(240,240,240));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // When you close the window this will make sure the program terminates.
        frame.setSize(1000, 800);
		frame.setVisible(true);
		showWelcomeSession();
	}
	
	private static void initWorld() {
		// Initialize items
	    Item teleporter = new Item(0, "Teleporter", "This teleports you to Lowell Thomas.");
	    Item ball = new Item(1, "Ball", "It does nothing.");
	    Item gold = new Item(2, "Gold", "This was stolen from a chemist in Donnelly.");
	    Item saber = new Item(3, "Light Saber", "A saber made of light and stuff.");
	    
	    // Initialize locations
	    Location donnelly = new Location(0, "Donnelly", "This is where people science.", gold);
	    Location dyson = new Location(1, "Dyson", "All business here.", null);
	    Location fontaine = new Location(2, "Fontaine", "There is nothing to do here.", null);
	    Location fontaineAnnex = new Location(3, "Fontaine Annex", "Tiniest building. Largest lot.", saber);
	    Location hancock = new Location(4, "Hancock", "Computers and stuff.", null);
	    Location library = new Location(5, "Library", "This is where people study.", null);
	    Location lowellThomas = new Location(6, "Lowell Thomas", "People are communicating.", teleporter);
	    Location mccann = new Location(7, "McCann Center", "YEA SPORTS", ball);
	    Location mcdonalds = new Location(8, "McDonald's", "They serve food and stuff.", null);
	    Location rotunda = new Location(9, "Rotunda", "There is nothing to do here.", null);
	    
	    // Assign locations to places on the map
	    map = new Location[5][3];
	    Location[] row1 = {null, fontaineAnnex, mcdonalds};
	    Location[] row2 = {null, fontaine, null};
	    Location[] row3 = {hancock, dyson, lowellThomas};
	    Location[] row4 = {rotunda, library, donnelly};
	    Location[] row5 = {null, mccann, null};
	    
	    map[0] = row1;
	    map[1] = row2;
	    map[2] = row3;
	    map[3] = row4;
	    map[4] = row5;
	    	    
	    // Start with no items
	    inventory = new Item[4];
	    // Start at Hancock
	    hancock.incrementVisitCount();
	    score = 5;
	    selectedRow = 2;
	    selectedCol = 0;
	    travelLog = new TravelLogItem[] {new TravelLogItem(hancock)};
	    errorLog = new ErrorLogItem[0];
	}
	
	public static void updateDirection(String direction) {
		TravelLogItem travelLogItem = travelLog[travelLog.length-1];
		Location currentLocation = travelLogItem.getLocation();
        Location changedLocation = moveLocation(currentLocation, direction);
        if(!currentLocation.getName().equals(changedLocation.getName())) {
        	resetGrid();
        	selectItemOnGrid();
        	currentLocationLabel.setText("Current Location: "+changedLocation.getName());
        	
        	TravelLogItem[] updatedTravelLog = new TravelLogItem[travelLog.length + 1];
        	for(int x = 0; x < travelLog.length; x++) {
        		updatedTravelLog[x] = travelLog[x];
        	}
        	updatedTravelLog[travelLog.length] = new TravelLogItem(changedLocation);
        	travelLog = updatedTravelLog;
        	updateTravelLog();
        }
	}
	
	public static Location moveLocation(Location currentLocation, String direction) {
		Location changedLocation = currentLocation;
        
		if(direction.equals("North")) {
			if(selectedRow != 0 && map[selectedRow - 1][selectedCol] != null) {
				changedLocation = map[selectedRow - 1][selectedCol];
				selectedRow--;
			}
			else warnAboutInvalidMove();
		} 
		else if(direction.equals("South")) {
			if(selectedRow != 4 && map[selectedRow + 1][selectedCol] != null) {
				changedLocation = map[selectedRow + 1][selectedCol];
				selectedRow++;
			}
			else warnAboutInvalidMove();
		}
		else if(direction.equals("East")) {
			if(selectedCol != 2 && map[selectedRow][selectedCol + 1] != null) {
				changedLocation = map[selectedRow][selectedCol + 1];
				selectedCol++;
			}
			else warnAboutInvalidMove();
		}
		else if(direction.equals("West")) {
			if(selectedCol != 0 && map[selectedRow][selectedCol - 1] != null) {
				changedLocation = map[selectedRow][selectedCol - 1];
				selectedCol--;
			}
			else warnAboutInvalidMove();
		}
		
		if(!changedLocation.getName().equals(currentLocation.getName())) {
			if(changedLocation.getVisitCount() == 0) {
				score += 5;
				updateScore();
				if(changedLocation.getItem() != null) {
					Item[] updatedInventory = new Item[inventory.length + 1];
					for(int x = 0; x < inventory.length; x++) {
						updatedInventory[x] = inventory[x];
					}
					updatedInventory[inventory.length] = changedLocation.getItem();
					inventory = updatedInventory;
				}
            }
            changedLocation.incrementVisitCount();
		}
		
		return changedLocation;
	}
	
	private static void showPlaySession() {
		frame.getContentPane().removeAll();
		
		BorderLayout layout = new BorderLayout();
		frame.setLayout(layout);
		
		/***********************
		 * Top Section
		 **********************/
		JPanel topPanel = new JPanel();
		topPanel.setBackground(new Color(71,71,71));
		
		scoreLabel = new JLabel("Score: 5", JLabel.CENTER);
		scoreLabel.setFont(new Font(scoreLabel.getFont().getName(), Font.BOLD, 20));
		scoreLabel.setForeground(Color.WHITE);
		topPanel.add(scoreLabel);
		
		frame.add(topPanel, BorderLayout.PAGE_START);
		
		/***********************
		 * Bottom Section
		 **********************/
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(new Color(71,71,71));
		bottomPanel.setLayout(new FlowLayout());
		
		// Controller label
		JLabel controllerLabel = new JLabel("Controller");
		controllerLabel.setForeground(Color.WHITE);
		controllerLabel.setFont(new Font(scoreLabel.getFont().getName(), Font.BOLD, 20));
		bottomPanel.add(controllerLabel);
		
		// Directional buttons
		JButton nButton = new JButton("North");
		nButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) { updateDirection("North"); }
	    });
		bottomPanel.add(nButton);
		
		JButton sButton = new JButton("South");
		sButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) { updateDirection("South"); }
	    });
		bottomPanel.add(sButton);
		
		JButton eButton = new JButton("East");
		eButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) { updateDirection("East"); }
	    });
		bottomPanel.add(eButton);
		
		JButton wButton = new JButton("West");
		wButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) { updateDirection("West"); }
	    });
		bottomPanel.add(wButton);
		
		// Info on directional input
		JLabel infoInputLabel = new JLabel("Enter n|s|e|w to move. Enter 'i' for inventory. Enter 'h' for help. ");
		infoInputLabel.setForeground(Color.WHITE);
		bottomPanel.add(infoInputLabel);
		
		// Directional input
		final JTextField textField = new JTextField("",5);
		bottomPanel.add(textField);
		
		// Go button
		JButton goButton = new JButton("GO");
		goButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) { 
	        	 char input = textField.getText().toLowerCase().charAt(0);
	        	 if(input == 'n') updateDirection("North");
	        	 else if(input == 's') updateDirection("South");
	        	 else if(input == 'e') updateDirection("East");   
	        	 else if(input == 'w') updateDirection("West");
	        	 else if(input == 'i') displayInventory();
	        	 else if(input == 'h') displayHelp();
	        	 else {
	        		 
	        	 }
	         }
	    });
		bottomPanel.add(goButton);
		
		frame.add(bottomPanel, BorderLayout.PAGE_END);
				
		/***********************
		 * Map Section
		 **********************/
		JPanel mapPanel = new JPanel();
		BoxLayout mapBoxLayout = new BoxLayout(mapPanel, BoxLayout.Y_AXIS);
		mapPanel.setBorder(new EmptyBorder(20,20,20,20));
		mapPanel.setLayout(mapBoxLayout);
		mapPanel.setPreferredSize(new Dimension(470, 800));
		
		JLabel mapLabel = new JLabel("Map", JLabel.CENTER);
		mapLabel.setFont(new Font(mapLabel.getFont().getName(), Font.BOLD, 20));
		mapPanel.add(mapLabel);
		
		currentLocationLabel = new JLabel("Current Location: Hancock", JLabel.CENTER);
		currentLocationLabel.setBorder(new EmptyBorder(0,0,20,0));
		currentLocationLabel.setFont(new Font(currentLocationLabel.getFont().getName(), Font.PLAIN, 15));
		mapPanel.add(currentLocationLabel);
		
		gridPanel = new JPanel();
		GridLayout gridLayout = new GridLayout(5,3);
		gridPanel.setLayout(gridLayout);
		// Spacing between grid units
		gridLayout.setHgap(30);
		gridLayout.setVgap(30);
		
		mapPanel.add(gridPanel);		
		
		// Create map UI
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[x].length; y++) {
				JLabel locationLabel;
				if(map[x][y] != null) {
					Location location = map[x][y];
					locationLabel = new JLabel(location.getName(), JLabel.CENTER);
				}
				else locationLabel = new JLabel("", JLabel.CENTER);
				
				Border border = BorderFactory.createLineBorder(new Color(220,220,220), 2);

				locationLabel.setBackground(Color.WHITE);	
				locationLabel.setBorder(border);
				locationLabel.setFont(new Font(locationLabel.getFont().getName(), Font.BOLD, 14));
				locationLabel.setForeground(new Color(40,40,40));
				locationLabel.setOpaque(true);
				gridPanel.add(locationLabel);
			}
		}
		
		frame.add(mapPanel, BorderLayout.LINE_START);
		selectItemOnGrid();
		
		/***********************
		 * Travel Log Section
		 **********************/
		JPanel travelPanel = new JPanel();
		travelPanel.setBackground(Color.WHITE);
		travelPanel.setBorder(new EmptyBorder(20,20,20,20));
		travelPanel.setPreferredSize(new Dimension(250, 800));
		
		BoxLayout travelLogLayout = new BoxLayout(travelPanel, BoxLayout.Y_AXIS);
		travelPanel.setLayout(travelLogLayout);
		
		JLabel travelLabel = new JLabel("Travel Log", JLabel.CENTER);
		travelLabel.setFont(new Font(travelLabel.getFont().getName(), Font.BOLD, 20));
		travelPanel.add(travelLabel);
		
		travelTextArea = new JTextArea(travelLog.length, 1);
		JScrollPane travelScrollPane = new JScrollPane(travelTextArea);
		travelScrollPane.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
		travelPanel.add(travelScrollPane);
		
		frame.add(travelPanel, BorderLayout.CENTER);
		
		/***********************
		 * Error Log Section
		 **********************/
		JPanel logPanel = new JPanel();
		logPanel.setBackground(Color.WHITE);
		logPanel.setBorder(new EmptyBorder(20,20,20,20));
		logPanel.setPreferredSize(new Dimension(180, 800));
		
		BoxLayout errorLogLayout = new BoxLayout(logPanel, BoxLayout.Y_AXIS);
		logPanel.setLayout(errorLogLayout);
		
		JLabel logLabel = new JLabel("Error Log", JLabel.CENTER);
		logLabel.setFont(new Font(logLabel.getFont().getName(), Font.BOLD, 20));
		logPanel.add(logLabel);
		
		errorTextArea = new JTextArea(errorLog.length, 1);
		JScrollPane scrollPane = new JScrollPane(errorTextArea);
		scrollPane.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
		logPanel.add(scrollPane);
		
		frame.add(logPanel, BorderLayout.LINE_END);
		
		frame.validate();
		frame.repaint();
		
		updateTravelLog();
	}
	
	private static void showWelcomeSession() {
		BoxLayout boxLayout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
	    frame.setLayout(boxLayout);
	    
	    /**************************
	     *  Create and add panels
	     *************************/
	    
	    // Welcome panel
	    JLabel welcomeLabel = new JLabel("Welcome", JLabel.CENTER);
		welcomeLabel.setFont(new Font(welcomeLabel.getFont().getName(), Font.BOLD, 40));
		welcomeLabel.setHorizontalTextPosition(JLabel.CENTER);
		welcomeLabel.setLocation(300,300);
		welcomeLabel.setSize(welcomeLabel.getPreferredSize());
		
		JPanel welcomePanel = new JPanel();
		welcomePanel.add(welcomeLabel);
		welcomePanel.setBorder(new EmptyBorder(300,0,0,0));
		welcomePanel.setMaximumSize(welcomePanel.getPreferredSize());
	    frame.add(welcomePanel);
	    
	    // Prompt panel
	    JLabel promptLabel = new JLabel("Before you start playing, please enter your name.", JLabel.CENTER);
	    promptLabel.setFont(new Font(promptLabel.getFont().getName(), Font.PLAIN, 20));
	    
	    JPanel promptPanel = new JPanel();
	    promptPanel.add(promptLabel);
	    promptPanel.setMaximumSize(promptPanel.getPreferredSize());
	    frame.add(promptPanel);
	    
	    // Name Panel
	    final JTextField nameTextField = new JTextField("", 30);
	    JButton goButton = new JButton("PLAY");
	    goButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	             if(nameTextField.getText().length() > 0) {
	            	 String updatedName = nameTextField.getText();
		        	 updatedName = Character.toUpperCase(updatedName.charAt(0)) + updatedName.substring(1);
	            	 declareName(updatedName, true);
	             }
	         }
	    });
	    
	    JPanel namePanel = new JPanel();
	    namePanel.setLayout(new FlowLayout());
	    namePanel.add(nameTextField);
	    namePanel.add(goButton);
	    frame.add(namePanel);
	    
	    frame.validate();
	}
	
	public static void declareName(String updatedName, boolean isInitialDeclaration) {
		name = updatedName;	
		frame.setTitle(name+"'s Adventure Game");
		if(isInitialDeclaration) showPlaySession();
	}
	
	private static void displayHelp() {
		String message = name+" Adventure Game is a simple game built with Java.\n";
		message += "It is based on the true life of "+name+" where he walks around campus all day collecting random items.\n";
		message += "To move around, press the directional buttons or enter one of the following directional key inputs: ( n | s | e | w )\n";
		message += "To open your inventory, enter 'i' in the text field above.";
		
		JOptionPane.showMessageDialog(frame, message, "Help", JOptionPane.INFORMATION_MESSAGE, null);
	}
	
	private static void displayInventory() {
		String message = "You do not have any items in your inventory.";
		
		int itemsCounted = 0;
		for(int x = 0; x < inventory.length; x ++) {
			if(inventory[x] != null) {
				if(itemsCounted == 0) message = "";
				Item item = inventory[x];
				itemsCounted++;
				message += itemsCounted+": "+item.getName()+"\n";
			}
		}
		
		JOptionPane.showMessageDialog(frame, message, "Items", JOptionPane.INFORMATION_MESSAGE, null);
	}
	
	private static void resetGrid() {
		Border border = BorderFactory.createLineBorder(new Color(220,220,220), 2);
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[x].length; y++) {
				if(map[x][y] != null) {
					JLabel locationLabel = (JLabel) gridPanel.getComponent((x*3) + y);
					locationLabel.setBackground(Color.WHITE);	
					locationLabel.setBorder(border);
					locationLabel.setFont(new Font(locationLabel.getFont().getName(), Font.BOLD, 14));
					locationLabel.setForeground(new Color(40,40,40));
					locationLabel.setOpaque(true);
				}
			}
		}
	}
	
	private static void selectItemOnGrid() {
		JLabel locationLabel = (JLabel) gridPanel.getComponent((selectedRow * 3) + selectedCol);
		locationLabel.setBackground(new Color(23,171,180));	
		Border border = BorderFactory.createLineBorder(Color.WHITE);
		locationLabel.setBorder(border);
		locationLabel.setForeground(Color.WHITE);
	}
	
	private static void updateErrorLog() {
		String errorText = "";
		for(int x = 0; x < errorLog.length; x++) {
			ErrorLogItem error = errorLog[x];
			errorText += error.getErrorMessage();
		}
		errorTextArea.setText(errorText);
	}
	
	private static void updateTravelLog() {
		String travelText = "";
		for(int x = 0; x < travelLog.length; x++) {
			TravelLogItem logItem = travelLog[x];
			travelText += logItem.toString()+"\n";
		}
		travelTextArea.setText(travelText);
	}
	
	private static void updateScore() {
		scoreLabel.setText("Score: "+score);
	}
	
	private static void warnAboutInvalidMove() {	
		ErrorLogItem[] updatedErrorLog = new ErrorLogItem[errorLog.length+1];
		for(int x = 0; x < errorLog.length; x++) {
			updatedErrorLog[x] = errorLog[x];
		}
		
		ErrorLogItem error = new ErrorLogItem("> Invalid move.\n");
		updatedErrorLog[updatedErrorLog.length-1] = error;
		
		errorLog = updatedErrorLog;
		updateErrorLog();
	}
}