import org.jbox2d.common.Vec2;
import city.soi.platform.*;

/**
 * Item that is dropped by Boxy. When it collides with any body apart from Player and Boxy, it explodes. 
 * When collides with Player, reduces player's lives, sets his strength to 10, puts at start of level.
 */
public class Bomb extends Body implements CollisionListener, StepListener
{   
    /** The game in which the player is playing. */
    private Game game; 
    
    /** The player. */
    private Player player;
    
    /** The level. */
    private GameLevel level;
    
    /** Position used to place bomb. */
    private int targetPositionX;
    private int targetPositionY;
    
    /** Explosion animation time. */
    private float animationTime;
    
    /**
     * Initialise a new Bomb.
     * @param game The game.
     */
    public Bomb(Game game)
    {
        super(game.getWorld());
        
        // set the game
        this.game = game;
        
        // add shape to it
        Shape bombShape = new PolygonShape(-16.0f,16.0f, 15.0f,16.0f, 15.0f,-14.0f, -16.0f,-14.0f);
        addShape(bombShape);
        
        // make the bomb ghostly
        bombShape.setGhostly(true);
        
        Shape bombShape2 = new PolygonShape(-16.0f,16.0f, 15.0f,16.0f, 15.0f,-14.0f, -16.0f,-14.0f);
        addShape(bombShape2);
        
        // make the bomb sensor
        bombShape2.setIsSensor(true);
        
        // set the player
        player = game.getPlayer();
     
        // set bomb's image
        setImage(new BodyImage("bomb.png", new Vec2(0, 1), 1));
        
        // add CollisionListener
        getWorld().addCollisionListener(this);
        
        // add step listener
        getWorld().addStepListener(this);
        
        // set level
        level = game.getLevel();
        
        // make bottle not to rotate when collide with other body
        setFixedRotation();
        
        // set animation time
        animationTime = 0.0f;

    }
    
    /** Place bomb where Boxy currently is.
       * @param x X coord.
       * @param y Y coord.
    */
    public void placeBomb(int x, int y)
    {
            targetPositionX = x;
            targetPositionY = y;
            setPosition(new Vec2(targetPositionX, targetPositionY));
    }
    
    
    /** Using Collision Listener:
     *  When collision with any body occurs, but not Boxy, play animation.
     *  When collides with player, reduces player's lives, sets his strength to 10, puts at start of level.
    */
    public void collide(CollisionEvent e)
    {
       // check if it's NOT player or if it's Boxy
       if (e.getOtherBody() == player)
       {           
           // destroy bottle
           player.decrementLivesCount();       
           level.putPlayerAtStart();
           player.setStrength(10.0f); 
           
           playAnimation(5.0f);         
           
       }
       else
       {
           if (e.getOtherBody() != player && !(e.getOtherBody() instanceof Boxy))
           {
               playAnimation(5.0f);             
           }
       }
    }
    
    /** If bomb gets below -300, it gets destroyed. 
       * Also - reduce animation time 1 step at a time.
       */
    public void postStep(StepEvent e)
    { 
        // get bottle's position in float
        float bombPositionY = getPosition().y;
        
        if (bombPositionY < -300.0)
        {
            destroy();
        }
        
        animationTime = animationTime - e.getStep();
    }
    
    public void preStep(StepEvent e) {}
    
    /** Play animation for given amount of time.
       * @param animationTime animation time.
    */    
    public void playAnimation(float animationTime)
    {
        if (animationTime > 0.0f)
        {
            // set bottle's image
            setImage(new BodyImage("animated_bomb.gif", new Vec2(0, 1), 1));
        }
    }
    
    

}