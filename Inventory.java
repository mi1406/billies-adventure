import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;
/**
 * Write a description of class Inventory here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Inventory implements Cloneable
{
    HashMap<Item, Integer> possessions;

    /**
     * Constructor for objects of class Inventory
     */
    public Inventory()
    {  
        possessions = new HashMap<>();
    }

    /**
     * Method providing the functionality for the "list" command in Game.
     */
    public void listInventory(){
        Set<Item> it = possessions.keySet();
        for(Item i : it){
            System.out.print(i.getName() + ": " + possessions.get(i) + "    ");    
        }
    }

    public Item getItem(String item){
        Set<Item> items = possessions.keySet();
        for(Item i : items){
            if(i.getName().equals(item)){
                return i;
            } 
        }
        return null;
    }

    @Override
    public Inventory clone(){
        try{
            return (Inventory)super.clone();   
        }
        catch(CloneNotSupportedException e){
            throw new AssertionError();
        }
    }

    /**
     * Method to add an item after the creation of an instance.
     * Adds the item under two forms as a object from class Item and
     * as a String.
     * @param items object calss Item.
     */

    public void addItem(Item items)
    {  
        Integer numberPossessions = possessions.getOrDefault(items, (Integer)0);
        possessions.put(items, numberPossessions +1);
    }

    /**
     * Method removing item
     * @param nameItem class String as specified by Game class.
     */
    public void removeItem(String nameItems){
        Set<Item> items = possessions.keySet();
        for(Item i : items){
            if(i.getName().equals(nameItems)){
                possessions.put(i, (possessions.get(i) - 1));
            }
        }
    }

    /**
     * Method used by class Battle to determine the 
     * power of attack.
     */
    public int findItemStrenghts(String item){
        Set<Item> items = possessions.keySet();
        Iterator<Item> it = items.iterator();
        int swordStrenght = 0;
        while(it.hasNext()){
            Item damn = it.next();
            if(damn.getName().equals(item)){
                swordStrenght = damn.getStrenght();
            }
        }
        return swordStrenght;
    }

    /**
     * Resturns the whole content
     * @returns HashMap < <String, Integer  > > 
     */
    public HashMap<Item, Integer> getContent(){
        return possessions;
    }

    /**
     * Calculates the total weight of inventory carrried by the hero.
     * Currently used to calculate speed.
     * Can be used as a linit for items.
     * @return int total weigh carried.
     */
    public int getTotalWeight(){
        Set<Item> items = possessions.keySet();
        int weight = 0;
        for (Item i : items){
            weight += (i.getWeight() * possessions.get(i));
        }
        return weight;
    }

}
