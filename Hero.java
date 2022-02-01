import java.util.HashMap;
import java.util.Set;
/**
 * Write a description of class Hero here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Hero implements Cloneable
{
    // instance variables 
    private Inventory possessions;
    private double healthReset;
    private double health;
    private int speed;
    private int ppAttack;
    private int ppDefence;
    /**
     * Constructor for objects of class Hero
     */
    public Hero(double health, int speed, Inventory possessions)
    {
        this.health = health;
        this.speed = speed;
        healthReset = health;
        this.possessions = possessions;
        ppAttack = 15;
        ppDefence = 10;
    }

    @Override
    public Hero clone(){
        try{
            Hero tmp = (Hero)super.clone(); 
            Inventory temp = possessions.clone();
            tmp.setInventory(temp);
            return tmp;   
        }
        catch(CloneNotSupportedException e){
            throw new AssertionError();
        }
    }

    /**
     * Used to reset the number of power points the player has.
     * @param newPP integer.
     */
    public void setPpAttack(int newPP){
        ppAttack = newPP;    
    }

    /**
     * Used to print information for the player.
     */
    public int getPPdefence(){
        return ppDefence;
    }

    /**
     * Used to print information for the player.
     */
    public int getPPattack(){
        return ppAttack;        
    }

    /**
     * Used to reset the number of power points the player has.
     */
    public void setPpDefence(int newPPd){
        ppDefence = newPPd;   
    }

    /**+
     * Method to remove an item from the palyer's inventory.
     * Does not allow unlimited use of items.
     */
    public void removePossession(String name){
        possessions.removeItem(name);
    }

    /**
     * Checks if the hero has item he is about to use.
     * @param item in the form of a string rather than an object Item.
     * @returns Integer.
     */
    public Integer hasItem(String item){
        HashMap<Item, Integer> contained = possessions.getContent(); 
        Set<Item> items = contained.keySet();
        Integer count = 0;
        for(Item i : items){
            if(i.getName().equals(item)){
                count = contained.get(i);
            }
        }
        return count;
    }

    /**
     * Setter for the HP points of the player.
     * It recalculates the points left.
     */
    public void setHealt(double damage)
    {
        health += damage; 
    }

    /**
     * returns the whole inventory of the player.
     * Used to get it's content inside the game Class.
     * @returns   Class Inventory.  
     */
    public Inventory getInventory(){
        return possessions;   
    }

    /**
     * Class to override, if needed, the Inventory of a "hero"
     */
    public void setInventory(Inventory stuff){
        possessions = stuff;   
    }

    /**
     * Used in class Battle.
     * Standart getter.
     */
    public int getAttack(){
        return possessions.findItemStrenghts("sword");
    }

    /**
     * Used in class Battle.
     * Standart getter.
     */
    public double getHealth(){
        return health;
    }

    /**
     * Used in class Battle.
     * Standart getter.
     */
    public int getSpeed(){
        return speed;   
    }

    /**
     * Used in class Battle.
     * Calculates speed to deside who goes first.
     * Takes the weigt of inventory as a desiding factor.
     */
    public int calculateSpeed(){
        return speed += -possessions.getTotalWeight();   
    }

    /**
     * Used to reset the state of the game, as well as use the same enemy for multiple battles.
     */
    public void revive(){
        health = healthReset;
    }

    /**
     * Used to end the game and the battle.
     * @returns param boolean.
     */
    public boolean isAlive(){
        if(health > 0){
            return true;
        }   
        else return false;
    }
}
