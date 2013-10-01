import org.jbox2d.common.Vec2;
import city.soi.platform.*;

/**
 * Item that is thrown in the game by player. When it collides with any body, it gets destroyed. 
 */
public class FlyingBottle extends Body implements CollisionListener, StepListener
{   
    /** The game in which the player is playing. */
    private Game game; 
    
    /** The player. */
    private Player player;
    
    /** The level. */
    private GameLevel level;
    
    /** Target positions to place bottle. */
    private int targetPositionX;
    private int targetPositionY;
    
    /**
     * Initialise a new Flying Bottle.
     * @param game The game.
     */
    public FlyingBottle(Game game)
    {
        super(game.getWorld());
        
        // set the game
        this.game = game;
        
        // add shape to it
        Shape bottleShape = new PolygonShape(4.0f,35.0f, 9.0f,3.0f, 9.0f,-35.0f, -9.0f,-35.0f, -9.0f,3.0f, -6.0f,35.0f);
        addShape(bottleShape);
        
        // set the player
        player = game.getPlayer();
     
        // set bottle's image
        setImage(new BodyImage("bottle.png", new Vec2(0, 1), 1));
        
        // add CollisionListener
        getWorld().addCollisionListener(this);
        
        // add step listener
        getWorld().addStepListener(this);
        
        // set level
        level = game.getLevel();

    }
    
    /** Place bottle where player currently is. */
    public void placeBottle()
    {
            targetPositionX = player.getPlayerPositionX();
            targetPositionY = player.getPlayerPositionY();
            setPosition(new Vec2(targetPositionX, targetPositionY));
    }
    
    /** Throw bottle to the right. */
    public void throwBottleRight()
    {
        applyImpulse(new Vec2(200, 300));
    }
    
    /** Throw bottle to the left. */
    public void throwBottleLeft()
    {
        applyImpulse(new Vec2(-200, 300));
    }
    
    /** Using Collision Listener:
     *  When collision with any body occurs but not the player or Boxy,
     *  destroy bottle.
    */
    public void collide(CollisionEvent e)
    {
       // check if it's NOT player or if it's NOT Boxy
       if (e.getOtherBody() != player && !(e.getOtherBody() instanceof Boxy))
       {           
           // destroy bottle
           destroy();           
       }
    }
    
    /** Destroy bottle if it gets below -500.
    */
    public void postStep(StepEvent e)
    { 
        // get bottle's position in float
        float flyingBottlePositionY = getPosition().y;
        
        if (flyingBottlePositionY < -500.0)
        {
            destroy();
        }
    }
    
    public void preStep(StepEvent e) {}
    
    

}
