import city.soi.platform.*;
import org.jbox2d.common.Vec2;

import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import java.util.HashMap;


/**
 * In this level Bender must collect 3 bottles to get to the next level, he must beware of the Boxies floating around and dropping bombs.
 */
public class Level3 extends GameLevel
{   
    /** Ground body. */ 
    private Body ground1;
    
    /** The World in which the game bodies move and interact.*/
    private World world;
    
    /** The player (a specialised Actor). */
    private Player player;
    
    /** Game frame. */ 
    private JFrame frame;
    
    /** Game. */
    private Game g;
    
    /** Flying Bottle. */
    private FlyingBottle flyingBottle;
    
    /** Does enemy exists?. */
    private boolean enemyExist;
    
    
    /**
     * Initialise game level 3.
     * @param game the game to which this level belongs
     */
    public Level3(Game game)
    {
        super(game);
        
        // set game var
        g = game;
        
        // set world
        world = game.getWorld();
        //set player
        player = game.getPlayer();
        // set default enemy exists state
        enemyExist = true;
        // set frame
        frame = g.getFrame();

    }
    
    /** Populate level with bodies. */
    public void populate() {     
        
        // make some grounds
        ground1 = new Body(world, PolygonShape.makeBox(100, 5), Body.Type.STATIC);
        ground1.setPosition(new Vec2(-380, -200));
        
        Body ground2 = new Body(world, PolygonShape.makeBox(100, 5), Body.Type.STATIC);
        ground2.setPosition(new Vec2(-0, -200));

        Body ground3 = new Body(world, PolygonShape.makeBox(100, 5), Body.Type.STATIC);
        ground3.setPosition(new Vec2(300, -100));
        
        // make a moving platform        
        Body movingPlatform = new SlidingPlatform(world, PolygonShape.makeBox(100, 5), new Vec2(130, 0), 2);
        movingPlatform.setPosition(new Vec2(-260, -150));
        
        // make some bottles
        Bottle bottle1 = new Bottle(game);
        bottle1.putOn(ground1);
        bottle1.setName("bottle1");
        
        Bottle bottle2 = new Bottle(game);
        bottle2.putOn(ground2);
        bottle2.setName("bottle2");
        
        Bottle bottle3 = new Bottle(game);
        bottle3.putOn(ground3);
        bottle3.setName("bottle3");
        
        // show dialog with information about level
        JOptionPane.showMessageDialog(frame, "Press N or M to throw bottles to kill Boxies.", "Level instructions:", JOptionPane.PLAIN_MESSAGE);
        
        // make some boxies
        Boxy boxy1 = new Boxy(game);
        boxy1.setName("boxy1");
        Vec2 vec1 = new Vec2(100, 0);
        boxy1.move(vec1);

        Boxy boxy2 = new Boxy(game);
        boxy2.setName("boxy2");
        Vec2 vec2 = new Vec2(-100, 200);
        boxy2.move(vec2);

        Boxy boxy3 = new Boxy(game);
        boxy3.setName("boxy3");
        Vec2 vec3 = new Vec2(-400, 200);
        boxy3.move(vec3);
        
    }
    
    
    /** When is level completed?. */
    public boolean isCompleted() {
        
        // set amount of bottles to complete the level
        int bottlesAmount = 3;
        
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
    
    /** Does enemy exists?. */
    public boolean doesEnemyExist() {
        return enemyExist;
    }
    
    /** Put Player at start of level. */
    public void putPlayerAtStart() {
        // put the player on the ground
        player.putOn(ground1); 
        player.move(new Vec2(-90, 0));
    }
    
    /** Clear level of bodies. */
    public void clearLevel() {
        super.clearLevel();
    }

}
