function Error(message) {
    this.message = message;
}

function Item(id, name, description) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.toString = function() {
        return this.name + " | " + this.description;
    }
}


function Location(id, name, description, item) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.item = item;
    this.toString = function() {
        return this.name + " | " + this.description;
    } 
    this.visitCount = 0;
}