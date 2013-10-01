import city.soi.platform.*;
import org.jbox2d.common.Vec2;

import java.util.HashMap;

/**
 * In this level Bender must collect 4 bottles to get to the next level, he must beware of the Devil.
 */
public class Level2 extends GameLevel
{   
    /** The Devil. */
    private Devil devil;
    
    /** Does enemy exists?. */
    private boolean enemyExist;
    
    /** Ground bodys. */
    private Body ground1;
    
    /** The World in which the game bodies move and interact.*/
    private World world;
    
    /** The player (a specialised Actor). */
    private Player player;

    
    /**
     * Initialise game level 2.
     * @param game the game to which this level belongs
     */
    public Level2(Game game)
    {
        super(game);
        // set world
        world = game.getWorld();
        //set player
        player = game.getPlayer();   
        // set default enemy exists state
        enemyExist = true;
    }
    
    /** Populate level with bodies. */
    public void populate() {
        
        // make some grounds
        ground1 = new Body(world, PolygonShape.makeBox(100, 5), Body.Type.STATIC);
        ground1.setPosition(new Vec2(-380, -200));

        Body ground2 = new Body(world, PolygonShape.makeBox(50, 5), Body.Type.STATIC);
        ground2.setPosition(new Vec2(50, 100));

        Body ground3 = new Body(world, PolygonShape.makeBox(100, 5), Body.Type.STATIC);
        ground3.setPosition(new Vec2(200, 200));
        
        // make some static platforms
        Body staticPlatform = new Body(world, PolygonShape.makeBox(50, 5),Body.Type.STATIC);
        staticPlatform.setPosition(new Vec2(200, -100));
        
        // make a moving platform        
        Body movingPlatform = new SlidingPlatform(world, PolygonShape.makeBox(100, 5), new Vec2(130, 10), 2);
        movingPlatform.setPosition(new Vec2(-160, -50));
        
        // make bottles
        Bottle bottle1 = new Bottle(game);
        bottle1.putOn(staticPlatform);
        bottle1.setName("bottle1");
        
        Bottle bottle2 = new Bottle(game);
        bottle2.putOn(ground1);
        bottle2.setName("bottle2");
        
        Bottle bottle3 = new Bottle(game);
        bottle3.putOn(ground2);
        bottle3.setName("bottle3");
        
        Bottle bottle4 = new Bottle(game);
        bottle4.putOn(ground3);
        bottle4.setName("bottle4");
        
        // make the devil
        devil = new Devil(game);
        devil.putOn(ground3);
        devil.setName("devil");
       
    }
    
    
    /** When is level completed?. */
    public boolean isCompleted() {
        
        // set amount of bottles to complete the level
        int bottlesAmount = 4;
        
        // if player collects the set amount, level is completed
        if (player.getBottles() == bottlesAmount)
        {
            return true;
        }
        else
        {
            return false;
        }
    }     
    
    /** Put Player at start of level. */
    public void putPlayerAtStart() {
        // put the player on the ground
        player.putOn(ground1); 
        player.move(new Vec2(-90, 0));
        
        // enemy exists
        enemyExist = true;
    }
    
    /** Does enemy exists?. */
    public boolean doesEnemyExist() {
        return enemyExist;
    }
    
    /** Clear level of bodies, set existence of enemy to false. */
    public void clearLevel() {
        enemyExist = false;
        super.clearLevel();
    }
    

}
