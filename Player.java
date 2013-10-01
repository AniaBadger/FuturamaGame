import city.soi.platform.*;
import org.jbox2d.common.Vec2;


/** Basic player in a game. */
public class Player extends Actor implements StepListener
{    
    /** Player's strength. */
    private float strength;
    
    /** Player's jump speed. */
    private int jumpSpeed;
    
    /** Player's walk speed. */
    private int walkSpeed;
    
    /** The number of bottles the player currently has. */
    private int bottles;  
    
    /** The number of lives the player currently has. */
    private int lives; 
    
    /** Players's picture. */
    private String playerImg;
    
    /** The game. */
    private Game game;
    
    /** Players's position coordinates. */
    private int playerPositionX;
    private int playerPositionY;
    
    /**
     * Initialise a new player.
     */
    public Player(Game game)
    {
        // create player's object
        super(game.getWorld(), new PolygonShape(-8.5f,38.0f, 2.5f,38.0f, 18.5f,2.0f, 18.5f,-39.0f, -18.5f,-39.0f, -18.5f,17.0f));
        
        // set the game
        this.game = game;
        
        // set default strength
        strength = 10.0f;
        
        // set lifes
        lives = 6;
        
        // set default jump speed
        jumpSpeed = 200;
        
        // set default walk speed
        walkSpeed = 90;  
        
        // set start bottle count
        bottles = 0;      
        
        // set player's default image
        playerImg = "bender.png";
        setImage(new BodyImage(playerImg, new Vec2(0, 0), 1));
        
        // add step listener
        getWorld().addStepListener(this);
    }       
    
    /** Increase the sthenght count. */
    public void incrementPlayerStrength()
    {
        strength = strength + 20.0f;
    }
    
    /** Decrease the sthenght count. */
    public void decrementPlayerStrength()
    {
        strength--;
    }
    
    /** The strength the player currently posseses. */
    public float getStrength()
    {
        return strength;
    }
    
    /** The strength the player currently posseses. */
    public void setStrength(float s)
    {
        strength = s;
    }
    
    /** Increase the lives count. */
    public void incrementLivesCount()
    {
        lives++;
    }
    
    /** Decrease the lives count. */
    public void decrementLivesCount()
    {
        lives--;
    }
    
    /** The number of lives the player currently has. */
    public int getLives()
    {
        return lives;
    }  
    
    public void setLives(int newLives)
    {
        lives = newLives;
    }  
    
    /** The jump speed the player currently posseses. */
    public float getJumpSpeed()
    {
        return jumpSpeed;
    }
    
    /** Change jump speed of player. */
    public void setJumpSpeed(int js)
    {
        jumpSpeed = js;
    }
    
    /** The walk speed the player currently posseses. */
    public float getWalkSpeed()
    {
        return walkSpeed;
    }
    
    /** Change walk speed of player. */
    public void setWalkSpeed(int ws)
    {
        walkSpeed = ws;
    }
    
    /** Increase the bottles count. */
    public void incrementBottleCount()
    {
        bottles++;
    }
    
    /** Decrease the bottles count. */
    private void decrementBottleCount()
    {
        bottles--;
    }
    
    /** The number of bottles the player currently has. */
    public int getBottles()
    {
        return bottles;
    }   
    
    /** Set new bottles count. */
    public void setBottles(int newCount)
    {
        bottles = newCount;
    }
    
    /** Get player's X position. */
    public int getPlayerPositionX()
    {
        return playerPositionX;
    }
    
    /** Get player's Y position. */
    public int getPlayerPositionY()
    {
        return playerPositionY;
    }
    
    /** Using StepListener: 
     * 1. Decreasing player's strength constantly while the game is running.
     * 2. Checking player's position and rounding it to int. 
     * If y less than -350, then life lost.
     * 3. Then changing the player's image, 
     * jump speed and strength status' image, 
     * depending on player's strength. 
    */    
    public void postStep(StepEvent e)
    {
        // get position in float
        float floatPositionX = getPosition().x;
        float floatPositionY = getPosition().y;
        
        // round it to int
        playerPositionX = Math.round((floatPositionX));
        playerPositionY = Math.round((floatPositionY));
        
        //decrease player's strength constantly while the game is running.
        strength = strength - e.getStep();
        
        //check player's position. If y less than -350, then loose life, get player at start of level, set it's velocity to 0.
        if (floatPositionY < -350.0) {
            decrementLivesCount();
            GameLevel level = game.getLevel();       
            level.putPlayerAtStart();
            setLinearVelocity(new Vec2(0, 0));
        }
        
        // if strength < 0 then change player's image, jump speed and strength status' image
        if (strength < 0.0f) {
            setImage(new BodyImage("bender_weak.png", new Vec2(0, 0), 1));
            setJumpSpeed(100);
        }
        else
        {
            // if strength > 20 then change player's image, jump speed and strength status' image
            if (strength > 20.0f) {
                setImage(new BodyImage("bender_strong.png", new Vec2(0, 0), 1));
                setJumpSpeed(300);
            }
            else
            {
                // if strength is > 0 and < 20 then change all back to default
                setImage(new BodyImage(playerImg, new Vec2(0, 0), 1));
                setJumpSpeed(200);
            }
        }
        
        // if strength goes below -10 then then loose life, get player at start of level, set it's velocity to 0, set strength to 10.
        if (strength < -10.0f) {
            decrementLivesCount();
            GameLevel level = game.getLevel();       
            level.putPlayerAtStart();
            strength = 10.0f;
        }

    }
    
    public void preStep(StepEvent e) {}
    
}
