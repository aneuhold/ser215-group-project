
public enum ShipType {

    // Ship (Size, Health)
    BATTLESHIP(4, 4, "Battleship", "Horz"),
    CRUISER(3, 3, "Cruiser", "Horz"),
    SUBMARINE(3, 3, "Submarine", "Horz"),
    DESTROYER(2, 2, "Destroyer", "Horz"),
    CARRIER(5, 5, "Carrier", "Horz");

    int size;
    int health;
    String name = "";
    String orientation;

    // private constructor for enum ShipType
// use ShipPiece constructor if wanting to create a ship object
    private ShipType(int size, int health, String name, String orientation) {
        this.size = size;
        this.health = health;
        this.name = name;
        this.orientation = orientation;
    }

}