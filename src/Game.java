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
            int startLocation = Integer.parseInt(args[0]);    // TODO We need to catch a possible exception here.
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
        stillPlaying = true;   // TODO: Do we need this?

        // Set up the location instances of the Locale class.
        Locale loc0 = new Locale(0);
        loc0.setName("Enemy Base");
        loc0.setDesc("You have entered the enemy base");
        loc0.setHasItem(true);

        Locale loc1 = new Locale(1);
        loc1.setName("Offworld");
        loc1.setDesc("You are offworld");

        Locale loc2 = new Locale(2);
        loc2.setName("Village");
        loc2.setDesc("You are in a village");
        loc2.setHasItem(true);

        Locale loc3 = new Locale(3);
        loc3.setName("Carter Office");
        loc3.setDesc("Colonel Carter\'s Office.");

        Locale loc4 = new Locale(4);
        loc4.setName("Gateroom");
        loc4.setDesc("You are in the gateroom");
        loc4.setHasVisited(true);
        loc4.setHasItem(true);

        Locale loc5 = new Locale(5);
        loc5.setName("Armory");
        loc5.setDesc("You are in the armory");
        loc5.setHasItem(true);

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

        // Set up the location instances of the Item class.
        Item item0 = new Item(0);
        item0.setName("map");
        item0.setDesc("You have picked up a map");

        Item item1 = new Item(1);
        item1.setName("intel");
        item1.setDesc("You have picked up enemy intelligence");

        Item item2 = new Item(2);
        item2.setName("gun");
        item2.setDesc("You have picked up an assault rifle");

        Item item3 = new Item(3);
        item3.setName("allies");
        item3.setDesc("You have gained allies");




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
        System.out.println(locations[currentLocale].getDesc());
    }

    private static void getCommand() {
        System.out.print("[" + moves + " moves, score: " + score + " achievement ratio: "+ score/moves +"] ");
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
        } else if ( command.equalsIgnoreCase("quit")  || command.equalsIgnoreCase("q")) {
            quit();
        } else if ( command.equalsIgnoreCase("help")  || command.equalsIgnoreCase("h")) {
            help();
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

    private static void help() {
        System.out.println("The commands are as follows:");
        System.out.println("   n/north");
        System.out.println("   s/south");
        System.out.println("   w/west");
        System.out.println("   e/east");
        System.out.println("   p/pickup");
        System.out.println("   q/quit");
    }

    private static void pickup() {
        if(locations[currentLocale].getHasItem() == true){

        }
    }

    private static void quit() {
        stillPlaying = false;
    }
}