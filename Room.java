import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Room implements Cloneable
{
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private String story;
    private ArrayList<Item> items;
    private ArrayList<Hero> fainds;
    private boolean hasBattle;
    private boolean searched;
    private boolean setUpRead;

    private ArrayList<Item> itemsReset;
    private ArrayList<Hero> faindsReset;
    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        itemsReset = new ArrayList<>();
        fainds = new ArrayList<Hero>();
        items = new ArrayList<>();
        faindsReset = new ArrayList<>();
        exits = new HashMap<>();
        searched = true;
        setUpRead = false;
    }

    @Override
    public Room clone(){
        try{
            Room tmp = (Room)super.clone();
            tmp.setItemsClone(itemsReset);
            return tmp;
        }
        catch(CloneNotSupportedException e){
            throw new AssertionError();
        }
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Used to reset the room to a previous state as specified by the requirements for a back command.
     * Currently does not work.
     */
    public void setEnemiesClone(ArrayList<Hero> clones){
        fainds = clones;
    }

    public void setItemsClone(ArrayList<Item> clone){
        items = clone;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "\n" + "You are " + description + ".\n" + getExitString();
    }

    /**
     * Prints the string describinf the room atmosphere
     */
    public void printStory(){
        if(!setUpRead){
            System.out.println(story);
            setUpRead = true;
        }
    }

    /**
     * Adds a longer description to the room.
     */
    public void setSetUp(String story){
        this.story = story;   
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    public String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }

    /**
     * Checks if the current room has battle
     */
    public boolean hasBattle(){
        return hasBattle;   
    }

    /**
     *  Method allowing the placement of items in the room.
     */
    public void setItem(Item item){
        items.add(item);
        itemsReset.add(item.clone());
    }

    /**
     *  Method removing taken item from the room.
     */
    public void removeItem(Item item){
        items.remove(item);
    }

    /**
     *  Method allowing the player to "see"
     *  the content of the room.
     */
    public void listItems(){
        System.out.println();
        for(Item i : items){
            System.out.print("  " + i.getName());
        }   
        System.out.println();
    }

    /**
     * Checks if the room has any items.
     * Used by Game class.
     * @returns boolean
     */
    public boolean hasItem(){
        if(items.isEmpty()){
            return false;
        }
        else return true;
    }

    /**
     * Used to loop through the list od enemies 
     * and return them one by one for rooms with 
     * more than one enemy.
     * @param index of enemy inside the ArrayList.
     * @returns The enemy object.
     */
    public Hero getEnemyIndex(int index){
        int i = 0;
        for(Hero h : fainds){
            if(i == index){
                return h;
            }
            else{
                i++;   
            }
        }
        return null;
    }

    /**
     * Counts how many items are there in a room.
     * @param ItemName string with the item name the way it's called in the game.
     * @returns int number of items.
     */
    public int countItems(String itemName){
        int count = 0;
        for(Item i : items){
            if(i.getName().equals(itemName)){
                count++;
            }
        }
        return count;
    }

    /**
     * Method to set the state of the room.
     * Does not allow items to be taken upon reentry.
     * @param state true or false
     */
    public void setSearched(boolean state){
        searched = state;   
    }

    /**
     * Returns the effect of method setSearched();
     * @returns boolean.
     */
    public boolean isSearched(){
        return searched;   
    }

    /**
     * Method to add a enemy after the creation of the room
     * @param enemy required class Hero.
     */
    public void setEnemy(Hero faind){
        fainds.add(faind);
        faindsReset.add(faind.clone());
    }

    /**
     * Method used to prevent triggering of battle upon reentry.
     * @param state boolean
     */
    public void setHasBattle(boolean state){
        hasBattle = state;   
    }

    /**
     * Method giving the size of the ArrayList containing the enemies.
     * @returns Integer.
     */
    public int numberEnemies(){
        return fainds.size();
    }

    public ArrayList<Item> getItems(){
        return items;   
    }

}

