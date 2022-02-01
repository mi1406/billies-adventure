
import java.util.Random;
/**
 * Class Battle.
 * A game within the game, copies most of it's functionality and implementation from
 * "World of Zuul" a game by Michael Kölling and David J. Barnes. 
 *
 * @author (your name)
 * @version (27/04/2020)
 */
public class Battle
{
    // instance variables 
    private Parser parser;
    private Hero enemyStats;
    private Hero heroStats;
    private Random rand;
    private boolean isFinnished;
    private int ppAttack;
    private int ppDefend;

    /**
     * Constructor for objects of class Battle
     * A turn based combat mechanic.
     */
    public Battle(Hero heroStats, Hero enemyStats )
    {   this.heroStats = heroStats;
        this.enemyStats = enemyStats;
        rand = new Random();
        isFinnished = false;
        parser = new Parser();
        ppAttack = heroStats.getPPattack();
        ppDefend = heroStats.getPPdefence();
    }

    /**
     * Equivalent to method play in "Game"
     * Checks if both ennemies are alive, if not the battle ends,
     * the evaluation wheter the player wins or looses is done in Game class.
     */
    public void battle(){
        printPrompt();
        boolean heroGoesFirst = heroStats.calculateSpeed() > enemyStats.calculateSpeed();
        while(enemyStats.isAlive() && heroStats.isAlive()){
            System.out.println("ppAttack: " + ppAttack + "  ppDefend : " + ppDefend);
            System.out.println("Your HP: " + heroStats.getHealth() + "  Enemy HP:  " + enemyStats.getHealth());
            Command command = parser.getCommand();
            processCommand(command, heroGoesFirst);
        }
        isFinnished = true;

    }

    /**
     * Prints prompt.
     */
    private void printPrompt(){
        System.out.println("You have encountered an ennemy!");   
        System.out.println("Your commands are attack, defend, use");
    }

    /**
     * Same as the method in class Game.
     * Exists only so the original method stay private.
     * Uses different command words since the actor battles and
     * does not navigate.
     * @param Command returned by parser and entered by player
     * @boolean deside if the player or the enemy goes first.
     */
    private void processCommand(Command command, boolean heroFirst){
        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("attack")) {
            if(heroFirst && ppAttack > 0){
                heroDamage();
                double damageEnemy = enemyStats.getAttack() * (1 + (double)rand.nextInt(6)/10.0);
                heroStats.setHealt(-damageEnemy);
            }
            else if(!heroFirst && ppAttack > 0){
                double damageEnemy = enemyStats.getAttack() * (1 + (double)rand.nextInt(6)/10.0);
                heroStats.setHealt(-damageEnemy);
                heroDamage();
            }
            else{
                System.out.println("You don't have any more left.");
                System.out.println("Try using another move or use an item.");
                return;
            }
        }
        else if (commandWord.equals("defend")) {
            doubleDamage();
        }
        else if (commandWord.equals("use")) {
            useItem(command, heroFirst);
        }
        else if(commandWord.equals("list")){
            heroStats.getInventory().listInventory();
        }
        else if(commandWord.equals("go") || commandWord.equals("quit") || commandWord.equals("help") || commandWord.equals("back") || commandWord.equals("get") || commandWord.equals("teleport")){
            System.out.println("You can not use those here!");
            System.out.println("Your commands are attack, defend, use");
        }
    }

    /**
     * Saves typing the same code over and over.
     * Calculates the damage dealth by the player at the given turn.
     * Uses the equivalent of a six sided die for attack boost.
     */
    private void heroDamage(){
        double damageHero = (double)heroStats.getAttack() * (1 + (double)rand.nextInt(6)/10.0);
        enemyStats.setHealt(-damageHero);
        ppAttack -= 1; 
        heroStats.setPpAttack(ppAttack);
    }

    /**
     * Actually the outcome of the player using command defend.
     * The idea is that the enemy attack bounces back.
     */
    private void doubleDamage(){
        if(ppDefend > 0){
            double damageEnemy = enemyStats.getAttack() * (1 - (double)rand.nextInt(6)/10.0);
            heroStats.setHealt(-damageEnemy);
            enemyStats.setHealt(-damageEnemy/2);
            ppDefend -= 1;
            heroStats.setPpDefence(ppDefend);
        }
        else{
            System.out.println("You don't have any more left.");
            System.out.println("Try using another move or use an item.");
            return;
        }
    }

    /**
     * Allows the player to use an item in battle 
     * either a heal or pp(Power Points) restore.
     * Same implementation as goRoom() in game.
     * Player does not attack if item is used.
     * @param command user input processed by the parser
     * @param boolean checks if hero geos first.
     */
    public void useItem(Command command, boolean heroFirst){
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Use what?");
            return;
        }

        String itemName = command.getSecondWord();
        //checks if the player has the item
        boolean hasItem = heroStats.hasItem(itemName) > 0;
        //since items are used with the command use, a check is performd if there is a valid name of an item

        if (!hasItem) {
            System.out.println("You dont have any!");
            return;
        }
        else {
            if(itemName.equals("ppRestore"))
                if(heroFirst){
                    heroStats.setPpAttack(15);
                    heroStats.setPpDefence(10);
                    double damageEnemy = enemyStats.getAttack() * (1 + (double)rand.nextInt(6)/10.0);
                    heroStats.setHealt(-damageEnemy);
                    heroStats.getInventory().removeItem(itemName);
                    ppAttack = 15;
                    ppDefend = 10;
                }
                else{
                    double damageEnemy = enemyStats.getAttack() * (1 + (double)rand.nextInt(6)/10.0);
                    heroStats.setHealt(-damageEnemy);  
                    heroStats.getInventory().removeItem(itemName);
                    heroStats.setPpAttack(15);
                    heroStats.setPpDefence(10);
                    ppAttack = 15;
                    ppDefend = 10;
                }
            else if(itemName.equals("healPotion")){
                if(heroFirst){
                    heroStats.setHealt(2000);
                    double damageEnemy = enemyStats.getAttack() * (1 + (double)rand.nextInt(6)/10.0);
                    heroStats.setHealt(-damageEnemy);
                    heroStats.getInventory().removeItem(itemName);
                }
                else{
                    double damageEnemy = enemyStats.getAttack() * (1 + (double)rand.nextInt(6)/10.0);
                    heroStats.setHealt(-damageEnemy);   
                    heroStats.setHealt(2000);
                    heroStats.getInventory().removeItem(itemName);
                }
            }
        }
    }
}   

