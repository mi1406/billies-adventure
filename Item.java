
/**
 * Stores information about the items
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Item implements Cloneable
{
    private String name;
    private String description;
    private int helth;
    private int strenght;
    private int weight;
    /**
     * Constructor for objects of class Item
     */
    public Item(String name, String description, int weight)
    {
        // initialise instance variables
        this.name = name;
        this.description = description;
        this.weight = weight;
    }

    /**
     * Override method of the cuper class Object.
     * Used by implementing clonable interface.
     */
    @Override
    public Item clone(){
        try{
            return (Item)super.clone();   
        }
        catch(CloneNotSupportedException e){
            throw new AssertionError();
        }
    }

    public void setWeight(int newWeight){
        weight = newWeight;   
    }

    /**
     * Since the player can pick up a new weapon.
     */
    public void setStrenght(int strenght){
        this.strenght = strenght;
    }

    /**
     * @returns The damage dealth by the actors in the form of an integer.
     */
    public int getStrenght(){
        return strenght;   
    }

    /**
     * Setter of the item damage.
     * Currently not used since such mechanic is absent from the game.
     */
    public void setHelth(){
        this.helth = helth;
    }

    /**
     * prevents trying to add an null to inventory whch can be problematic later on.
     * @returns boolean.
     */
    public boolean isEmpty(){
        if(name.equals(null) && description.equals(null)){
            return true;
        }
        else return false;
    }

    /**
     *@returns the name of the Item.
     *In order for the whole game to work, the instance variable name and this name need to be the same.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Currently unused. 
     * Whould be useful for providing option to inspect an item in game.
     */
    public String getDescription(){
        return description;   
    }

    /**
     * Used by inventory to calculate total weight and from there how much is the player
     * slowed down.
     */
    public int getWeight(){
        return weight;   
    }
}
