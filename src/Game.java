import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    // Globals
    public static final boolean DEBUGGING = false;  // Debugging flag.
    public static final int MAX_LOCALES = 9;        // Total number of rooms/locations we have in the game.
    public static int currentLocale = 4;            // Player starts in locale 4.
    public static String command;                   // What the player types as he or she plays the game.
    public static boolean stillPlaying = true;      // Controls the game loop.
    public static boolean moved = false;            // Determines whether or not to print game information
    public static boolean buying = false;           // Determines if the player is trying to buy an item
    public static boolean guessing = false;         // Determines if the player is trying to guess the password
    public static boolean allies = false;           // Determines if the player has successfully gained allies
    public static boolean inJumper = false;         // Determines if the player is in the Jumper
    public static double moves = 1;                 // Counter of the player's moves.
    public static double score = 5;                 // Tracker of the player's score.
    public static int enemyCount = 210;             // Change this to increase/decrease difficulty
    public static int totalPower = 0;               // Keeps track of the total power the player has / damage they can do
    public static Item[] items;                     // An uninitialized array of type Item. See init() for initialization.
    public static Item[] inventory;                 // An array of Items that stores the players items they pickup
    public static ArrayList<ListItem> magicItemsInventory = new ArrayList<ListItem>(); //An arrayList to store Magic Items bought by the player
    public static int inventoryCounter = 0;         // keeps track of # of items in inventory
    public static int coins = 100;                  // keeps track of coins used to buy items in magick shoppe
    public static int cheaterCounter = 0;           //used in case the player decides to mine for coins for too long
    public static int guessCounter = 5;             //ued to keep track of guesses for password
    public static MagicItemsList magicItems  = new MagicItemsList();
    public static LocaleList localeList  = new LocaleList();




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
            if(moved){ //Can simplify to just if(moved), but i believe this makes it easier to read quickly
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
        item1.setName("Mckay prototype");
        item1.setDesc("There is one of Mckay's prototypes available for pickup.");
        item1.setPower(10);

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

        Item item5 = new Item(5);
        item5.setName("gate addresses");
        item5.setDesc("There is a list of gate addresses available for pickup.");

        //array of all items in game
        items = new Item[6];
        items[0] = item0; //map
        items[1] = item1; //prototype
        items[2] = item2; //gun
        items[3] = item3; //allies
        items[4] = item4; //blue jello
        items[5] = item5; //gate addresses
        inventory = new Item[5]; //creates the array to store items when the player picks them up

        createLocales();
        createMagicItems(); //Creates the items that player can buy from McKay's Machine
    }


    private static void createLocales() {
        // Create the list manager for our magic items.
        localeList.setName("List of Locales");
        localeList.setDesc("These are the locales the player can navigate to.");


        // Create some Locales and put them in the list.
        OffWorld loc0 = new OffWorld(0);
        loc0.setName("Enemy Base");
        loc0.setDesc("You have entered the enemy base");
        loc0.setInhabitants("Wraith");
        loc0.setNumberOfInhabitants(enemyCount);
        loc0.setLookDesc("You have entered the Wraith's base. There are " +loc0.getNumberOfInhabitants() + " Wraith soldiers in front of you.\n" +
                "In the distance you see a Hive Ship with the intel you need to retrieve.\n" +
                "If you think you are strong enough you can begin your attack");

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
        loc2.setNumberOfInhabitants(100);

        Locale loc3 = new Locale(3);
        loc3.setName("Carter Office");
        loc3.setDesc("You are in Colonel Carter\'s Office.");
        loc3.setLookDesc("You walk into carters office. She's laying on her table in a skimpy bikini, waiting for you. \n" +
                "Whoa. Calm down there dream boy. Enough with the fantasy. \n"+
                "Carter is sitting at her desk, she is reviewing mission files.");
        loc3.setHasItem(true);
        loc3.setWhichItem(items[5]);

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

        Locale loc7 = new Locale(7);
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
                "To guess, type Guess Password, enter your guess after the prompt.");
        loc8.setHasItem(true);
        loc8.setWhichItem(items[1]);

        Locale loc9 = new Locale(9);
        loc9.setName("Jumper Bay");
        loc9.setDesc("You are in the Jumper Bay");
        loc9.setLookDesc("You have entered the Jumper Bay. You look around and see Puddle Jumpers all around the room. \n" +
                         "A puddle Jumper is a small spacecraft created by the Ancients, it is small enough to fit through the stargate \n"+
                         "You can use it to travel through the stargate or explore the planet.");

        Locale loc10 = new Locale(10);
        loc10.setName("Midway Space Station");
        loc10.setDesc("You are at the Midway Space Station");
        loc10.setLookDesc("You have arrived at the Midway Space Station. Halfway point between Pegasus and the Milky Way Galaxies. \n" +
                "Construction has not yet been finished so you must remain in your jumper. \n"+
                "From here you can gate to Stargate Command on Earth, or gate back to Atlantis.");

        Locale loc11 = new Locale(9);
        loc11.setName("Stargate Command");
        loc11.setDesc("You are in Stargate Command");
        loc11.setLookDesc("You have entered arrived at Stargate Command on Earth. \n" +
                "General Hank Landry, and General O'Neill are standing there to great you.\n"+
                "In case you have been compromised you will not be allowed to explore Stargate Command at this time.");

        //set up the links for navigation
        loc0.setGoNorth(null);
        loc0.setGoEast(loc1);
        loc0.setGoSouth(null);
        loc0.setGoWest(null);

        loc1.setGoToAthos(loc2);
        loc1.setGoToWratih(loc0);
        loc1.setGoToAtlantis(loc4);

        loc2.setGoToBarren(loc1);
        loc2.setGoToWratih(loc0);
        loc2.setGoToAtlantis(loc4);

        loc3.setGoNorth(null);
        loc3.setGoEast(loc4);
        loc3.setGoSouth(null);
        loc3.setGoWest(null);

        loc4.setGoNorth(loc1);
        loc4.setGoEast(loc5);
        loc4.setGoSouth(loc7);
        loc4.setGoWest(loc3);
        loc4.setGoToAthos(loc2);
        loc4.setGoToBarren(loc1);
        loc4.setGoToWratih(loc0);
        loc4.setGoToMidway(loc10);

        loc5.setGoNorth(null);
        loc5.setGoEast(loc9);
        loc5.setGoSouth(loc8);
        loc5.setGoWest(loc4);

        loc6.setGoNorth(null);
        loc6.setGoEast(loc7);
        loc6.setGoSouth(null);
        loc6.setGoWest(null);

        loc7.setGoNorth(loc4);
        loc7.setGoEast(loc8);
        loc7.setGoSouth(null);
        loc7.setGoWest(loc6);

        loc8.setGoNorth(loc5);
        loc8.setGoEast(null);
        loc8.setGoSouth(null);
        loc8.setGoWest(loc7);

        loc9.setGoNorth(null);
        loc9.setGoEast(null);
        loc9.setGoSouth(null);
        loc9.setGoWest(loc5);

        loc10.setGoToEarth(loc11);
        loc10.setGoToAtlantisJumper(loc9);


        localeList.add(loc0);
        localeList.add(loc1);
        localeList.add(loc2);
        localeList.add(loc3);
        localeList.add(loc4);
        localeList.add(loc5);
        localeList.add(loc6);
        localeList.add(loc7);
        localeList.add(loc8);
        localeList.add(loc9);
        localeList.add(loc10);
        localeList.add(loc11);
        localeList.setCurrent(loc4);


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
        item2.setPower(120);

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

        ListItem item6 = new ListItem();
        item6.setName("Lantean Pistol");
        item6.setDesc("A pistol made by the Ancients");
        item6.setCost(5);
        item6.setPower(1);

        ListItem item7 = new ListItem();
        item7.setName("Mini Drones");
        item7.setDesc("Tiny automated drones that rip through enemies");
        item7.setCost(50);
        item7.setPower(25);



        final String fileName = "magicitems.txt";
        readMagicItemsFromFile(fileName, magicItems);
        magicItems.add(item1);
        magicItems.add(item2);
        magicItems.add(item3);
        magicItems.add(item4);
        magicItems.add(item5);
        magicItems.add(item6);
        magicItems.add(item7);

    }

    private static void updateDisplay() {
        //To limit number of decimal places in the achievement ratio
        double achievementRatio;
        achievementRatio = score/moves;
        achievementRatio = (double)Math.round(achievementRatio * 100) / 100; //Stack Overflow
        System.out.print("[Current progress: " + moves + " moves, score: " + score + ", achievement ratio: " + achievementRatio + ", coins: " + coins + ", power: " + totalPower + "] ");
        System.out.println();
        if(moves -1!= 0){
            if(localeList.getCurrent().getHasItem() && !localeList.getCurrent().getHasVisited()){
                System.out.println(localeList.getCurrent().getLookDesc() + ". " + localeList.getCurrent().getWhichItem().getDesc());
            }
            else if(!localeList.getCurrent().getHasVisited()){
                System.out.println(localeList.getCurrent().getLookDesc());
            }
            else if(localeList.getCurrent().getHasItem())
            {
                System.out.println(localeList.getCurrent().getDesc() + ". " + localeList.getCurrent().getWhichItem().getDesc());
            }
            else{
                System.out.println(localeList.getCurrent().getDesc());
            }

            System.out.print("From here the directions you can move are: ");


            if(localeList.getCurrent().getGoNorth() != null){
                System.out.print("North ");
            }
            if(localeList.getCurrent().getGoSouth() != null){
                System.out.print("South ");
            }
            if(localeList.getCurrent().getGoEast() != null){
                System.out.print("East ");
            }
            if(localeList.getCurrent().getGoWest() != null){
                System.out.print("West ");
            }
            else{
                System.out.print("Back through the stargate ");
            }

            System.out.println();
            buying = false;
            //gives the player 5 points the first time they visit a location
            if(!localeList.getCurrent().getHasVisited()){
                score+=5;
                localeList.getCurrent().setHasVisited(true);
            }

            if(currentLocale == 9){
                puddleJumper();
            }

            if(currentLocale == 10){
                midway();
            }



            //code for the end game
            if(currentLocale == 0 && allies) { //Player can only attack if they have gained allies
                System.out.println("You and your allies have gated in, the enemy troops have spotted you. You can either begin your attack or gate back.");
                System.out.println("Would you like to attack? Yes, or no?"); //Gives player option to attack
                getCommand();
                if(command.equalsIgnoreCase("yes") || command.equalsIgnoreCase("y")){
                    attack();
                }
                else{ //If player chooses not to attack, send them back to location 1
                    currentLocale = 1;
                    updateDisplay();
                    System.out.println("You have gated back. When you are ready to attack, travel to the enemy base again.");
                }

            }
            else if(currentLocale == 0 && !allies) {
                System.out.println("Without allies you don't stand a chance. Come back when you have gained allies");
            }
        }
        //displays the opening story for the game
        else{
            StringBuilder sb = new StringBuilder(); //Used StringBuilder to save memory
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

    //gets inputs from the player
    private static void getCommand(){
        Scanner inputReader = new Scanner(System.in);
        command = inputReader.nextLine();  // command is global.
    }

    //allows the player to move around the map
    private static void navigate() {

        int dir = -1;  // This will get set to a value > 0 if a direction command was entered.

        if (        command.equalsIgnoreCase("north") || command.equalsIgnoreCase("n") || command.equalsIgnoreCase("go north") || command.equalsIgnoreCase(" go n") ) { //go north
            dir = 0;
        } else if ( command.equalsIgnoreCase("south") || command.equalsIgnoreCase("s") || command.equalsIgnoreCase("go south") || command.equalsIgnoreCase(" go s") ) { //go south
            dir = 1;
        } else if ( command.equalsIgnoreCase("east")  || command.equalsIgnoreCase("e") || command.equalsIgnoreCase("go east") || command.equalsIgnoreCase(" go e") ) { //go east
            dir = 2;
        } else if ( command.equalsIgnoreCase("west")  || command.equalsIgnoreCase("w") || command.equalsIgnoreCase("go west") || command.equalsIgnoreCase(" go w") ) { //go west
            dir = 3;

        } else if ( command.equalsIgnoreCase("pick")  || command.equalsIgnoreCase("pickup") || command.equalsIgnoreCase("p")) { //pickup item
            pickup();
        } else if ( command.equalsIgnoreCase("inventory")  || command.equalsIgnoreCase("i") ) { //show items in inventory
            showInventory();
        } else if ( command.equalsIgnoreCase("map")  || command.equalsIgnoreCase("m") ) { //shows the map
            showMap();
        } else if ( command.equalsIgnoreCase("buy")  || command.equalsIgnoreCase("b") ) { //buy items
            buyItems();
        } else if ( command.equalsIgnoreCase("look")  || command.equalsIgnoreCase("l") ) { //look around
            look();
        } else if (command.equalsIgnoreCase("guess password")  || command.equalsIgnoreCase("guess"))  { //guess password
            password();
        } else if ( command.equalsIgnoreCase("quit")  || command.equalsIgnoreCase("q")) { //quit game
            quit();
        } else if ( command.equalsIgnoreCase("help")  || command.equalsIgnoreCase("h")) { //display available commands
            help();
        } else if ( currentLocale == 8 && buying) { //buy prompt
            shop();
        } else if ( currentLocale == 8 && guessing) { //guess prompt
            guessPassword();
        //easter egg commands
        } else if (command.equalsIgnoreCase("kiss")) { //kiss someone
            kiss();
        }else if (command.equalsIgnoreCase("dance")) { //dance
            dance();
        }else if (command.equalsIgnoreCase("wake") || command.equalsIgnoreCase("wake up") || command.equalsIgnoreCase("wakeup")) { //wake up Ronan
            wake();
        }else if (command.equalsIgnoreCase("talk") || command.equalsIgnoreCase("t")) { //talk to people
            talk();
        }else{
            System.out.println("That is an invalid command. Type help to see a list of commands.");
        }

        Locale newLocation = null;
        Boolean hasAdresses = false;
        for(int i=0; i<inventory.length; i++){
            if(inventory[i] == items[5]){
                hasAdresses = true;
            }
        }
        if (dir > -1) {   // This means a dir was set.
           if(currentLocale == 1 || currentLocale == 2 || (currentLocale == 4 && dir == 0) || currentLocale == 11){
               if(hasAdresses){
                   System.out.println("What address would you like to dial?");
                   if(currentLocale != 11){
                       if(currentLocale != 4){
                       System.out.println("Atlantis");
                       }
                       System.out.println("Athos");
                       System.out.println("Barren World");
                       System.out.println("Wraith Base");
                   }
                   if(currentLocale == 11){
                       System.out.println("Midway Space Station");
                   }
                   getCommand();
                   System.out.println("The gate has been dialed, you step through the gate and you emerge at your destination");
                   if(command.equalsIgnoreCase("Barren World")){
                       currentLocale = 1;
                       newLocation = localeList.getCurrent().getGoToBarren();
                   }
                   else if(command.equalsIgnoreCase("Athos")){
                       currentLocale = 2;
                       newLocation = localeList.getCurrent().getGoToAthos();
                   }
                   else if(command.equalsIgnoreCase("Wriath Base")){
                       currentLocale = 0;
                       newLocation = localeList.getCurrent().getGoToWratih();
                   }
                   else if(command.equalsIgnoreCase("Atlantis")){
                       currentLocale = 4;
                       newLocation = localeList.getCurrent().getGoToAtlantis();
                   }
                   else if(command.equalsIgnoreCase("Midway Space Station")){
                       currentLocale = 10;
                       newLocation = localeList.getCurrent().getGoToMidway();
                   }

               }
               else{
                   System.out.println("You cannot dial the gate without any gate addresses, find a list of gate addresses.");
               }
           }
           else if(dir == 0){
               newLocation = localeList.getCurrent().getGoNorth();
           }
           else if(dir == 1){
               newLocation = localeList.getCurrent().getGoSouth();
           }
           else if(dir == 2){
               newLocation = localeList.getCurrent().getGoEast();
           }
           else{
               newLocation = localeList.getCurrent().getGoWest();
           }

            //if the player can't go that way, tell them
            if (newLocation == null) {
                System.out.println("You cannot go that way.");
            } else  {
                localeList.setCurrent(newLocation);
                currentLocale = localeList.getCurrent().getId();
                moves = moves + 1;
                moved=true;

            }
        }
    }

    //Prompts the player to enter a guess at the password
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

    //Allows player to attempt to guess a password to gain additional coins
    private static void guessPassword() {
        if(guessCounter>0){
            if(command.equalsIgnoreCase("16431879196842")){
                System.out.println("You have cracked the password, Damn McKay is arrogant.");
                System.out.println("Your reward is 300 coins.");
                coins += 300; //gives 300 coins for correct guess
                score += 10; //also give points as reward

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

    //if the player types buy in the right location, display this prompt
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

    //checks to see if the item the player typed is available to be bought, checks if player has enough coins, adds to inventory, subtracts coins
    private static void shop() {

        ListItem currentItem = magicItems.getHead();
        Boolean boughtSomething = false;
        while (currentItem != null) {


            if(command.equalsIgnoreCase(currentItem.getName())){ //looks for item
                if(coins >= currentItem.getCost()){ //checks for enough coins
                    System.out.println("You have purchased " + currentItem.getName());
                    System.out.println("Thank you for testing out Dr. McKays's Prototype Magick Shoppe. Please come again. ");
                    coins-=currentItem.getCost(); //subtracts coins
                    boughtSomething = true;
                    magicItemsInventory.add(currentItem); //adds item to inventory
                    totalPower += currentItem.getPower(); //adds to players power
                    score += 2;                           //gives player some points for buying items
                }
                else{
                    System.out.println("McKay set the price too high, you don't have enough coins to afford this item."); //if player doesn't have enough coins
                    boughtSomething = true;
                }

            }

            currentItem = currentItem.getNext();
        }
        buying = false;
        if(!boughtSomething){
            System.out.println("That is not an item McKay has stocked the machine with. "); // if player typed unavailable item
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
        if(localeList.getCurrent().getHasItem() && currentLocale == 2){ //a check for gaining allies
            boolean jelloCheck = false;
            System.out.println("The Athosian leaders have discussed coming to war with you.");
            System.out.println("They will send soldiers to battle with you in exchange for blue jello.");
            System.out.println("Do you have blue jello with you?");
            //checks for jello in inventory
            for(int i=0; i<inventory.length; i++){
                if(inventory[i] == items[4]){
                    allies = true; //player now has allies
                    jelloCheck = true;
                    System.out.println("You do have blue jello!");
                    System.out.println("We will fight on your side.");
                    System.out.println("You have gained the allies necessary to fight the Wraith");
                    totalPower += 100; //adds to power total
                }
            }
            if(!jelloCheck){
                System.out.println("You don't have blue jello with you. You must get some before we join you.");
            }
        }
        //in all other locations, checks if there is an item available for pickup
        else if(localeList.getCurrent().getHasItem()){
            inventory[inventoryCounter] = localeList.getCurrent().getWhichItem(); //if there is an item, pick it up
            System.out.println("You have picked up a " + inventory[inventoryCounter].getName()); //tells player
            System.out.println("You also found 25 coins.");
            coins+=25; //give them coins as a bonus
            totalPower += localeList.getCurrent().getWhichItem().getPower(); //adds items power to players total
            localeList.getCurrent().setHasItem(false);
            inventoryCounter++;
        }
        else{
            //if there is no item to pickup, it tells trhe player and gives them 5 coins anyway
            System.out.println("There is no item here to pickup.");
            System.out.println("But you found 5 coins.");
            coins+=5;
            cheaterCounter++; //prevents the player from abusing my generosity
            if(cheaterCounter >= 25){ //if they abuse my generosity, punish them
                System.out.println("You think you're smart, think you found a way to cheat the system and get a shit load of coins? ");
                System.out.println("Well guess what");
                System.out.println("An ascended ancient has appeared and chastises you for being greedy. ");
                System.out.println("As punishment you lose all your coins. ");
                System.out.println("And Ronan punches you in the face. ");
                coins = 0; //take all their coins
                cheaterCounter = 0;
            }
        }
    }

    //allows the player to view the items they have picked up
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

    //If the player types look, provide them with the longer description of the current location
    private static void look() {
        System.out.println(localeList.getCurrent().getLookDesc());
    }

    //if the player is in the enemy base give them the option to attack
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

    //Displays the map to help the player navigate


    private static void quit() {
        stillPlaying = false;
    }


    //
    //Easter Eggs
    //

    //allows the player to attempt to kiss various characters
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

    //allows the player to randomly dance
    private static void dance() {
        System.out.println("You suddenly break out into dance, everyone looks at you strangely.");
    }

    //allows the player to wake up Ronan Dex
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

    private static void talk() {
        if(currentLocale == 0){
            System.out.println("The Wraith aren't much for talking. They prefer sucking the life out of you.");
        }
        if(currentLocale == 2){
            System.out.println("The Athosian leaders comes over and tells you that he loves blue jello.");
        }
        else if(currentLocale == 3){
            System.out.println("Were you just picturing me in a bikini Major Young?");
        }
        else if(currentLocale == 5){
            System.out.println("Shepard asks you to go golfing with him, Lorne asks you to paint with him. You walk away, quickly.");
        }
        else if(currentLocale == 6){
            System.out.println("You're smart enough not to talk to Ronan while he's sleeping.");
        }
        else if(currentLocale == 8){
            System.out.println("Zelenka is muttering to himself in Czech. No point trying to talk to him.");
        }
        else{
            System.out.println("There is no one here to talk to your sorry ass.");
        }
    }

    private static void puddleJumper(){
        Boolean hasAdresses = false;
        for(int i=0; i<inventory.length; i++){
            if(inventory[i] == items[5]){
                hasAdresses = true;
            }
        }
        if(hasAdresses){
            System.out.println("Would you like to enter a jumper?");
            getCommand();
            if(command.equalsIgnoreCase("yes") || command.equalsIgnoreCase("y")){
                inJumper = true;
                System.out.println("What would you like to do?");
                System.out.println("Go to the gateroom and travel through the stargate? Or Open the roof and go explore the planet?");
                System.out.println("Type Gateroom or Explore");
                getCommand();
                if(command.equalsIgnoreCase("gateroom")){
                    localeList.setCurrent(localeList.getCurrent().getGoWest().getGoWest());
                    currentLocale = 4;
                    System.out.println("The puddle jumper has been lowered into the gateroom. What address would you like to dial?");
                    System.out.println("Midway Space Station");
                    getCommand();
                    System.out.println("The gate has been dialed, the puddle jumper enters the gate and you emerged at your destination");
                    if(command.equalsIgnoreCase("Midway Space Station")){
                        currentLocale = 10;
                        localeList.setCurrent(localeList.getCurrent().getGoToMidway());
                    }
                    updateDisplay();

                }
                else if(command.equalsIgnoreCase("explore")){
                    System.out.println("The roof of the Jumper Bay retracts and you fly the jumper out. ");
                    int currentRow = 3;
                    int currentColumn = 7;
                    boolean done = false;
                    String[] jumperMapRow0 = new String[15];
                    String[] jumperMapRow1 = new String[15];
                    String[] jumperMapRow2 = new String[15];
                    String[] jumperMapRow3 = new String[15];
                    String[] jumperMapRow4 = new String[15];
                    String[] jumperMapRow5 = new String[15];
                    String[] jumperMapRow6 = new String[15];

                    for(int i =0; i< 14; i++){
                        jumperMapRow0[i] = ".";
                    }
                    for(int i =0; i< 14; i++){
                        jumperMapRow1[i] = ".";
                    }
                    for(int i =0; i< 14; i++){
                        jumperMapRow2[i] = ".";
                    }
                    for(int i =0; i< 14; i++){
                        jumperMapRow3[i] = ".";
                        jumperMapRow3[7] = "A";
                    }
                    for(int i =0; i< 14; i++){
                        jumperMapRow4[i] = ".";
                    }
                    for(int i =0; i< 14; i++){
                        jumperMapRow5[i] = ".";
                    }
                    for(int i =0; i< 14; i++){
                        jumperMapRow6[i] = ".";
                    }

                    String[][] allRows = new String[7][15];
                    allRows[0] = jumperMapRow0;
                    allRows[1] = jumperMapRow1;
                    allRows[2] = jumperMapRow2;
                    allRows[3] = jumperMapRow3;
                    allRows[4] = jumperMapRow4;
                    allRows[5] = jumperMapRow5;
                    allRows[6] = jumperMapRow6;

                    for(int i= 0; i<14; i++){
                        System.out.print(jumperMapRow0[i]);
                    }
                    System.out.println(jumperMapRow0[5]);
                    for(int i= 0; i<14; i++){
                        System.out.print(jumperMapRow1[i]);
                    }
                    System.out.println(jumperMapRow1[5]);
                    for(int i= 0; i<14; i++){
                        System.out.print(jumperMapRow2[i]);
                    }
                    System.out.println(jumperMapRow2[5]);
                    for(int i= 0; i<14; i++){
                        System.out.print(jumperMapRow3[i]);
                    }
                    System.out.println(jumperMapRow3[5]);
                    for(int i= 0; i<14; i++){
                        System.out.print(jumperMapRow4[i]);
                    }
                    System.out.println(jumperMapRow4[5]);
                    for(int i= 0; i<14; i++){
                        System.out.print(jumperMapRow5[i]);
                    }
                    System.out.println(jumperMapRow5[5]);
                    for(int i= 0; i<14; i++){
                        System.out.print(jumperMapRow6[i]);
                    }
                    System.out.println(jumperMapRow6[5]);

                    while(!done){
                        getCommand();
                        if(command.equalsIgnoreCase("n")){
                            if(currentRow!=0){
                                if(allRows[currentRow][currentColumn] != "M" && allRows[currentRow][currentColumn] != "A" && allRows[currentRow][currentColumn] != "W" && allRows[currentRow][currentColumn] != "O"){
                                    allRows[currentRow][currentColumn] = "x";
                                }
                                allRows[currentRow-1][currentColumn] = "J";
                                currentRow = currentRow-1;
                            }
                            else{
                                System.out.println("You cannot go that way, you are too far from Atlantis");
                            }

                        }
                        else if(command.equalsIgnoreCase("s")){
                            if(currentRow!=6){
                                if(allRows[currentRow][currentColumn] != "M" && allRows[currentRow][currentColumn] != "A" && allRows[currentRow][currentColumn] != "W" && allRows[currentRow][currentColumn] != "O"){
                                    allRows[currentRow][currentColumn] = "x";
                                }
                                allRows[currentRow+1][currentColumn] = "J";
                                currentRow = currentRow+1;
                            }
                            else{
                                System.out.println("You cannot go that way, you are too far from Atlantis");
                            }
                        }
                        else if(command.equalsIgnoreCase("e")){
                            if(currentColumn!=13)
                            {
                                if(allRows[currentRow][currentColumn] != "M" && allRows[currentRow][currentColumn] != "A" && allRows[currentRow][currentColumn] != "W" && allRows[currentRow][currentColumn] != "O"){
                                    allRows[currentRow][currentColumn] = "x";
                                }
                                allRows[currentRow][currentColumn+1] = "J";
                                currentColumn = currentColumn+1;
                            }
                            else{
                                System.out.println("You cannot go that way, you are too far from Atlantis");
                            }

                        }
                        else if(command.equalsIgnoreCase("w")){
                            if(currentColumn!=0)
                            {
                                if(allRows[currentRow][currentColumn] != "M" && allRows[currentRow][currentColumn] != "A" && allRows[currentRow][currentColumn] != "W" && allRows[currentRow][currentColumn] != "O"){
                                    allRows[currentRow][currentColumn] = "x";
                                }
                                allRows[currentRow][currentColumn-1] = "J";
                                currentColumn = currentColumn-1;
                            }
                            else{
                                System.out.println("You cannot go that way, you are too far from Atlantis");
                            }
                        }

                        if(allRows[currentRow][currentColumn] == allRows[0][12]){
                            allRows[currentRow][currentColumn] = "M";
                            System.out.println("You have found the mainland, you land the jumper and explore.");
                            System.out.println("You discover a crashed alien satellite and search it and find an alien power core.");
                            System.out.println("This will aid you in your fight with the Wraith.");
                            totalPower += 10;
                        }

                        if(allRows[currentRow][currentColumn] == allRows[5][3]){
                            allRows[currentRow][currentColumn] = "W";
                            System.out.println("There is a very large whale in the ocean beneath you.");
                        }

                        if(allRows[currentRow][currentColumn] == allRows[5][9]){
                            allRows[currentRow][currentColumn] = "O";
                            System.out.println("There is an underwater drilling platform beneath the ocean. Perhaps this can be used on day.");
                        }

                        if(allRows[currentRow][currentColumn] == allRows[3][7]){
                            System.out.println("You have successfully returned to Atlantis");
                            updateDisplay();
                        }

                        for(int i= 0; i<14; i++){
                            System.out.print(jumperMapRow0[i]);
                        }
                        System.out.println();
                        for(int i= 0; i<14; i++){
                            System.out.print(jumperMapRow1[i]);
                        }
                        System.out.println();
                        for(int i= 0; i<14; i++){
                            System.out.print(jumperMapRow2[i]);
                        }
                        System.out.println();
                        for(int i= 0; i<14; i++){
                            System.out.print(jumperMapRow3[i]);
                        }
                        System.out.println();
                        for(int i= 0; i<14; i++){
                            System.out.print(jumperMapRow4[i]);
                        }
                        System.out.println();
                        for(int i= 0; i<14; i++){
                            System.out.print(jumperMapRow5[i]);
                        }
                        System.out.println();
                        for(int i= 0; i<14; i++){
                            System.out.print(jumperMapRow6[i]);
                        }
                        System.out.println();
                    }



                }
            }
            else if(command.equalsIgnoreCase("no") || command.equalsIgnoreCase("n")){
                System.out.println("Well, no point hanging around in here, back to the Armory.");
                localeList.setCurrent(localeList.getCurrent().getGoWest());
                currentLocale = 5;
                updateDisplay();
            }
        }
        else{
            System.out.println("There is no point in entering the jumper without a list of gate addresses.");
        }
    }

    private static void midway(){
        System.out.println("Would you like to gate to Earth or Atlantis?");
        System.out.println("Type Earth or Atlantis");
        getCommand();
        if(command.equalsIgnoreCase("earth")){
            localeList.setCurrent(localeList.getCurrent().getGoToEarth());
            currentLocale = 11;
            System.out.println("You dial the gate and fly the puddle jumper into the wormhole.");
        }
        else if(command.equalsIgnoreCase("atlantis")){
            localeList.setCurrent(localeList.getCurrent().getGoToAtlantisJumper());
            currentLocale = 9;
            System.out.println("You dial the gate and fly the puddle jumper into the wormhole.");
            System.out.println("You arrive in the gateroom, you maneuver the jumper back to the jumper bay.");
            inJumper = false;
        }
        updateDisplay();
    }

    private static void readMagicItemsFromFile(String fileName,
                                               MagicItemsList lm) {
        File myFile = new File(fileName);
        try {
            Scanner input = new Scanner(myFile);
            while (input.hasNext()) {
                // Read a line from the file.
                String itemName = input.nextLine();

                // Construct a new list item and set its attributes.
                ListItem fileItem = new ListItem();
                fileItem.setName(itemName);
                fileItem.setCost(Math.round(Math.random() * 100));
                fileItem.setPower((int)(fileItem.getCost())/2);
                fileItem.setNext(null); // Still redundant. Still safe.

                // Add the newly constructed item to the list.
                lm.add(fileItem);
            }
            // Close the file.
            input.close();
        } catch (FileNotFoundException ex) {
            System.out.println("File not found. " + ex.toString());
        }
    }

    private static void showMap() {
        boolean hasMap = false;
        for(int i=0; i<inventoryCounter; i++){
            if(inventory[i].getName() == "map"){
                hasMap = true;
            }
        }
        //Only shows the map if the player has picked it up
        //Displays an indicator that will show what location the player is in
        if(hasMap){
            if(currentLocale == 11){
                System.out.println("You do not have clearance to have a map of the SGC");
            }
            else{
                System.out.println(" _____________________________");

                if(currentLocale == 0){
                    System.out.println("|    *    |         |         |");
                }
                if(currentLocale == 1){
                    System.out.println("|         |    *    |         |");
                }
                if(currentLocale == 2){
                    System.out.println("|         |         |    *    |");
                }
                if(currentLocale == 3 || currentLocale == 4 || currentLocale == 5 ||currentLocale == 6 || currentLocale == 7 || currentLocale == 8 || currentLocale == 9){
                    System.out.println("|         |         |         |");
                }
                    System.out.println("|EnemyBase|Offworld | Village |");
                    System.out.println("|xxxxxxxxx|_________|xxxxxxxxx|");
                    System.out.println("|xxxxxxxxx|         |xxxxxxxxx|");
                    System.out.println("|xxxxxxxxx|         |xxxxxxxxx|__________");
                    System.out.println("|Carter's |Gateroom | Armory  |Jumper Bay|");

                if(currentLocale == 3){
                    System.out.println("| Office* |         |         |          |");
                }
                if(currentLocale == 4){
                    System.out.println("| Office  |    *    |         |          |");
                }
                if(currentLocale == 5){
                    System.out.println("| Office  |         |    *    |          |");
                }
                if(currentLocale == 9){
                    System.out.println("| Office  |         |         |    *     |");
                }
                if(currentLocale == 0 || currentLocale == 1 || currentLocale == 2 ||currentLocale == 6 || currentLocale == 7 || currentLocale == 8){
                    System.out.println("| Office  |         |         |");
                }
                    System.out.println("|xxxxxxxxx|_________|_________|__________|");
                    System.out.println("|Ronan's  |         | McKay's |");
                    System.out.println("|Quarters |  Cafe   |   Lab   |");

                if(currentLocale == 6){
                    System.out.println("|____*____|_________|_________|");
                }
                if(currentLocale == 7){
                    System.out.println("|_________|____*____|_________|");
                }
                if(currentLocale == 8){
                    System.out.println("|_________|_________|____*____|");
                }
                if(currentLocale == 0 || currentLocale == 1 || currentLocale == 2 ||currentLocale == 3 || currentLocale == 4 || currentLocale == 5 || currentLocale == 9){
                    System.out.println("|_________|_________|_________|");
                }
        }


            /*  What map looks like
                System.out.println(" _____________________________");
                System.out.println("|         |         |         |");
                System.out.println("|EnemyBase|Offworld | Village |");
                System.out.println("|xxxxxxxxx|_________|xxxxxxxxx|");
                System.out.println("|xxxxxxxxx|         |xxxxxxxxx|");
                System.out.println("|xxxxxxxxx|         |xxxxxxxxx|__________");
                System.out.println("|Carter's |Gateroom |         |Jumper Bay|");
                System.out.println("| Office  |         | Armory  |          |");
                System.out.println("|xxxxxxxxx|_________|_________|__________|");
                System.out.println("|Ronan's  |         | McKay's |");
                System.out.println("|Quarters |  Cafe   |   Lab   |");
                System.out.println("|_________|_________|_________|");
            */
        }
        else {
            System.out.println("You do not have the map.");
        }
    }


}