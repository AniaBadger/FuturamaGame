import org.jbox2d.common.Vec2;
import city.soi.platform.*;

import java.util.Random;
import java.util.ArrayList;

import java.lang.Math;

/**
 * Enemy in a game. When the player collides with a boxy, the
 * player's strength is reduced.
 * Boxy drops bombs.
 * When Boxy collides with flying bottle, it is destroyed.
 * Boxy moves around randomly, changing it's position coordinates at random, every random amount of seconds. 
 * It's also protected from leaving the visible game area.
 * When player gets too close to Boxy, boxy change eyes colour and starts chasing Player.
 */
public class Boxy extends Body implements CollisionListener , StepListener
{   
    /** The game in which the player is playing. */
    private Game game;    

    /** The Player. */
    private Player player;
    
    /** The level. */
    private GameLevel level;
    
    /** Move time - for moving. */
    private float moveTime;
    
    /** Random X coord generator. */
    private Random randomizerX;
    
    /** Random Y coord generator. */
    private Random randomizerY;
    
    /** Random sec time generator. */
    private Random randomizerTime;
    
    /** Move time - for Following. */
    private float moveTimeF;
    
    /** Array recording player's path. */
    private ArrayList<Vec2> playerHistory;
    
    /** index to iterate through player history array. */
    private int index;
    
    /** check for devil if to go out of tracing step. */
    private boolean check;
    
    /** Move randomly? */
    private boolean move;
    
    /** Start following player? */
    private boolean follow;
    
    /**
    * Initialise a new Boxy.
    * @param game The game.
    */
    public Boxy(Game game)
    {
        super(game.getWorld());
        
        // set and add shape
        Shape boxyShape = new PolygonShape(-29.0f,38.0f, 36.0f,38.0f, 36.0f,-38.0f, -41.0f,-38.0f);
        addShape(boxyShape);

        // set game
        this.game = game;
        
        // set image for boxy
        setImage(new BodyImage("boxy.png", new Vec2(0, -1), 1)); 
        
        // make boxy not affected by gravity
        setGravityStrength(0.0f);
        
        // make boxe not to rotate when collide with other body
        setFixedRotation();
        
        // set player
        player = game.getPlayer();   
        
        // set level
        level = game.getLevel();
        
        // add Collision Listener
        getWorld().addCollisionListener(this);
        
        // add Step Listener
        getWorld().addStepListener(this);
        
        // set default move time for moving
        moveTime = 0.0f;
        
        // set default move time for following
        moveTimeF = 2.0f;
        
        // make new player history array
        playerHistory = new ArrayList<Vec2>();
        
        // set beginning index
        index = 0;
        
        // set default check state
        check = true;
        
        // set move to true
        move = true;
        
        // set follow to false
        follow = false;
    }   
    
    /**
    * Set if Boxy should move.
    * @param m true/false.
    */
    public void setMove(boolean m)
    {
        move = m;
    }
    
    /** Get move value */
    public boolean getMove()
    {
        return move;
    }
    
    /**
    * Set if Boxy should follow player.
    * @param f true/false.
    */
    public void setFollow(boolean f)
    {
        follow = f;
    }
    
    /** Get follow value */
    public boolean getFollow()
    {
        return follow;
    }
    
    /** Using Collision Listener:
     * When boxy collides with player, player's strength is reduced.
     * When it collides with flying bottle, it is destroyed.
    */
    public void collide(CollisionEvent e)
    {
       // check if it's player
       if (e.getOtherBody() == player)
       {           
           float strength = player.getStrength();
           strength = strength - 10.0f;
           player.setStrength(strength);
       }
       else
       {
           // check if it's flying bottle
           if (e.getOtherBody() instanceof FlyingBottle)
           {
               String boxyName = getName();           
               e.getOtherBody().destroy();
               destroy();
           }
       }
    }
    
    /** Using Step Listener:
     * 1. Decrease move time on every step a time.
     * 2. When time to move, make Boxy change it's velocity randomly at random time.
     * 3. Don't let boxy leave the visible game area.
     * Also:
     * 4. When time to follow, make Boxy follow the player.
    */
    public void postStep(StepEvent e)
    {        
        
        // check boxy's position
        int boxyPositionX = Math.round(getPosition().x);
        int boxyPositionY = Math.round(getPosition().y);
        
        // get player position
        int targetPositionX = player.getPlayerPositionX();
        int targetPositionY = player.getPlayerPositionY();
        
        // get absolute distance between them
        int distanceX = Math.abs(targetPositionX - boxyPositionX);
        int distanceY = Math.abs(targetPositionY - boxyPositionY);

        // if distance is less than 100, then stop moving randomly, change appearance, start following player.
        if ((distanceX < 100) && (distanceY < 100))
        {
            setMove(false);
            setFollow(true);

            setImage(new BodyImage("boxy_follow.png", new Vec2(0, -1), 1)); 
        }
        
        
        // if time to move, move Boxy randomly and make him drop bombs.
        if (getMove() == true)
        {
        
            moveTime = moveTime - e.getStep();
    
            // if time is less than 0
            if (moveTime < 0.0f) {
                
                // make new random generators
                Random randomizerX = new Random();        
                Random randomizerY = new Random();
                Random randomizerTime = new Random();
            
                // produce random ints for coordinates
                int randomX = randomizerX.nextInt();
                int randomY = randomizerY.nextInt();
                
                // keep them within range -39 to 39
                randomX = randomX % 40;
                randomY = randomY % 40;
                
                // set boxy's velocity
                setLinearVelocity(new Vec2(randomX, randomY));
                
                // produce random int for move time span with range 0-5
                int randomTimeInt = randomizerTime.nextInt(6);
                
                // produce random float for move time within range 0-1 and add random int to it
                float randomTimeFloat = randomizerTime.nextFloat() + randomTimeInt;
                
                // set created time
                moveTime = randomTimeFloat;
                
                // create new bomb and place it (to drop it)
                Bomb bomb = new Bomb(game);
                bomb.placeBomb(boxyPositionX, boxyPositionY);
                
            }
            
            
            // don't let it leave the visible game part by setting velocity to opposite direction.
            if (boxyPositionX > 400)
            {
                setLinearVelocity(new Vec2(-80, 0));
            }
            else
            {
                if (boxyPositionX < -400)
                {
                    setLinearVelocity(new Vec2(80, 0));
                }
                else
                {
                    if (boxyPositionY > 200)
                    {
                        setLinearVelocity(new Vec2(0, -80));
                    }
                    else
                    {
                        if (boxyPositionY < -200)
                        {
                            setLinearVelocity(new Vec2(0, 80));
                        }
                    }
                }
            }
        }
        
        // if time to follow, let Boxy follow the player.
        if (getFollow() == true)
        {
            
            if (level.doesEnemyExist() == true)
            {                
                // constantly store each pair of coords in array
                playerHistory.add(new Vec2(targetPositionX, targetPositionY));
                
                // decrease boxy move time with each animation step
                moveTimeF = moveTimeF - e.getStep();
        
                // if move time less than 0, release the devil
                if (moveTimeF < 0.0f) {
                    
                    // make boxy visable
                    setVisible(true);
                    
                    // do the loop for checking where boxy should go according to player's past recorded steps
                    while (check == true)
                    {        
                        // getting players past step from array in order
                        Vec2 boxyStep = playerHistory.get(index);
                        
                        // rounding them to ints
                        int boxyStepX = Math.round(boxyStep.x);
                        int boxyStepY = Math.round(boxyStep.y);                      
                        
                        // set new position of the boxy
                        setPosition(new Vec2(boxyStepX, boxyStepY));
                        
                        // 1 step finished - make to go out of loop
                        check = false;
                        
                        // increase index
                        index++;
                       
                    }
                    
                }
                
            }

        }
    }
    
    public void preStep(StepEvent e) { 
        
        // set check value to true to boxy can proceed with chasing
        check = true; 
    
    }

}