import java.util.Scanner;

public class Game {

    // Globals
    public static final boolean DEBUGGING = false;   // Debugging flag.
    public static final int MAX_LOCALES = 2;        // Total number of rooms/locations we have in the game.
    public static int currentLocale = 4;            // Player starts in locale 0.
    public static String command;                   // What the player types as he or she plays the game.
    public static boolean stillPlaying = true;      // Controls the game loop.
    public static Locale[] locations;               // An uninitialized array of type Locale. See init() for initialization.
    public static int[][]  nav;                     // An uninitialized array of type int int.
    public static int moves = 1;                    // Counter of the player's moves.
    public static int score = 5;                    // Tracker of the player's score.
    public static Item[] items;                     // An uninitialized array of type Item. See init() for initialization.
    public static Item[] inventory;                 // An array of Items that stores the players items they pickup
    public static int inventoryCounter = 0;

    public static void main(String[] args) {
        if (DEBUGGING) {
            // Display the command line args.
            System.out.println("Starting with args:");
            for (int i = 0; i < args.length; i++) {
                System.out.println(i + ":" + args[i]);
            }
        }

        // Set starting locale, if it was provided as a command line parameter.
        if (args.length > 0) {
            int startLocation = Integer.parseInt(args[0]);
            // Check that the passed-in value for startLocation is within the range of actual locations.
            if ( startLocation >= 0 && startLocation <= MAX_LOCALES) {
                currentLocale = startLocation;
            }
        }

        // Get the game started.
        init();
        updateDisplay();


        // Game Loop
        while (stillPlaying) {
            getCommand();
            navigate();
            updateDisplay();
        }

        // We're done. Thank the player and exit.
        System.out.println("Thank you for playing.");
    }


    private static void init() {
        // Initialize any uninitialized globals.
        command = new String();
        stillPlaying = true;

        // Set up the item instances of the Item class.
        Item item0 = new Item(0);
        item0.setName("map");
        item0.setDesc("There is a map available for pickup.");

        Item item1 = new Item(1);
        item1.setName("intel");
        item1.setDesc("There is enemy intelligence available for pickup.");

        Item item2 = new Item(2);
        item2.setName("gun");
        item2.setDesc("There is an assault rifle available for pickup.");

        Item item3 = new Item(3);
        item3.setName("allies");
        item3.setDesc("There are allies available for pickup.");

        //array of all items in game
        items = new Item[4];
        items[0] = item0; //map
        items[1] = item1; //intel
        items[2] = item2; //gun
        items[3] = item3; //allies

        inventory = new Item[4]; //creates the array to store items when the player picks them up

        // Set up the location instances of the Locale class.
        Locale loc0 = new Locale(0);
        loc0.setName("Enemy Base");
        loc0.setDesc("You have entered the enemy base");
        loc0.setHasItem(true);
        loc0.setHasItem(true);
        loc0.setWhichItem(items[1]);

        Locale loc1 = new Locale(1);
        loc1.setName("Offworld");
        loc1.setDesc("You are offworld");

        Locale loc2 = new Locale(2);
        loc2.setName("Village");
        loc2.setDesc("You are in a village");
        loc2.setHasItem(true);
        loc2.setHasItem(true);
        loc2.setWhichItem(items[3]);

        Locale loc3 = new Locale(3);
        loc3.setName("Carter Office");
        loc3.setDesc("Colonel Carter\'s Office.");

        Locale loc4 = new Locale(4);
        loc4.setName("Gateroom");
        loc4.setDesc("You are in the gateroom");
        loc4.setHasVisited(true);
        loc4.setHasItem(true);
        loc4.setWhichItem(items[0]);

        Locale loc5 = new Locale(5);
        loc5.setName("Armory");
        loc5.setDesc("You are in the armory");
        loc5.setHasItem(true);
        loc5.setHasItem(true);
        loc5.setWhichItem(items[2]);

        Locale loc6 = new Locale(6);
        loc6.setName("Ronan Quarter");
        loc6.setDesc("You are in Ronan\'s Quarters");

        Locale loc7 = new Locale(7);
        loc7.setName("McKay Lab");
        loc7.setDesc("You are in Dr. Mckay\'s Lab");

        Locale loc8 = new Locale(8);
        loc8.setName("Cafe");
        loc8.setDesc("You are in the cafe");


        // Set up the location array.
        locations = new Locale[9];
        locations[0] = loc0; // "Enemy Base"
        locations[1] = loc1; // "OffWorld"
        locations[2] = loc2; // "Village"
        locations[3] = loc3; // "Colonel Carter Office"
        locations[4] = loc4; // "Gateroom"
        locations[5] = loc5; // "Armory"
        locations[6] = loc6; // "Ronan Quarter"
        locations[7] = loc7; // "McKay Lab"
        locations[8] = loc8; // "Cafe"

        if (DEBUGGING) {
            System.out.println("All game locations:");
            for (int i = 0; i < locations.length; ++i) {
                System.out.println(i + ":" + locations[i].toString());
            }
        }
        nav = new int[][] {
                                 /* N   S   E   W */
                                 /* 0   1   2   3 */
         /* nav[0] for loc 0 */  { -1,  3,  1, -1 },
         /* nav[1] for loc 1 */  { -1,  4,  2,  0 },
         /* nav[2] for loc 2 */  { -1,  5, -1,  1 },
         /* nav[0] for loc 3 */  {  0,  6,  4, -1 },
         /* nav[1] for loc 4 */  {  1,  7,  5,  3 },
         /* nav[2] for loc 5 */  {  2,  8, -1,  4 },
         /* nav[0] for loc 6 */  {  3, -1,  7, -1 },
         /* nav[1] for loc 7 */  {  4, -1,  8,  6 },
         /* nav[2] for loc 8 */  {  5, -1, -1,  7 },

        };
    }

    private static void updateDisplay() {
        //System.out.println(locations[currentLocale].getName());
        if(locations[currentLocale].getHasItem() == true)
        {
            System.out.println(locations[currentLocale].getDesc() + ". " + locations[currentLocale].getWhichItem().getDesc());
        }
        else{
            System.out.println(locations[currentLocale].getDesc());
        }
        System.out.print("From here the directions you can move are: ");
        if(nav[currentLocale][0]!=-1){
            System.out.print("North ");
        }
        if(nav[currentLocale][1]!=-1){
            System.out.print("South ");
        }
        if(nav[currentLocale][2]!=-1){
            System.out.print("East ");
        }
        if(nav[currentLocale][3]!=-1){
            System.out.print("West ");
        }
        System.out.println();
    }

    private static void getCommand() {
        System.out.print("[Current progress: " + moves + " moves, score: " + score + " achievement ratio: "+ score/moves +"] ");
        Scanner inputReader = new Scanner(System.in);
        command = inputReader.nextLine();  // command is global.
    }

    private static void navigate() {
        final int INVALID = -1;
        int dir = INVALID;  // This will get set to a value > 0 if a direction command was entered.

        if (        command.equalsIgnoreCase("north") || command.equalsIgnoreCase("n") ) {
            dir = 0;
        } else if ( command.equalsIgnoreCase("south") || command.equalsIgnoreCase("s") ) {
            dir = 1;
        } else if ( command.equalsIgnoreCase("east")  || command.equalsIgnoreCase("e") ) {
            dir = 2;
        } else if ( command.equalsIgnoreCase("west")  || command.equalsIgnoreCase("w") ) {
            dir = 3;

        } else if ( command.equalsIgnoreCase("pick")  || command.equalsIgnoreCase("pickup") || command.equalsIgnoreCase("p")) {
           pickup();
        } else if ( command.equalsIgnoreCase("inventory")  || command.equalsIgnoreCase("i") ) {
            showInventory();
        } else if ( command.equalsIgnoreCase("quit")  || command.equalsIgnoreCase("q")) {
            quit();
        } else if ( command.equalsIgnoreCase("help")  || command.equalsIgnoreCase("h")) {
            help();
        } else{
            System.out.println("That is an invalid command. Type help to see a list of commands.");
        };

        if (dir > -1) {   // This means a dir was set.
            int newLocation = nav[currentLocale][dir];
            if (newLocation == INVALID) {
                System.out.println("You cannot go that way.");
            } else {
                currentLocale = newLocation;
                moves = moves + 1;
                if(locations[currentLocale].getHasVisited() == false){
                    score+=5;
                    locations[currentLocale].setHasVisited(true);
                }
            }
        }
    }

    //displays the available actions to the player
    private static void help() {
        System.out.println("The commands are as follows:");
        System.out.println("   n/north");
        System.out.println("   s/south");
        System.out.println("   w/west");
        System.out.println("   e/east");
        System.out.println("   p/pickup");
        System.out.println("   i/inventory");
        System.out.println("   q/quit");
    }

    //allows the user to pickup items
    private static void pickup() {
        if(locations[currentLocale].getHasItem() == true){
            inventory[inventoryCounter] = locations[currentLocale].getWhichItem();
            System.out.println("You have picked up a " + inventory[inventoryCounter].getName());
            locations[currentLocale].setHasItem(false);
            inventoryCounter++;
        }
        else{
            System.out.println("There is no item here to pickup.");

        }
    }

    private static void showInventory() {
        int i=0;
        System.out.print("You currently have ");
        while(inventory[i]!=null){
            System.out.print(inventory[i].getName()+ ", ");
            i++;
        }
        System.out.print("in your inventory.");
        System.out.println();

    }

    private static void quit() {
        stillPlaying = false;
    }
}