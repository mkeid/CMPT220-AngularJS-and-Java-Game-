function init(){}

function Simple($scope) {
    // Initialize the error log
    $scope.errors = [];
    
    // For showing / hiding direction buttons
    $scope.showN = false;
    $scope.showS = true;
    $scope.showE = true;
    $scope.showW = true;
    // For showing / hiding inventory and help menu
    $scope.showI = false;
    $scope.showH = false;
    
    // Initialize items
    var teleporter = new Item(0, "Teleporter", "This teleports you to Lowell Thomas.");
    var ball = new Item(1, "Ball", "It does nothing.");
    var gold = new Item(2, "Gold", "This was stolen from a chemist in Donnelly.");
    var saber = new Item(3, "Light Saber", "A saber made of light and stuff.");
    
    // Initialize locations
    var donnelly = new Location(0, "Donnelly", "This is where people science.", gold);
    var dyson = new Location(1, "Dyson", "All business here.", null);
    var fontaine = new Location(2, "Fontaine", "There is nothing to do here.", null);
    var fontaineAnnex = new Location(3, "Fontaine Annex", "Smallest building. Largest parking lot.", saber);
    var hancock = new Location(4, "Hancock", "Computers and stuff.", null);
    var library = new Location(5, "Library", "This is where people study sometimes.", null);
    var lowellThomas = new Location(6, "Lowell Thomas", "Shh people are communicating.", teleporter);
    var mccann = new Location(7, "McCann Center", "YEA SPORTS", ball);
    var mcdonalds = new Location(8, "McDonald's", "They serve food and stuff.", null);
    var rotunda = new Location(9, "Rotunda", "There is nothing to do here.", null);
    
    // Initialize the map
    $scope.map = [
        [ null, fontaineAnnex, mcdonalds ], 
        [ null, fontaine, null ], 
        [ hancock, dyson, lowellThomas ], 
        [ rotunda, library, donnelly ], 
        [ null, mccann, null ], 
    ];
    
    // Create the possible directions
    var possibleDirections = ["North", "South", "East", "West"];
    
    // Start with no items
    $scope.inventory = [];
    // Start at Hancock
    hancock.visitCount = 1;
    $scope.score = 5;
    $scope.selectedRow = 2;
    $scope.selectedCol = 0;  
    $scope.visitedLocations = [hancock];
    
    
    $scope.updateLocation = function(direction) { 
        var currentLocation = $scope.visitedLocations[$scope.visitedLocations.length-1];
        var changedLocation = moveLocation(currentLocation, direction);
        if(currentLocation != changedLocation) {
            $scope.visitedLocations.push(changedLocation);
            
            // For showing / hiding direction Buttons
            if($scope.selectedRow != 0 && $scope.map[$scope.selectedRow - 1][$scope.selectedCol]) {
                $scope.showN = true;
            }
            else { $scope.showN = false; }
            
            if($scope.selectedRow != 4 && $scope.map[$scope.selectedRow + 1][$scope.selectedCol]) {
                $scope.showS = true;
            }
            else { $scope.showS = false; }
            
            if($scope.map[$scope.selectedRow][$scope.selectedCol + 1]) {
                $scope.showE = true;
            }
            else { $scope.showE = false; }
            
            if($scope.map[$scope.selectedRow][$scope.selectedCol - 1]) {
                $scope.showW = true;
            }
            else { $scope.showW = false; }
        }
     }
    
    function moveLocation(currentLocation, direction) {
        var changedLocation = currentLocation;
        
        switch(direction) {
            case "North":
                if($scope.selectedRow != 0 && $scope.map[$scope.selectedRow - 1][$scope.selectedCol]) {
                    changedLocation = $scope.map[$scope.selectedRow - 1][$scope.selectedCol];
                    $scope.selectedRow--;
                }
                else {
                    pushDirectionError();
                }
                break;
            case "South":
                if($scope.selectedRow != 4 && $scope.map[$scope.selectedRow + 1][$scope.selectedCol]) {
                    changedLocation = $scope.map[$scope.selectedRow + 1][$scope.selectedCol];
                    $scope.selectedRow++;
                }
                else {
                    pushDirectionError();
                }
                break;
            case "East":
                if($scope.selectedCol != 2 && $scope.map[$scope.selectedRow][$scope.selectedCol + 1]) {
                    changedLocation = $scope.map[$scope.selectedRow][$scope.selectedCol + 1];
                    $scope.selectedCol++;
                }
                else {
                    pushDirectionError();
                }
                break;
            case "West":
                if($scope.selectedCol != 0 && $scope.map[$scope.selectedRow][$scope.selectedCol - 1]) {
                    changedLocation = $scope.map[$scope.selectedRow][$scope.selectedCol - 1];
                    $scope.selectedCol--;
                }
                else {
                    pushDirectionError();
                }
                break;
        }
        
        if(changedLocation != currentLocation) {
            if(changedLocation.visitCount == 0) {
                $scope.score += 5;
                if(changedLocation.item) {
                    $scope.inventory.push(changedLocation.item);
                }
            }
            changedLocation.visitCount++;
        }
        
        return changedLocation;
    }
    
    // This is the function executed when one clicks the go button
    $scope.go = function(input) {
        var possibleInputDirections = ["n","s","e","w"];
        var inputChar = input.toLowerCase();
        if(possibleInputDirections.indexOf(inputChar) >= 0) {
            var direction;
            switch(inputChar) {
                case "n":
                    direction = possibleDirections[0];
                    break;
                case "s":
                    direction = possibleDirections[1];
                    break;
                case "e":
                    direction = possibleDirections[2];
                    break;
                case "w":
                    direction = possibleDirections[3];
                    break;
            }
            $scope.updateLocation(direction);
        }
        else if(inputChar == "i") {
            if($scope.showI) $scope.showI = false;
            else $scope.showI = true;
        }
        else if(inputChar == "h") {
            if($scope.showH) $scope.showH = false;
            else $scope.showH = true;
        }
        else {
            var error = new Error("That is not a valid character for input. Enter 'h' for help.");
            $scope.errors.push(error);
        }
    }
    
    // This is the function executed when on presses enter in the text field. It will call the go function
    $scope.goEnter = function(event, input) {
        if(event.which == 13) $scope.go(input);
    }
    
    // This creates an error alerting the user that he or she cannot move in a certain direction
    function pushDirectionError() {
        var error = new Error("You cannot go this way.");
        $scope.errors.push(error);
    }
}