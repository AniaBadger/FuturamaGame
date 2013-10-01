import org.jbox2d.common.Vec2;
import city.soi.platform.*;

import java.io.Serializable;

/**
 * Pick-ups in a game. When the player collides with an bottle, the
 * player's bottle count and strength is increased, the bottle is removed
 * from the world. 
 */
public class Bottle extends Body implements CollisionListener, Serializable
{   
    /** The game in which the player is playing. */
    private Game game; 
    
    /** The player. */
    private Player player;
    
    private GameLevel level;
    
    /**
     * Initialise a new Bottle.
     * @param game The game.
     */
    public Bottle(Game game)
    {
        //make bottle object
        super(game.getWorld(), Body.Type.STATIC);
        
        // set the game
        this.game = game;
        
        // add shape to it
        Shape bottleShape = new PolygonShape(4.0f,35.0f, 9.0f,3.0f, 9.0f,-35.0f, -9.0f,-35.0f, -9.0f,3.0f, -6.0f,35.0f);
        addShape(bottleShape);
        
        // make the bottle ghostly
        bottleShape.setGhostly(true);
        
        Shape bottleShape2 = new PolygonShape(4.0f,35.0f, 9.0f,3.0f, 9.0f,-35.0f, -9.0f,-35.0f, -9.0f,3.0f, -6.0f,35.0f);
        addShape(bottleShape2);
        
        // make the bottle sensor
        bottleShape2.setIsSensor(true);
        
        // give bottle 0 gravity
        setGravityStrength(0);
        
        // set the player
        player = game.getPlayer();
        
        // set level
        level = game.getLevel();
     
        // set bottle's image
        setImage(new BodyImage("bottle.png", new Vec2(0, 1), 1));
        
        // add CollisionListener
        getWorld().addCollisionListener(this);
    }
    
    /** Using Collision Listener:
     *  When collision with player occurs,
     *  increase the bottle count,
     *  increase the player's strength,
     *  destroy bottle.
    */
    public void collide(CollisionEvent e)
    {
       // check if it's player
       if (e.getOtherBody() == player)
       {
           // increase bottle count
           player.incrementBottleCount();
           
           // increase player's strength
           player.incrementPlayerStrength(); 
           
           // destroy bottle
           destroy();
           
       }
    }

}
