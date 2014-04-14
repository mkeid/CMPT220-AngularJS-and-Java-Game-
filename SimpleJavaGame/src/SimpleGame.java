import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class SimpleGame {
	public static String name;
	
	// UI stuff
	private static JPanel bottomPanel;
	private static JLabel currentLocationLabel;
	private static JTextArea errorTextArea;
	private static JFrame frame;
	private static JPanel gridPanel;
	private static JButton magicShopButton;
	private static JLabel moveCountLabel;
	private static JLabel moveRatioLabel;
	private static JLabel scoreLabel;
	private static JPanel topPanel;
	private static JTextArea travelTextArea;
	
	// Shop stuff
	private static ArrayList<Item> shopItems;
	private static ArrayList<Integer> shopItemCosts;
	private static ArrayList<String> shopItemNames;
	
	// Directional buttons
	/*
	private static DirectionalButton eButton;
	private static DirectionalButton nButton;
	private static DirectionalButton sButton;
	private static DirectionalButton wButton;
	*/
	
	/*****************************************************************
	 * Directional buttons originally global because they were going 
	 * to be accessed by a separate function to alter their listener
	 * and opacity (to indicate whether or not one could click it)
	 ****************************************************************/
	
	
	// Game stuff
	private static Item[] inventory;
	private static boolean isAtMagicShop;
	private static Location[][] map;
	private static int moveCount;
	private static int score;
	private static int selectedCol;
	private static int selectedRow;
	private static TravelLogItem[] travelLog;
	private static ErrorLogItem[] errorLog;

	public static void main(String [] args) {
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
		score = 0;
		moveCount = 0;
		
		// Initialize Shop Stuff
		shopItems = new ArrayList<Item>();
		shopItemCosts = new ArrayList<Integer>();
		shopItemNames = new ArrayList<String>();
		
		// Initialize items
	    score = 0;
	    moveCount = 0;
		
	    // Initialize items
	    Item teleporter = new Item(0, "Teleporter", "This teleports you to Lowell Thomas.");
	    Item ball = new Item(1, "Ball", "It does nothing.");
	    Item gold = new Item(2, "Gold", "This was stolen from a chemist in Donnelly.");
	    Item saber = new Item(3, "Light Saber", "A saber made of light and stuff.");
	    
	    // Initialize locations
	    AcademicLocation donnelly = new AcademicLocation(0, "Donnelly", "This is where people science.", gold);
	    AcademicLocation dyson = new AcademicLocation(1, "Dyson", "All business here.", null);
	    AcademicLocation fontaine = new AcademicLocation(2, "Fontaine", "There is nothing to do here.", null);
	    AcademicLocation fontaineAnnex = new AcademicLocation(3, "Fontaine Annex", "Tiniest building. Largest lot.", saber);
	    AcademicLocation hancock = new AcademicLocation(4, "Hancock", "Computers and stuff.", null);
	    AcademicLocation library = new AcademicLocation(5, "Library", "This is where people study.", null);
	    AcademicLocation lowellThomas = new AcademicLocation(6, "Lowell Thomas", "People are communicating.", teleporter);
	    AcademicLocation mccann = new AcademicLocation(7, "McCann Center", "YEA SPORTS", ball);
	    ServiceLocation mcdonalds = new ServiceLocation(8, "McDonald's", "They serve food and stuff.", null);
	    AcademicLocation rotunda = new AcademicLocation(9, "Rotunda", "There is nothing to do here.", null);
	    AcademicLocation magicShop = new AcademicLocation(9, "Magick Shoppe", "You're at the magic shop.", null);
	    
	    try {
			initShop();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // Assign surrounding locations
        magicShop.setNearbyLocations(null, fontaineAnnex, null, null);
        fontaineAnnex.setNearbyLocations(null, mcdonalds, fontaine, magicShop);
        mcdonalds.setNearbyLocations(null, null, null, fontaineAnnex);

        fontaine.setNearbyLocations(fontaineAnnex, null, dyson, null);

        hancock.setNearbyLocations(null, dyson, rotunda, null);
        dyson.setNearbyLocations(fontaine, lowellThomas, library, hancock);
        lowellThomas.setNearbyLocations(null, null, donnelly, dyson);

        rotunda.setNearbyLocations(hancock, library, null, null);
        library.setNearbyLocations(dyson, donnelly, mccann, rotunda);
        donnelly.setNearbyLocations(lowellThomas, null, null, library);

        mccann.setNearbyLocations(library, null, null, null);

	    // Assign locations to places on the map
	    map = new Location[5][3];
	    Location[] row1 = {magicShop, fontaineAnnex, mcdonalds};
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
	
	private static void initShop() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/magicitems.txt"));
        while (scanner.hasNextLine()){
            Item item = new Item(0, scanner.nextLine(), "");
            Random generator = new Random();
            int i = 10 - generator.nextInt(10);
            item.setCost(i);

            shopItems.add(item);
            shopItemCosts.add(item.getCost());
            shopItemNames.add(item.getName() + ": "+ item.getCost()+"pts");
        }
        scanner.close();
	}
	
	public static void updateDirection(String direction) {
		TravelLogItem travelLogItem = travelLog[travelLog.length-1];
		Location currentLocation = travelLogItem.getLocation();
        Location changedLocation = moveLocation(currentLocation, direction);
        if(!currentLocation.getName().equals(changedLocation.getName())) {
        	resetGrid();
        	selectItemOnGrid();
        	currentLocationLabel.setText("Current Location: "+changedLocation.getName());
        	
        	updateMoveCount();
        	
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
			/*
			if(selectedRow != 0 && map[selectedRow - 1][selectedCol] != null) {
				changedLocation = map[selectedRow - 1][selectedCol];
				selectedRow--;
			}
			else warnAboutInvalidMove();
            */
            if(currentLocation.getNorthLocation() != null) {
                changedLocation = currentLocation.getNorthLocation();
                selectedRow--;
            }
            else warnAboutInvalidMove();
		} 
		else if(direction.equals("South")) {
            /*
			if(selectedRow != 4 && map[selectedRow + 1][selectedCol] != null) {
				changedLocation = map[selectedRow + 1][selectedCol];
				selectedRow++;
			}
			else warnAboutInvalidMove();
			*/
            if(currentLocation.getSouthLocation() != null) {
                changedLocation = currentLocation.getSouthLocation();
                selectedRow++;
            }
            else warnAboutInvalidMove();
		}
		else if(direction.equals("East")) {
            /*
			if(selectedCol != 2 && map[selectedRow][selectedCol + 1] != null) {
				changedLocation = map[selectedRow][selectedCol + 1];
				selectedCol++;
			}
			else warnAboutInvalidMove();
			*/
            if(currentLocation.getEastLocation() != null) {
                changedLocation = currentLocation.getEastLocation();
                selectedCol++;
            }
            else warnAboutInvalidMove();
		}
		else if(direction.equals("West")) {
            /*
			if(selectedCol != 0 && map[selectedRow][selectedCol - 1] != null) {
				changedLocation = map[selectedRow][selectedCol - 1];
				selectedCol--;
			}
			else warnAboutInvalidMove();
			*/
            if(currentLocation.getWestLocation() != null) {
                changedLocation = currentLocation.getWestLocation();
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
		initWorld();
		
		frame.getContentPane().removeAll();
		
		BorderLayout layout = new BorderLayout();
		frame.setLayout(layout);
		
		/***********************
		 * Top Section
		 **********************/
		topPanel = new JPanel();
		topPanel.setBackground(new Color(71,71,71));
		
		JButton resetButton = new JButton("Reset Game");
		resetButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) { showPlaySession(); }
	    });
		resetButton.setBackground(new Color(90,90,90));
		resetButton.setBorderPainted(false);
		resetButton.setFont(new Font(resetButton.getFont().getName(), Font.BOLD,15));
		resetButton.setForeground(Color.WHITE);
		resetButton.setOpaque(true);
		topPanel.add(resetButton);
		
		scoreLabel = new JLabel(" Score: 5", JLabel.CENTER);
		scoreLabel.setFont(new Font(scoreLabel.getFont().getName(), Font.BOLD, 20));
		scoreLabel.setForeground(Color.WHITE);
		topPanel.add(scoreLabel);
		
		JLabel topBreakLabel = new JLabel(" | ", JLabel.CENTER);
		topBreakLabel.setFont(new Font(scoreLabel.getFont().getName(), Font.BOLD, 24));
		topBreakLabel.setForeground(new Color(110,110,110));
		topPanel.add(topBreakLabel);
		
		moveCountLabel = new JLabel("Move Count: 0", JLabel.CENTER);
		moveCountLabel.setFont(new Font(scoreLabel.getFont().getName(), Font.BOLD, 20));
		moveCountLabel.setForeground(Color.WHITE);
		topPanel.add(moveCountLabel);
		
		JLabel topBreakLabel2 = new JLabel(" | ", JLabel.CENTER);
		topBreakLabel2.setFont(new Font(scoreLabel.getFont().getName(), Font.BOLD, 24));
		topBreakLabel2.setForeground(new Color(110,110,110));
		topPanel.add(topBreakLabel2);
		
		moveRatioLabel = new JLabel("Achievement Ratio: 0.00", JLabel.CENTER);
		moveRatioLabel.setFont(new Font(scoreLabel.getFont().getName(), Font.BOLD, 20));
		moveRatioLabel.setForeground(Color.WHITE);
		topPanel.add(moveRatioLabel);
		
		frame.add(topPanel, BorderLayout.PAGE_START);
		
		/***********************
		 * Bottom Section
		 **********************/
		bottomPanel = new JPanel();
		bottomPanel.setBackground(new Color(71,71,71));
		bottomPanel.setLayout(new FlowLayout());
		
		// Controller label
		JLabel controllerLabel = new JLabel("Controller");
		controllerLabel.setForeground(Color.WHITE);
		controllerLabel.setFont(new Font(scoreLabel.getFont().getName(), Font.BOLD, 16));
		bottomPanel.add(controllerLabel);
		
		setupDirectionalButtons();
		
		// Info on directional input
		JLabel infoInputLabel = new JLabel("Enter n|s|e|w to move. Enter 'i' for inventory. Enter 'h' for help. ");
		infoInputLabel.setForeground(Color.WHITE);
		infoInputLabel.setFont(new Font(infoInputLabel.getFont().getName(), Font.BOLD,12));
		bottomPanel.add(infoInputLabel);
		
		// Directional input
		final JTextField textField = new JTextField("",2);
		textField.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		       triedChangingDirection(textField);
		    }
		});
		bottomPanel.add(textField);
		
		// Go button
		JButton goButton = new JButton("GO");
		goButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) { 
	        	 triedChangingDirection(textField);
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
		
		JLabel mapLabel = new JLabel("Map");
		mapLabel.setFont(new Font(mapLabel.getFont().getName(), Font.BOLD, 20));
		mapPanel.add(mapLabel);
		
		currentLocationLabel = new JLabel("Current Location: Hancock");
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
				
				Border border = BorderFactory.createLineBorder(new Color(180,180,180), 1);

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
	    nameTextField.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	triedSubmittingName(nameTextField);
		    }
		});
	    JButton goButton = new JButton("PLAY");
	    goButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	             triedSubmittingName(nameTextField);
	         }
	    });
	    
	    JPanel namePanel = new JPanel();
	    namePanel.setLayout(new FlowLayout());
	    namePanel.add(nameTextField);
	    namePanel.add(goButton);
	    frame.add(namePanel);
	    
	    frame.validate();
	}
	
	private static void addToInventory(Item item) {
		Item[] updatedInventory = new Item[inventory.length+1];
		for(int x = 0; x < inventory.length; x++) if(inventory[x] != null) updatedInventory[x] = inventory[x];
		updatedInventory[inventory.length] = item;
		inventory = updatedInventory;
	}
	
	private static void declareName(String updatedName, boolean isInitialDeclaration) {
		name = updatedName;	
		frame.setTitle(name+"'s Adventure Game");
		if(isInitialDeclaration) showPlaySession();
	}
	
	private static void displayHelp() {
		String message = name+"'s Adventure Game is a simple game built with Java.\n";
		message += "It is based on the true life of "+name+" where he walks around campus all day collecting random items.\n";
		message += "To move around, press the directional buttons or enter one of the following directional key inputs: ( n | s | e | w )\n";
		message += "To open your inventory, enter 'i'.";
		message += "To quit, close the window or enter 'q'.";
		
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
		Border border = BorderFactory.createLineBorder(new Color(180,180,180), 1);
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
		
		Location location = map[selectedRow][selectedCol];
		if(location.getName().equalsIgnoreCase("Magick Shoppe")) {
			isAtMagicShop = true;
			magicShopButton = new JButton("Enter Magick Shoppe");
			magicShopButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					try {
						enterMagicShop();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		    });
			magicShopButton.setBackground(new Color(103,78,178));
			magicShopButton.setBorderPainted(false);
			magicShopButton.setFont(new Font(magicShopButton.getFont().getName(), Font.BOLD,15));
			magicShopButton.setForeground(Color.WHITE);
			magicShopButton.setOpaque(true);
			topPanel.add(magicShopButton);
		}
		else if(isAtMagicShop) {
			// remove magic shop button here
			topPanel.remove(magicShopButton);
			isAtMagicShop = false;
			frame.validate();
			frame.repaint();
		}
	}
	
	private static void enterMagicShop() throws FileNotFoundException {
		Item[] tempItems = shopItems.toArray(new Item[shopItems.size()]);
		int shopItemNamesSize = shopItemNames.size();
		String[] emptyStringArray = new String[shopItemNamesSize];
        String[] shopItemNamesArray = shopItemNames.toArray(emptyStringArray);
        String itemName = (String) JOptionPane.showInputDialog(null, 
        		"What would you like to buy?", 
        		"The Magick Shoppe", 
        		JOptionPane.QUESTION_MESSAGE, 
        		null, 
        		shopItemNamesArray, 
        		shopItemNames.get(0));
        int counter = 0;
        boolean isFound = false;
        while(counter < shopItems.size() && !isFound) {
            Item item = shopItems.get(counter);
            String itemNameWithCost = item.getName()+": "+item.getCost()+"pts";
            if(itemName.equals(itemNameWithCost)) {
                 if(score >= item.getCost()) {
                     addToInventory(item);
                     System.out.println("The cost was "+item.getCost()+" points.");
                     score -= item.getCost();
                     //JOptionPane.showMessageDialog(null, "The cost was "+item.getCost()+" points.");
                 }
                 else {
                     System.out.println("You don't have enough points to buy this item");
                     JOptionPane.showMessageDialog(null, "You don't have enough points to buy this item");
                 }
                 updateScore();
                 isFound = true;
            }
            counter++;
        }
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
		scoreLabel.setText(" Score: "+score);
	}
	
	private static void warnAboutInvalidInput() {	
		pushToErrorLog(new ErrorLogItem("> Invalid input.\n"));
	}
	
	private static void warnAboutInvalidMove() {	
		pushToErrorLog(new ErrorLogItem("> Invalid move.\n"));
	}
	
	private static void pushToErrorLog(ErrorLogItem logItem) {
		ErrorLogItem[] updatedErrorLog = new ErrorLogItem[errorLog.length+1];
		for(int x = 0; x < errorLog.length; x++) {
			updatedErrorLog[x] = errorLog[x];
		}
		updatedErrorLog[updatedErrorLog.length-1] = logItem;
		errorLog = updatedErrorLog;
		updateErrorLog();
	}
	
	private static void setupDirectionalButtons() {
		// North button
		DirectionalButton nButton = new DirectionalButton("North");
		nButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) { updateDirection("North"); }
	    });
		nButton.setBackground(new Color(92,184,92));
		nButton.setBorderPainted(false);
		nButton.setFont(new Font(nButton.getFont().getName(), Font.BOLD,15));
		nButton.setForeground(Color.WHITE);
		nButton.setOpaque(true);
		bottomPanel.add(nButton);
		
		// South button
		DirectionalButton sButton = new DirectionalButton("South");
		sButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) { updateDirection("South"); }
	    });
		sButton.setBackground(new Color(217,83,79));
		sButton.setBorderPainted(false);
		sButton.setFont(new Font(sButton.getFont().getName(), Font.BOLD,15));
		sButton.setForeground(Color.WHITE);
		sButton.setOpaque(true);
		bottomPanel.add(sButton);
		
		// East button
		DirectionalButton eButton = new DirectionalButton("East");
		eButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) { updateDirection("East"); }
	    });
		eButton.setBackground(new Color(66,139,202));
		eButton.setBorderPainted(false);
		eButton.setFont(new Font(eButton.getFont().getName(), Font.BOLD,15));
		eButton.setForeground(Color.WHITE);
		eButton.setOpaque(true);
		bottomPanel.add(eButton);
		
		// West button
		DirectionalButton wButton = new DirectionalButton("West");
		wButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) { updateDirection("West"); }
	    });
		wButton.setBackground(new Color(240,173,78));
		wButton.setBorderPainted(false);
		wButton.setFont(new Font(wButton.getFont().getName(), Font.BOLD,15));
		wButton.setForeground(Color.WHITE);
		wButton.setOpaque(true);
		bottomPanel.add(wButton);
	}
	
	private static void triedChangingDirection(JTextField textField) {
		 char input = textField.getText().toLowerCase().charAt(0);
	   	 if(input == 'n') updateDirection("North");
	   	 else if(input == 's') updateDirection("South");
	   	 else if(input == 'e') updateDirection("East");   
	   	 else if(input == 'w') updateDirection("West");
	   	 else if(input == 'i') displayInventory();
	   	 else if(input == 'h') displayHelp();
	   	 else if(input == 'q') frame.dispose();
	   	 else warnAboutInvalidInput();
	   	 textField.setText("");
	}
	
	private static void triedSubmittingName(JTextField nameTextField) {
		if(nameTextField.getText().length() > 0) {
	       	 String updatedName = nameTextField.getText();
	       	 updatedName = Character.toUpperCase(updatedName.charAt(0)) + updatedName.substring(1);
	       	 declareName(updatedName, true);
        }
	}
	
	private static void updateAchievementRatio() {
		float ratio = (float) score / moveCount;
		String ratioString = String.format("%.2f", ratio);
		moveRatioLabel.setText("Achievement Ratio: "+ratioString+" ");
	}
	
	private static void updateMoveCount() {
		moveCount++;
		moveCountLabel.setText("Move Count: "+moveCount);
		updateAchievementRatio();
	}
}
