import java.util.Stack;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.ArrayList;
/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game implements Cloneable
{   private Item restore, heal, sword, teleporter;
    private Parser parser;
    private Room currentRoom;
    private Deque<Room> stack; 
    private Stack<Room> stackLegacy;
    private Hero hero, enemy;
    private Room dragonLayer;
    private boolean gameWon;
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {   gameWon = false;
        stack = new ArrayDeque<Room>();
        stackLegacy = new Stack<>();
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room bedRoom, sittingRoom, drawingRoom, firstFloorStaircase, secondFloorStaircase, library, receptionHall, dressingRoom, diningRoom, entrance;
        Inventory heroStuff, enemyStuff;
        //create items and enemies
        teleporter = new Item("teleporter", "The item can teleport you to any room you wish", 0); 
        restore = new Item("ppRestore","restores power points", 2);
        heal = new Item("healPotion", "restores  HP", 2);
        sword = new Item("sword", "Better sword than your old one", 10);
        sword.setStrenght(500);
        heroStuff = new Inventory();
        enemyStuff = new Inventory();
        heroStuff.addItem(restore);
        heroStuff.addItem(sword);
        hero = new Hero(10000, 150, heroStuff );
        enemy = new Hero(1500, 200, heroStuff);

        // create the rooms
        bedRoom = new Room("in the queen's room.");
        sittingRoom = new Room("in the dressing room");
        drawingRoom = new Room("in the guests room");
        firstFloorStaircase = new Room("in the staircase");
        secondFloorStaircase = new Room("in the staircase");
        library = new Room("in the dungeon.");
        receptionHall = new Room("in the great hall.");
        diningRoom = new Room("in the lord's room");
        dragonLayer = new Room("in the dragon dungeon. Defeat the dragon to save the princess!");
        entrance = new Room("standing in front of the castle");

        // initialise room exits battles and items
        bedRoom.setExit("east", sittingRoom);
        bedRoom.setItem(sword);

        sittingRoom.setExit("west", bedRoom);
        sittingRoom.setExit("east", drawingRoom);
        sittingRoom.setHasBattle(true);
        sittingRoom.setEnemy(enemy);
        sittingRoom.setEnemy(enemy);
        sittingRoom.setEnemy(enemy);
        sittingRoom.setEnemy(enemy);
        sittingRoom.setEnemy(enemy);

        drawingRoom.setExit("west", sittingRoom);
        drawingRoom.setExit("east", secondFloorStaircase);
        drawingRoom.setItem(heal);
        drawingRoom.setItem(heal);
        drawingRoom.setItem(restore);

        //you can not go back its not a bug
        secondFloorStaircase.setExit("west",drawingRoom);
        secondFloorStaircase.setExit("east",library);

        library.setExit("west",secondFloorStaircase);
        library.setEnemy(enemy);
        library.setEnemy(enemy);
        library.setHasBattle(true);
        library.setItem(teleporter);

        firstFloorStaircase.setExit("up", secondFloorStaircase);
        firstFloorStaircase.setExit("east", receptionHall);

        receptionHall.setExit("west", firstFloorStaircase);
        receptionHall.setExit("east", diningRoom);
        receptionHall.setExit("down", entrance);

        //add battle to reception hall and items
        receptionHall.setItem(heal);
        receptionHall.setEnemy(enemy);
        receptionHall.setHasBattle(true);

        diningRoom.setExit("west", receptionHall);
        diningRoom.setEnemy(enemy);
        diningRoom.setEnemy(enemy);
        diningRoom.setHasBattle(true);

        entrance.setExit("up", receptionHall);

        currentRoom = entrance;  // start game outside

        stack.addFirst(currentRoom);
        stackLegacy.push(currentRoom);
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;

        while (!finished) {
            if(!gameWon){
                if(hero.isAlive()){                                       /** Checks since the final batle is not created here **/
                    if(currentRoom.hasBattle()){                /** Checks if the battle is played already **/
                        System.out.println("In this room there are " + currentRoom.numberEnemies() + " enemies");
                        for(int i = 0; i < currentRoom.numberEnemies(); i++){
                            System.out.println("This is enemy number: " + (i + 1));
                            Battle battles = new Battle(hero, currentRoom.getEnemyIndex(i));
                            battles.battle();
                            currentRoom.setHasBattle(false);
                            enemy.revive();
                            enemy.setHealt(250);
                            if(!hero.isAlive()){                       /** The Battle class does not perform the check **/ 
                                System.out.println("You have died! Try aggain.");
                                return; 
                            }
                        }
                    }
                    if(currentRoom.hasItem()){                           /** Since items can only be collected onece **/
                        while(currentRoom.isSearched()){                             /** Prevents the palyer from using all the commands **/ 
                            currentRoom.listItems();
                            currentRoom.setSearched(currentRoom.hasItem());
                            if(currentRoom.hasItem()){
                                Command command = parser.getCommand();
                                if(!command.isUnknown()){
                                    String commandWord = command.getCommandWord();
                                    if(commandWord.equals("get")){
                                        processCommand(command);
                                    }
                                    else if(commandWord.equals("quit")){
                                        currentRoom.setSearched(false);
                                    }
                                    else{
                                        System.out.println("Type quit to exit collection mode");
                                    }
                                }
                                else{
                                    System.out.println("I don't know what you mean...");
                                    continue;
                                }
                            }
                            else continue;
                        }
                    }
                    System.out.println(currentRoom.getLongDescription());
                    Command command = parser.getCommand();
                    finished = processCommand(command);
                }
                else{
                    System.out.println("You have died!");
                    Command c = new Command("quit", null);
                    finished = processCommand(c);
                }
            }
            else break;
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the Adventures of Billy!");
        System.out.println("You are a fearless knight, Named William.");
        System.out.printf("%s\n%s\n%s\n", "Armed with sword and shield,", "you have come to this castle", "to save the princess from the dragon!");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        // System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.go
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if(commandWord.equals("back")){
            goBack();
        }
        else if(commandWord.equals("use")){
            useItem(command);
        }
        else if(commandWord.equals("get")){
            collectItem(command);
        }
        else if(commandWord.equals("list")){
            hero.getInventory().listInventory();
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:
    //some added to be used in the game
    private void collectItem(Command command){
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Get what?");
            return;
        }

        String itemName = command.getSecondWord();
        boolean roomHas = currentRoom.countItems(itemName) > 0;
        // Try to leave current room.

        if (itemName.contains("pp") && roomHas ) {
            Inventory i = hero.getInventory();
            i.addItem(restore);
            hero.setInventory(i);
            currentRoom.removeItem(restore);
            //currentRoom.listItems();
        }
        else if(itemName.contains("heal") && roomHas){
            Inventory i = hero.getInventory();
            i.addItem(heal);
            hero.setInventory(i);
            currentRoom.removeItem(heal);
            //   currentRoom.listItems();
        }
        else if(itemName.contains("sword") && roomHas){
            sword.setStrenght(3000);
            hero.getInventory().getItem("sword").setWeight(-20);
            // Inventory i = hero.getInventory();
            // i.addItem(sword);
            // hero.setInventory(i);
            currentRoom.removeItem(sword);

            //   currentRoom.listItems();
        }
        else if (itemName.contains("telep") && roomHas){
            Inventory i = hero.getInventory();
            i.addItem(teleporter);
            hero.setInventory(i);
            currentRoom.removeItem(teleporter);
            //   currentRoom.listItems();
        }
        else{
            System.out.println("The item does not exist.");   
        }
    }

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("Slay your enemies. Save the princess.");
        System.out.println("I whould probably go to the dining room and after that to the library");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /**
     * Implementing a back command.
     * Does not work at the moment.
     */
    private void goBack(){
        Room temporary = stack.peekFirst();
        stack.removeFirst();
        stackLegacy.pop();
        if(stack.isEmpty()){
            stackLegacy.push(temporary);
            stack.addFirst(temporary);
            currentRoom = stackLegacy.peek();
        }
        else{
            // currentRoom = stack.peekFirst();
            currentRoom = stackLegacy.peek();
        }    
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            Room tmp = currentRoom.clone();
            stack.addFirst(tmp);
            stackLegacy.push(tmp);
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    /**
     * Allows the player to use items he has collected.
     */
    private void useItem(Command command){
        if(!command.hasSecondWord()) {
            System.out.println("Use what?");
            return;
        }

        String itemName = command.getSecondWord();
        boolean hasItem = hero.hasItem(itemName) > 0;
        System.out.println(hasItem);
        // Try to leave current room.

        if (!hasItem) {
            System.out.println("You dont have any!");
            return;
        }
        else {
            if(itemName.equals("ppRestore")){
                hero.setPpDefence(10);
                hero.setPpAttack(15);
                hero.removePossession(itemName);
            }
            else if(itemName.equals("healPotion")){
                hero.setHealt(2000);
                hero.removePossession(itemName);
            }
            else if(itemName.equals("teleporter")){                        /** used to enter the final room, leads to end of the game **/ 
                currentRoom = dragonLayer;
                System.out.println(currentRoom.getLongDescription());
                Inventory i = new Inventory(); 
                Item claws = new Item("sword", "dragons claws", 0);
                claws.setStrenght(750);
                i.addItem(claws);

                Hero dragon = new Hero(10000, 200, i);
                Battle battles = new Battle(hero, dragon);
                battles.battle();  
                if(hero.isAlive()){
                    System.out.println("You have defeated the dragon, saved the  princess and defeated the game.");
                    System.out.println("Congratulations!!!");
                    gameWon = true;
                }
            }
        }
    }
}
