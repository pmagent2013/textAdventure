import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    // Globals
    public static final boolean DEBUGGING = false;   // Debugging flag.
    public static final int MAX_LOCALES = 9;        // Total number of rooms/locations we have in the game.
    public static int currentLocale = 4;            // Player starts in locale 4.
    public static String command;                   // What the player types as he or she plays the game.
    public static boolean stillPlaying = true;      // Controls the game loop.
    public static boolean moved = false;            // Determines whether or not to print game information
    public static boolean buying = false;           // Determines if the player is trying to buy an item
    public static boolean guessing = false;         // Determines if the player is trying to guess the password
    public static boolean allies = false;           // Determines if the player has successfully gained allies
    public static Locale[] locations;               // An uninitialized array of type Locale. See init() for initialization.
    public static int[][]  nav;                     // An uninitialized array of type int int.
    public static int moves = 1;                    // Counter of the player's moves.
    public static int score = 5;                    // Tracker of the player's score.
    public static int enemyCount = 200;             // Change this to increase/decrease difficulty
    public static int totalPower = 0;               // Keeps track of the total power the player has / damage they can do
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

        Item item2 = new Item(2);
        item2.setName("gun");
        item2.setDesc("There is an assault rifle available for pickup.");
        item2.setPower(5);

        Item item3 = new Item(3);
        item3.setName("allies");
        item3.setDesc("There are allies available for pickup.");

        Item item4 = new Item(4);
        item4.setName("blue jello");
        item4.setDesc("There is blue jello available for pickup.");

        //array of all items in game
        items = new Item[5];
        items[0] = item0; //map
        items[2] = item2; //gun
        items[3] = item3; //allies
        items[4] = item4; //blue jello

        inventory = new Item[5]; //creates the array to store items when the player picks them up


        // Set up the location instances of the Locale class.
        OffWorld loc0 = new OffWorld(0);
        loc0.setName("Enemy Base");
        loc0.setDesc("You have entered the enemy base");
        loc0.setLookDesc("You have entered the Wraith's base. There are " +loc0.getNumberOfInhabitants() + " Wraith soldiers in front of you.\n" +
                "In the distance you see a Hive Ship with the intel you need to retrieve.\n" +
                "If you think you are strong enough you can begin your attack");
        loc0.setInhabitants("Wraith");
        loc0.setNumberOfInhabitants(200);

        OffWorld loc1 = new OffWorld(1);
        loc1.setName("Offworld");
        loc1.setDesc("You are offworld");
        loc1.setLookDesc("You gate to a barren world. You can gate east to a allied planet, west to the Wraith planet, or south back to Atlantis.");
        loc1.setInhabitants("None");
        loc1.setNumberOfInhabitants(0);

        OffWorld loc2 = new OffWorld(2);
        loc2.setName("Village");
        loc2.setDesc("You are in a village");
        loc2.setHasItem(true);
        loc2.setWhichItem(items[3]);
        loc2.setLookDesc("You have gated to a friendly planet. The Athosians camp is in front of you. \n" +
                " The camp is in the middle of a dense forest, it night here. The only light you see comes from the village. \n" +
                "You will need to have some of them join you before you can attack the Wraith planet.");
        loc2.setInhabitants("Athosians");
        loc2.setNumberOfInhabitants(250);

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
        loc5.setWhichItem(items[2]);

        Locale loc6 = new Locale(6);
        loc6.setName("Ronan Quarter");
        loc6.setDesc("You are in Ronan\'s Quarters");
        loc6.setLookDesc("Against logic you have entered the sleeping quarters of Ronan Dex. \n" +
                "There is some sort of animal rug on the floor, along with paintings of Sateda. Ronan is sleeping on his bed, better hope he doesn't wake up.");

        Locale loc7 = new Locale(8);
        loc7.setName("Cafe");
        loc7.setDesc("You are in the cafe");
        loc7.setLookDesc("You have entered the cafe. Many Atlantis personal are eating here. Today they are serving blue jello and Salisbury Steak");
        loc7.setHasItem(true);
        loc7.setWhichItem(items[4]);

        Locale loc8 = new Locale(8);
        loc8.setName("McKay Lab");
        loc8.setDesc("You are in Dr. Mckay\'s Lab");
        loc8.setLookDesc("You have entered Dr. Rodney Mckay's Laboratory. You look around and see rows of computer hardware\n" +
                "and monitors. The tables are covered with random parts, and McKay's lunch. On the far wall you see a large\n" +
                "machine with the name Magick Shoppe Prototype on it. The description reads: type buy to purchase items using coins.\n" +
                "Mckay's laptop is on his desk. There's a note on it, it says if you can guess my password you will get a reward. \n" +
                "Here's a hint: the birth years of the three smartest scientists and the answer to the question of life, the universe, and everything in it. \n" +
                "To guess, type Guess Password, enter your guess after the prompt.");





        // Set up the location array.
        locations = new Locale[9];
        locations[0] = loc0; // "Enemy Base"
        locations[1] = loc1; // "OffWorld"
        locations[2] = loc2; // "Village"
        locations[3] = loc3; // "Colonel Carter Office"
        locations[4] = loc4; // "Gateroom"
        locations[5] = loc5; // "Armory"
        locations[6] = loc6; // "Ronan Quarter"
        locations[7] = loc7; // "Cafe"
        locations[8] = loc8; // "McKay Lab"

        if (DEBUGGING) {
            System.out.println("All game locations:");
            for (int i = 0; i < locations.length; ++i) {
                System.out.println(i + ":" + locations[i].toString());
            }
        }
        nav = new int[][] {
                                 /* N   S   E   W */
                                 /* 0   1   2   3 */
         /* nav[0] for loc 0 */  { -1, -1,  1, -1 },
         /* nav[1] for loc 1 */  { -1,  4,  2,  0 },
         /* nav[2] for loc 2 */  { -1, -1, -1,  1 },
         /* nav[0] for loc 3 */  { -1, -1,  4, -1 },
         /* nav[1] for loc 4 */  {  1,  7,  5,  3 },
         /* nav[2] for loc 5 */  { -1,  8, -1,  4 },
         /* nav[0] for loc 6 */  { -1, -1,  7, -1 },
         /* nav[1] for loc 7 */  {  4, -1,  8,  6 },
         /* nav[2] for loc 8 */  {  5, -1, -1,  7 },

        };
        createMagicItems();
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
        item1.setPower(3);

        ListItem item2 = new ListItem();
        item2.setName("ZPM");
        item2.setDesc("An ancient power source that extracts vacuum energy from an artificial region of subspace-time until it reaches maximum entropy");
        item2.setCost(200);
        item2.setPower(110);

        ListItem item3 = new ListItem();
        item3.setName("Ancient Personal Shield");
        item3.setDesc("An ancient device that protects the wearer from harm");
        item3.setCost(20);
        item3.setPower(7);

        ListItem item4 = new ListItem();
        item4.setName("ATA Gene Therapy");
        item4.setDesc("A gene treatment therapy that gives the recipient the Ancient gene.");
        item4.setCost(30);
        item4.setPower(11);

        ListItem item5 = new ListItem();
        item5.setName("Puddle Jumper");
        item5.setDesc("A powerful spaceship that can fit through the gate and is equipped with a cloak and drone weapons");
        item5.setCost(150);
        item5.setPower(80);

        magicItems.add(item1);
        magicItems.add(item2);
        magicItems.add(item3);
        magicItems.add(item4);
        magicItems.add(item5);
    }

    private static void updateDisplay() {
        //System.out.println(locations[currentLocale].getName());

        System.out.print("[Current progress: " + moves + " moves, score: " + score + ", achievement ratio: " + score / moves + ", coins: " + coins + "] ");
        System.out.println();
        if(moves -1!= 0){
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
            buying = false;
            //gives the player 5 points the first time they visit a location
            if(locations[currentLocale].getHasVisited() == false){
                score+=5;
                locations[currentLocale].setHasVisited(true);
            }else
            if(currentLocale == 0 && allies == true) {
                System.out.println("You and your allies have gated in, the enemy troops have spotted you. You can either begin your attack or gate back.");
                System.out.println("Would you like to attack? Yes, or no?");
                getCommand();
                if(command.equalsIgnoreCase("yes") || command.equalsIgnoreCase("y")){
                    attack();
                }
                else{
                    currentLocale = 1;
                    updateDisplay();
                    System.out.println("You have gated back. When you are ready to attack, travel to the enemy base again.");
                }

            }
            else if(currentLocale == 0 && allies == false) {
                System.out.println("Without allies you don't stand a chance. Come back when you have gained allies");
            }
        }
        else{
            StringBuilder sb = new StringBuilder();
            sb.append("Cheyenne Mountain, Colorado. \n");
            sb.append("Major Young, due to your outstanding performance in the field, you have been promoted, and given the highest level of security clearance. \n");
            sb.append("The device in front of you is a Stargate. It was created by a race of beings we refer to as the Ancients. \n");
            sb.append("It creates a wormhole, enabling instantaneous transportation to another Stargate located many light years away from the starting point. \n");
            sb.append("We have used this network of Stargates to explore the Milky Way, and discover more about these Ancients. \n");
            sb.append("About 5 years ago, we discovered that the Ancients left our galaxy and moved to the Pegasus Galaxy. They built a large city called Atlantis. \n");
            sb.append("We sent an expedition and upon their arrival we discovered a powerful enemy in the Galaxy. We have finally located their homeworld.\n");
            sb.append("We need you to travel to Atlantis, gather powerful technology, and weapons, and form an alliance with a race of friendly aliens. \n");
            sb.append("You must then launch an attack on their planet and make the Galaxy safe. Now step through the Stargate and complete you mission Major.\n");
            sb.append("................. \n");
            sb.append("Hello Major, I am Colonel Carter. Welcome to Atlantis, you are free to explore the city. When you are ready travel through the Stargate to Complete your mission. \n");
            sb.append("... You look around. In front of you is the Stargate, behind you is the Cafe. To the left is Carter's Office, and to the right is the Armory \n");
            sb.append("Basic Commands You Can Type: North. South. East. West. Pickup. Inventory. Map. Buy. Quit. \n");
            System.out.println(sb);
        }

    }

    private static void getCommand(){
        Scanner inputReader = new Scanner(System.in);
        command = inputReader.nextLine();  // command is global.
    }

    private static void navigate() {
        final int INVALID = -1;
        int dir = INVALID;  // This will get set to a value > 0 if a direction command was entered.

        if (        command.equalsIgnoreCase("north") || command.equalsIgnoreCase("n") || command.equalsIgnoreCase("go north") || command.equalsIgnoreCase(" go n") ) {
            dir = 0;
        } else if ( command.equalsIgnoreCase("south") || command.equalsIgnoreCase("s") || command.equalsIgnoreCase("go south") || command.equalsIgnoreCase(" go s") ) {
            dir = 1;
        } else if ( command.equalsIgnoreCase("east")  || command.equalsIgnoreCase("e") || command.equalsIgnoreCase("go east") || command.equalsIgnoreCase(" go e") ) {
            dir = 2;
        } else if ( command.equalsIgnoreCase("west")  || command.equalsIgnoreCase("w") || command.equalsIgnoreCase("go west") || command.equalsIgnoreCase(" go w") ) {
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
        } else if ( currentLocale == 8 && buying == true) {
            shop();
        } else if ( currentLocale == 8 && guessing == true) {
            guessPassword();
        } else if (command.equalsIgnoreCase("kiss")) {
            kiss();
        }else if (command.equalsIgnoreCase("dance")) {
            dance();
        }else if (command.equalsIgnoreCase("wake") || command.equalsIgnoreCase("wake up") || command.equalsIgnoreCase("wakeup")) {
            wake();
        }else{
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
        if(currentLocale == 8){
            System.out.println("Laptop Login");
            System.out.println("Username: MeredithMcKay ");
            System.out.println("Password: ");
            System.out.println("Here's a hint: the birth years of the three smartest scientists and the answer to the question of life, the universe, and everything in it");
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
                System.out.println("Your reward is 300 coins.");
                coins += 300;

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
                    totalPower += currentItem.getPower();
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
        if(locations[currentLocale].getHasItem() == true && currentLocale == 2){
            boolean jelloCheck = false;
            System.out.println("The Athosian leaders and have discussed coming to war with you.");
            System.out.println("They will send soldiers to battle with you in exchange for blue jello.");
            System.out.println("Do you have blue jello with you?");
            for(int i=0; i<inventory.length; i++){
                if(inventory[i] == items[4]){
                    allies = true;
                    jelloCheck = true;
                    System.out.println("You do have blue jello!");
                    System.out.println("We will fight on your side.");
                    System.out.println("You have gained the allies necessary to fight the Wraith");
                    totalPower += 100;
                }
            }
            if(jelloCheck == false){
                System.out.println("You don't have blue jello with you. You must get some before we join you.");
            }
        }
        else if(locations[currentLocale].getHasItem() == true){
            inventory[inventoryCounter] = locations[currentLocale].getWhichItem();
            System.out.println("You have picked up a " + inventory[inventoryCounter].getName());
            System.out.println("You also found 25 coins.");
            coins+=25;
            totalPower += locations[currentLocale].getWhichItem().getPower();
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
        if(currentLocale == 8){
            System.out.println("Welcome to the Magick Shoppe protoype created by Dr. Rodney McKay");
            System.out.println("These are the items available for purchase: ");
            System.out.println(magicItems.Shop());
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

    private static void attack() {
        System.out.println("You have begun your assault on the Wraith Base ");
        if(totalPower > enemyCount){
            System.out.println("You have defeated the enemy troops and captured vital intelligence. Because of the victory Atlantis will remain safe.");
            quit();
        }
        else {
            System.out.println("The enemy forces were to strong, and you did not have good enough items.");
            System.out.println("In the aftermath of the battle the Wraith found you barely alive. They have taken \n +" +
                               "you to michael to be experimented on. Death would've been better.");
            quit();
        }
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
                System.out.println("|xxxxxxxxx|_________|xxxxxxxxx|");
                System.out.println("|xxxxxxxxx|         |xxxxxxxxx|");
                System.out.println("|xxxxxxxxx|         |xxxxxxxxx|");
                System.out.println("|Carter's |Gateroom |         |");
                System.out.println("| Office  |         | Armory  |");
                System.out.println("|xxxxxxxxx|_________|_________|");
                System.out.println("|Ronan's  |         | McKay's |");
                System.out.println("|Quarters |  Cafe   |   Lab   |");
                System.out.println("|_________|_________|_________|");
            }
            if(currentLocale == 1){
                System.out.println(" _____________________________");
                System.out.println("|         |    *    |         |");
                System.out.println("|EnemyBase|Offworld | Village |");
                System.out.println("|xxxxxxxxx|_________|xxxxxxxxx|");
                System.out.println("|xxxxxxxxx|         |xxxxxxxxx|");
                System.out.println("|xxxxxxxxx|         |xxxxxxxxx|");
                System.out.println("|Carter's |Gateroom |         |");
                System.out.println("| Office  |         | Armory  |");
                System.out.println("|xxxxxxxxx|_________|_________|");
                System.out.println("|Ronan's  |         | McKay's |");
                System.out.println("|Quarters |  Cafe   |   Lab   |");
                System.out.println("|_________|_________|_________|");
            }
            if(currentLocale == 2){
                System.out.println(" _____________________________");
                System.out.println("|         |         |    *    |");
                System.out.println("|EnemyBase|Offworld | Village |");
                System.out.println("|xxxxxxxxx|_________|xxxxxxxxx|");
                System.out.println("|xxxxxxxxx|         |xxxxxxxxx|");
                System.out.println("|xxxxxxxxx|         |xxxxxxxxx|");
                System.out.println("|Carter's |Gateroom |         |");
                System.out.println("| Office  |         | Armory  |");
                System.out.println("|xxxxxxxxx|_________|_________|");
                System.out.println("|Ronan's  |         | McKay's |");
                System.out.println("|Quarters |  Cafe   |   Lab   |");
                System.out.println("|_________|_________|_________|");
            }
            if(currentLocale == 3){
                System.out.println(" _____________________________");
                System.out.println("|         |         |         |");
                System.out.println("|EnemyBase|Offworld | Village |");
                System.out.println("|xxxxxxxxx|_________|xxxxxxxxx|");
                System.out.println("|xxxxxxxxx|         |xxxxxxxxx|");
                System.out.println("|xxxxxxxxx|         |xxxxxxxxx|");
                System.out.println("|Carter's |Gateroom |         |");
                System.out.println("| Office* |         | Armory  |");
                System.out.println("|xxxxxxxxx|_________|_________|");
                System.out.println("|Ronan's  |         | McKay's |");
                System.out.println("|Quarters |  Cafe   |   Lab   |");
                System.out.println("|_________|_________|_________|");
            }
            if(currentLocale == 4){
                System.out.println(" _____________________________");
                System.out.println("|         |         |         |");
                System.out.println("|EnemyBase|Offworld | Village |");
                System.out.println("|xxxxxxxxx|_________|xxxxxxxxx|");
                System.out.println("|xxxxxxxxx|         |xxxxxxxxx|");
                System.out.println("|xxxxxxxxx|         |xxxxxxxxx|");
                System.out.println("|Carter's |Gateroom |         |");
                System.out.println("| Office  |    *    | Armory  |");
                System.out.println("|xxxxxxxxx|_________|_________|");
                System.out.println("|Ronan's  |         | McKay's |");
                System.out.println("|Quarters |  Cafe   |   Lab   |");
                System.out.println("|_________|_________|_________|");
            }
            if(currentLocale == 5){
                System.out.println(" _____________________________");
                System.out.println("|         |         |         |");
                System.out.println("|EnemyBase|Offworld | Village |");
                System.out.println("|xxxxxxxxx|_________|xxxxxxxxx|");
                System.out.println("|xxxxxxxxx|         |xxxxxxxxx|");
                System.out.println("|xxxxxxxxx|         |xxxxxxxxx|");
                System.out.println("|Carter's |Gateroom |    *    |");
                System.out.println("| Office  |         | Armory  |");
                System.out.println("|xxxxxxxxx|_________|_________|");
                System.out.println("|Ronan's  |         | McKay's |");
                System.out.println("|Quarters |  Cafe   |   Lab   |");
                System.out.println("|_________|_________|_________|");
            }
            if(currentLocale == 6){
                System.out.println(" _____________________________");
                System.out.println("|         |         |         |");
                System.out.println("|EnemyBase|Offworld | Village |");
                System.out.println("|xxxxxxxxx|_________|xxxxxxxxx|");
                System.out.println("|xxxxxxxxx|         |xxxxxxxxx|");
                System.out.println("|xxxxxxxxx|         |xxxxxxxxx|");
                System.out.println("|Carter's |Gateroom |         |");
                System.out.println("| Office  |         | Armory  |");
                System.out.println("|xxxxxxxxx|_________|_________|");
                System.out.println("|Ronan's  |         | McKay's |");
                System.out.println("|Quarters |  Cafe   |   Lab   |");
                System.out.println("|____*____|_________|_________|");
            }
            if(currentLocale == 7){
                System.out.println(" _____________________________");
                System.out.println("|         |         |         |");
                System.out.println("|EnemyBase|Offworld | Village |");
                System.out.println("|xxxxxxxxx|_________|xxxxxxxxx|");
                System.out.println("|xxxxxxxxx|         |xxxxxxxxx|");
                System.out.println("|xxxxxxxxx|         |xxxxxxxxx|");
                System.out.println("|Carter's |Gateroom |         |");
                System.out.println("| Office  |         | Armory  |");
                System.out.println("|xxxxxxxxx|_________|_________|");
                System.out.println("|Ronan's  |         | McKay's |");
                System.out.println("|Quarters |  Cafe   |   Lab   |");
                System.out.println("|_________|____*____|_________|");
            }
            if(currentLocale == 8){
                System.out.println(" _____________________________");
                System.out.println("|         |         |         |");
                System.out.println("|EnemyBase|Offworld | Village |");
                System.out.println("|xxxxxxxxx|_________|xxxxxxxxx|");
                System.out.println("|xxxxxxxxx|         |xxxxxxxxx|");
                System.out.println("|xxxxxxxxx|         |xxxxxxxxx|");
                System.out.println("|Carter's |Gateroom |         |");
                System.out.println("| Office  |         | Armory  |");
                System.out.println("|xxxxxxxxx|_________|_________|");
                System.out.println("|Ronan's  |         | McKay's |");
                System.out.println("|Quarters |  Cafe   |   Lab   |");
                System.out.println("|_________|_________|____*____|");
            }
        }
        else {
            System.out.println("You do not have the map.");
        }
    }

    private static void quit() {
        stillPlaying = false;
    }


    //
    //Easter Eggs
    //

    private static void kiss() {
        if(currentLocale == 2){
            System.out.println("You see Teyla is the distance, you walk over grab her, before you can kiss her she flips you on your back and kicks you in the groin.");
        }
        else if(currentLocale == 3){
            System.out.println("You walk over to Carter's desk. Spin her around, and lean in to kiss her.... Hey dream boy, Wake Up!");
        }
        else if(currentLocale == 5){
            System.out.println("You see Shepard doing target practice. You tap him on the shoulder and lean in for a kiss. He points his 9mm in your face and tells you to leave.");
        }
        else if(currentLocale == 6){
            System.out.println("You walk over to Ronan and lean in for a kiss. You get punched in the face, repeatedly.");
        }
        else{
            System.out.println("There is no one here to kiss.");
        }
    }
    private static void dance() {
        System.out.println("You suddenly break out into dance, everyone looks at you strangely.");
    }
    private static void wake() {
        if(currentLocale == 6){
            System.out.println("For some reason you have decided to wake up Ronan, predictably you get your ass kicked.");
            if(coins>=5){
                System.out.println("While Ronan was kicking the crap out of you, you dropped 5 coins, he takes them . Ronan then went back to sleep");
                coins-=5;
            }
        }
        else{
            System.out.println("There is no one here sleeping to be woken up.");
        }
    }


}