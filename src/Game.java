import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    // Globals
    public static final boolean DEBUGGING = false;   // Debugging flag.
    public static final int MAX_LOCALES = 2;        // Total number of rooms/locations we have in the game.
    public static int currentLocale = 4;            // Player starts in locale 0.
    public static String command;                   // What the player types as he or she plays the game.
    public static boolean stillPlaying = true;      // Controls the game loop.
    public static boolean moved = false;            // Determines whether or not to print game information
    public static boolean buying = false;           // Determines if the player is trying to buy an item
    public static boolean guessing = false;         // Determines if the player is trying to guess the password
    public static Locale[] locations;               // An uninitialized array of type Locale. See init() for initialization.
    public static int[][]  nav;                     // An uninitialized array of type int int.
    public static int moves = 1;                    // Counter of the player's moves.
    public static int score = 5;                    // Tracker of the player's score.
    public static Item[] items;                     // An uninitialized array of type Item. See init() for initialization.
    public static Item[] inventory;                 // An array of Items that stores the players items they pickup
    public static ArrayList<ListItem> magicItemsInventory = new ArrayList<ListItem>();
    public static int inventoryCounter = 0;         // keeps track of # of items in inventory
    public static int coins = 100;                  // keeps track of coins used to buy items in magick shoppe
    public static int cheaterCounter = 0;           //used in case the player decides to mine for coins for too long
    public static int guessCounter = 5;             //ued to keep track of guesses for password
    public static MagicItemsList magicItems  = new MagicItemsList();

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
            if(moved == true){
                updateDisplay();
            }
            moved = false;
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
        loc0.setLookDesc("You have entered the Wraith's base. There are hundreds of Wraith soldiers in front of you.\n" +
                "In the distance you see a Hive Ship with the intel you need to retrieve.\n");
        loc0.setHasItem(true);
        loc0.setHasItem(true);
        loc0.setWhichItem(items[1]);

        Locale loc1 = new Locale(1);
        loc1.setName("Offworld");
        loc1.setDesc("You are offworld");
        loc1.setLookDesc("You gate to a barren world. You can gate east to a allied planet, west to the Wraith planet, or south back to Atlantis.");

        Locale loc2 = new Locale(2);
        loc2.setName("Village");
        loc2.setDesc("You are in a village");
        loc2.setHasItem(true);
        loc2.setHasItem(true);
        loc2.setWhichItem(items[3]);
        loc2.setLookDesc("You have gated to a friendly planet. The Athosians camp is in front of you. \n" +
                        " The camp is in the middle of a dense forest, it night here. The only light you see comes from the village. \n" +
                        "You will need to have some of them join you before you can attack the Wraith planet.");

        Locale loc3 = new Locale(3);
        loc3.setName("Carter Office");
        loc3.setDesc("You are in Colonel Carter\'s Office.");
        loc3.setLookDesc("You walk into carters office. She's laying on her table in a skimpy bikini, waiting for you. \n" +
        "Whoa. Calm down there dream boy. Enough with the fantasy. \n"+
        "Carter is sitting at her desk, she is reviewing mission files.");

        Locale loc4 = new Locale(4);
        loc4.setName("Gateroom");
        loc4.setDesc("You are in the gateroom");
        loc4.setLookDesc("In front of you is the Stargate. A powerful device that can transport you across the galaxy to other planets. \n" +
                    "There are both friendly and hostile races out there. Once you have fully explored Atlantis you should venture through the Stargate.");
        loc4.setHasVisited(true);
        loc4.setHasItem(true);
        loc4.setWhichItem(items[0]);

        Locale loc5 = new Locale(5);
        loc5.setName("Armory");
        loc5.setDesc("You are in the armory");
        loc5.setLookDesc("You have entered the armory. It is stocked with Assault Rifles, P90's, M9's, and C4. There are targets setup for practice. \n" +
                "Lt. Colonel John Shepard is practicing with Major Lorne.");
        loc5.setHasItem(true);
        loc5.setHasItem(true);
        loc5.setWhichItem(items[2]);

        Locale loc6 = new Locale(6);
        loc6.setName("Ronan Quarter");
        loc6.setDesc("You are in Ronan\'s Quarters");
        loc6.setLookDesc("Against logic you have entered the sleeping quarters of Ronan Dex. \n" +
        "There is some sort of animal rug on the floor, along with paintings of Sateda. Ronan is sleeping on his bed, better hope he doesn't wake up.");

        Locale loc7 = new Locale(7);
        loc7.setName("McKay Lab");
        loc7.setDesc("You are in Dr. Mckay\'s Lab");
        loc7.setLookDesc("You have entered Dr. Rodney Mckay's Laboratory. You look around and see rows of computer hardware\n" +
                        "and monitors. The tables are covered with random parts, and McKay's lunch. On the far wall you see a large\n" +
                        "machine with the name Magick Shoppe Prototype on it. The description reads: type buy to purchase items using coins.\n" +
                        "Mckay's laptop is on his desk. There's a note on it, it says if you can guess my password you will get a reward. \n" +
                        "Here's a hint: the birth years of the three smartest scientists and the answer to the question of life, the universe, and everything in it. \n" +
                        "To guess, type Guess Password, enter your guess after the prompt.");

        MagickItemShoppe loc8 = new MagickItemShoppe(8);
        loc8.setName("Cafe");
        loc8.setDesc("You are in the cafe");
        loc8.setLookDesc("You have entered the cafe. Many Atlantis personal are eating here. Today they are serving blue jello and Salisbury Steak");



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

    private static void createMagicItems() {
        // Create the list manager for our magic items.
        magicItems.setName("List of Magic Items");
        magicItems.setDesc("These are the magic items for purchase.");


        // Create some magic items and put them in the list.
        ListItem item1 = new ListItem();
        item1.setName("Wraith Stunner");
        item1.setDesc("Gun that stuns target");
        item1.setCost(10);

        ListItem item2 = new ListItem();
        item2.setName("ZPM");
        item2.setDesc("An ancient power source that extracts vacuum energy from an artificial region of subspace-time until it reaches maximum entropy");
        item2.setCost(200);

        ListItem item3 = new ListItem();
        item3.setName("Ancient Personal Shield");
        item3.setDesc("An ancient device that protects the wearer from harm");
        item3.setCost(50);

        // Link it all up.
        magicItems.setHead(item1);
        item1.setNext(item2);
        item2.setNext(item3);
        item3.setNext(null);

        System.out.println(magicItems.Shop());


    }

    private static void updateDisplay() {
        //System.out.println(locations[currentLocale].getName());

        if(locations[currentLocale].getHasItem() == true && locations[currentLocale].getHasVisited() == false){
            System.out.println(locations[currentLocale].getLookDesc() + ". " + locations[currentLocale].getWhichItem().getDesc());
        }
        else if(locations[currentLocale].getHasVisited() == false){
            System.out.println(locations[currentLocale].getLookDesc());
        }
        else if(locations[currentLocale].getHasItem() == true)
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
        System.out.print("[Current progress: " + moves + " moves, score: " + score + ", achievement ratio: " + score / moves + ", coins: " + coins + "] ");
        System.out.println();
        buying = false;
        //gives the player 5 points the first time they visit a location
        if(locations[currentLocale].getHasVisited() == false){
            score+=5;
            locations[currentLocale].setHasVisited(true);
        }
    }

    private static void getCommand(){
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
        } else if ( command.equalsIgnoreCase("map")  || command.equalsIgnoreCase("m") ) {
            showMap();
        } else if ( command.equalsIgnoreCase("buy")  || command.equalsIgnoreCase("b") ) {
            buyItems();
        } else if ( command.equalsIgnoreCase("look")  || command.equalsIgnoreCase("l") ) {
            look();
        } else if (command.equalsIgnoreCase("guess password")  || command.equalsIgnoreCase("guess"))  {
            password();
        } else if ( command.equalsIgnoreCase("quit")  || command.equalsIgnoreCase("q")) {
            quit();
        } else if ( command.equalsIgnoreCase("help")  || command.equalsIgnoreCase("h")) {
            help();
        } else if ( currentLocale == 7 && buying == true) {
            shop();
        } else if ( currentLocale == 7 && guessing == true) {
            guessPassword();
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
                moved=true;

            }
        }
    }

    private static void password() {
        if(currentLocale == 7){
            System.out.println("Laptop Login");
            System.out.println("Username: MeredithMcKay ");
            System.out.println("Password: ");
            guessing = true;
        }
        else{
            System.out.println("Go to McKay's Lab to purchase items.");
        }
    }

    private static void guessPassword() {
        if(guessCounter>0){
            if(command.equalsIgnoreCase("16431879196842")){
                System.out.println("You have cracked the password, Damn McKay is arrogant.");
                System.out.println("Your reward is 200 coins.");
                coins += 200;

            }
            else {
                System.out.println("Incorrect Password. Chances left: " + (guessCounter-1));
                System.out.println("To try again type, guess password");
            }
            guessing = false;
            guessCounter--;
        }
        else{
            System.out.println("You have guessed too many times, the laptop encrypted itself and is now inaccessible");
        }
    }

    private static void shop() {

        ListItem currentItem = magicItems.getHead();
        Boolean boughtSomething = false;
        while (currentItem != null) {
            if(command.equalsIgnoreCase(currentItem.getName())){
                if(coins >= currentItem.getCost()){
                    System.out.println("You have purchased a " + currentItem.getName());
                    System.out.println("Thank you for testing out Dr. McKays's Prototype Magick Shoppe. Please come again. ");
                    coins-=currentItem.getCost();
                    boughtSomething = true;
                    magicItemsInventory.add(currentItem);
                }
                else{
                    System.out.println("McKay set the price too high, you don't have enough coins to afford this item.");
                    boughtSomething = true;
                }

            }

            currentItem = currentItem.getNext();
        }
        buying = false;
        if(boughtSomething == false){
            System.out.println("That is not an item McKay has stocked the machine with. ");
        }
    }

    //displays the available actions to the player
    private static void help() {
        System.out.println("The commands are as follows:");
        System.out.println("   n/north");
        System.out.println("   s/south");
        System.out.println("   w/west");
        System.out.println("   e/east");
        System.out.println("   l/look");
        System.out.println("   p/pickup");
        System.out.println("   i/inventory");
        System.out.println("   m/map");
        System.out.println("   b/buy");
        System.out.println("   q/quit");
    }

    //allows the user to pickup items
    private static void pickup() {
        if(locations[currentLocale].getHasItem() == true){
            inventory[inventoryCounter] = locations[currentLocale].getWhichItem();
            System.out.println("You have picked up a " + inventory[inventoryCounter].getName());
            System.out.println("You also found 25 coins.");
            coins+=25;
            locations[currentLocale].setHasItem(false);
            inventoryCounter++;
        }
        else{
            System.out.println("There is no item here to pickup.");
            System.out.println("But you found 5 coins.");
            coins+=5;
            cheaterCounter++;
            if(cheaterCounter >= 25){
                System.out.println("You think you're smart, think you found a way to cheat the system and get a shit load of coins? ");
                System.out.println("Well guess what");
                System.out.println("An ascended ancient has appeared and chastises you for being greedy. ");
                System.out.println("As punishment you lose all your coins. ");
                System.out.println("And Ronan punches you in the face. ");
                coins = 0;
                cheaterCounter = 0;
            }
        }
    }

    private static void showInventory() {
        int i=0;
        System.out.print("You currently have ");
        while(inventory[i]!=null){
            System.out.print(inventory[i].getName()+ ", ");
            i++;
        }
        for(ListItem item: magicItemsInventory){
            System.out.print(item.getName() + ", ");
        }

        System.out.print("in your inventory.");
        System.out.println();

    }

    private static void buyItems() {
        if(currentLocale == 7){
            System.out.println("Welcome to the Magick Shoppe protoype created by Dr. Rodney McKay");
            System.out.println("These are the items available for purchase: ");
            createMagicItems();
            System.out.println("What would you like to buy?");
            buying = true;
        }
        else{
            System.out.println("Go to McKay's Lab to purchase items.");
        }
    }

    private static void look() {
        System.out.println(locations[currentLocale].getLookDesc());
    }

    private static void showMap() {
        boolean hasMap = false;
        for(int i=0; i<inventoryCounter; i++){
            if(inventory[i].getName() == "map"){
                hasMap = true;
            }
        }

        if(hasMap == true){
            if(currentLocale == 0){
                System.out.println(" _____________________________");
                System.out.println("|    *    |         |         |");
                System.out.println("|EnemyBase|Offworld | Village |");
                System.out.println("|_________|_________|_________|");
                System.out.println("|Carter/'s|         |         |");
                System.out.println("| Office  |Gateroom | Armory  |");
                System.out.println("|_________|_________|_________|");
                System.out.println("|Ronan/'s |McKay/'s |         |");
                System.out.println("|Quarters |   Lab   |The Cafe |");
                System.out.println("|_________|_________|_________|");
            }
            if(currentLocale == 1){
                System.out.println(" _____________________________");
                System.out.println("|         |    *    |         |");
                System.out.println("|EnemyBase|Offworld | Village |");
                System.out.println("|_________|_________|_________|");
                System.out.println("|Carter/'s|         |         |");
                System.out.println("| Office  |Gateroom | Armory  |");
                System.out.println("|_________|_________|_________|");
                System.out.println("|Ronan/'s |McKay/'s |         |");
                System.out.println("|Quarters |   Lab   |The Cafe |");
                System.out.println("|_________|_________|_________|");
            }
            if(currentLocale == 2){
                System.out.println(" _____________________________");
                System.out.println("|         |         |    *    |");
                System.out.println("|EnemyBase|Offworld | Village |");
                System.out.println("|_________|_________|_________|");
                System.out.println("|Carter/'s|    *    |         |");
                System.out.println("| Office  |Gateroom | Armory  |");
                System.out.println("|_________|_________|_________|");
                System.out.println("|Ronan/'s |McKay/'s |         |");
                System.out.println("|Quarters |   Lab   |The Cafe |");
                System.out.println("|_________|_________|_________|");
            }
            if(currentLocale == 3){
                System.out.println(" _____________________________");
                System.out.println("|         |         |         |");
                System.out.println("|EnemyBase|Offworld | Village |");
                System.out.println("|_________|_________|_________|");
                System.out.println("|Carter/'s|         |         |");
                System.out.println("| Office  |Gateroom | Armory  |");
                System.out.println("|____*____|_________|_________|");
                System.out.println("|Ronan/'s |McKay/'s |         |");
                System.out.println("|Quarters |   Lab   |The Cafe |");
                System.out.println("|_________|_________|_________|");
            }
            if(currentLocale == 4){
                System.out.println(" _____________________________");
                System.out.println("|         |         |         |");
                System.out.println("|EnemyBase|Offworld | Village |");
                System.out.println("|_________|_________|_________|");
                System.out.println("|Carter/'s|    *    |         |");
                System.out.println("| Office  |Gateroom | Armory  |");
                System.out.println("|_________|_________|_________|");
                System.out.println("|Ronan/'s |McKay/'s |         |");
                System.out.println("|Quarters |   Lab   |The Cafe |");
                System.out.println("|_________|_________|_________|");
            }
            if(currentLocale == 5){
                System.out.println(" _____________________________");
                System.out.println("|         |         |         |");
                System.out.println("|EnemyBase|Offworld | Village |");
                System.out.println("|_________|_________|_________|");
                System.out.println("|Carter/'s|         |    *    |");
                System.out.println("| Office  |Gateroom | Armory  |");
                System.out.println("|_________|_________|_________|");
                System.out.println("|Ronan/'s |McKay/'s |         |");
                System.out.println("|Quarters |   Lab   |The Cafe |");
                System.out.println("|_________|_________|_________|");
            }
            if(currentLocale == 6){
                System.out.println(" _____________________________");
                System.out.println("|         |         |         |");
                System.out.println("|EnemyBase|Offworld | Village |");
                System.out.println("|_________|_________|_________|");
                System.out.println("|Carter/'s|         |         |");
                System.out.println("| Office  |Gateroom | Armory  |");
                System.out.println("|_________|_________|_________|");
                System.out.println("|Ronan/'s |McKay/'s |         |");
                System.out.println("|Quarters |   Lab   |The Cafe |");
                System.out.println("|____*____|_________|_________|");
            }
            if(currentLocale == 7){
                System.out.println(" _____________________________");
                System.out.println("|         |         |         |");
                System.out.println("|EnemyBase|Offworld | Village |");
                System.out.println("|_________|_________|_________|");
                System.out.println("|Carter/'s|    *    |         |");
                System.out.println("| Office  |Gateroom | Armory  |");
                System.out.println("|_________|_________|_________|");
                System.out.println("|Ronan/'s |McKay/'s |         |");
                System.out.println("|Quarters |   Lab   |The Cafe |");
                System.out.println("|_________|____*____|_________|");
            }
            if(currentLocale == 8){
                System.out.println(" _____________________________");
                System.out.println("|         |         |         |");
                System.out.println("|EnemyBase|Offworld | Village |");
                System.out.println("|_________|_________|_________|");
                System.out.println("|Carter/'s|    *    |         |");
                System.out.println("| Office  |Gateroom | Armory  |");
                System.out.println("|_________|_________|_________|");
                System.out.println("|Ronan/'s |McKay/'s |    *    |");
                System.out.println("|Quarters |   Lab   |The Cafe |");
                System.out.println("|_________|_________|_________|");
            }
        }
        else {
            System.out.println("You do not have the map.");
        }
    }

    private static void quit() {
        stillPlaying = false;
    }
}