import org.jbox2d.common.Vec2;
import city.soi.platform.*;

/**
 * Enemy in a game. When the player collides with a hypnotoad, the
 * player's ability to move is frozen for few seconds,
 * same time the hypnotoad starts to glow for the amount of time the player is frozen. 
 */
public class Hypnotoad extends SlidingPlatform implements CollisionListener , StepListener
{   
    /** The game in which the player is playing. */
    private Game game;    
    
    /** The Player. */
    private Player player;
    
    /** Freeze the plater? */
    private boolean freeze;
    
    /** Freeze time. */
    private float freezeTime;
    
    /**
   * Initialise a new Hypnotoad.
   * @param game The game.
   */
    public Hypnotoad(Game game)
    {
        // create hypnotoad object
        super(game.getWorld(), new PolygonShape(-9.0f,33.0f, 29.0f,33.0f, 29.0f,-31.0f, -28.0f,-31.0f, -28.0f,4.0f), new Vec2(50, 0), 1);

        // set game
        this.game = game;
        
        // set player
        player = game.getPlayer();
        
        // set image for hypnotoad
        setImage(new BodyImage("hypnotoad.png", new Vec2(0, -1), 1));
        
        // add Collision Listener
        getWorld().addCollisionListener(this);
        
        // add Step Listener
        //getWorld().addStepListener(this);
        
        // set default freeze value
        freeze = false;
        
        // set default freeze time
        freezeTime = 0.0f;
    }    
    
    /** What the freeze time is. */
    private float getFreezeTime()
    {
        return freezeTime;
    }
    
    /** Using Collision Listener:
     * When hypnotad collides with player
     * freeze is set to true
     * and freeze time to 10.
    */
    public void collide(CollisionEvent e)
    {
       // check if it's player
       if (e.getOtherBody() == player)
       {           
           // set freeze
           freeze = true;
           
           // set freeze time
           freezeTime = 10.0f;
       }
    }
    
    /** Using Step Listener:
     * When hypnotad collided with player:
     * 1. Decreasing freeze time till 0.
     * 2. If freeze time is greater than 0, them make the hypnotoad glow
     * and keep the player frozen
     * 3. When it's less than 0, restore player's functions, stop the hypnotoad glowing
    */
    public void postStep(StepEvent e)
    {
        // calling collision values from superclass not to overwrite important stuff
        super.postStep(e);
        
        // Decrease freeze time 1 step a time
        freezeTime = freezeTime - e.getStep();

        // check freeze time and freeze value
        if ((freezeTime > 0.0f) && (freeze == true)) {
            
            // set gravity strength of player to 100 so it can't jump
            player.setGravityStrength(100);
            
            // set player's walking speed to 0 so it can't walk
            player.setWalkSpeed(0);
            
            // move player slightly to the left so it's out of reach of hypnotoad
            player.move(new Vec2(-0.2f, 0));
            
            // make hypnotoad glow
            setImage(new BodyImage("hypnotoad_active.png", new Vec2(0, -1), 1));
        }
        
        // set stuff back to normal
        else
        {
            freezeTime = 0.0f;
            freeze = false;
            player.setGravityStrength(1);
            player.setWalkSpeed(90);
            setImage(new BodyImage("hypnotoad.png", new Vec2(0, -1), 1));
        }
              

    }
    
    public void preStep(StepEvent e) { super.preStep(e); }

}
