import org.jbox2d.common.Vec2;
import city.soi.platform.*;

import java.util.ArrayList;

/**
 * Enemy in a game. Since devil is created, it follows player all the time. When the player collides with a devil, the
 * world view starts to shake for 1 second. 
 */
public class Devil extends Actor implements CollisionListener , StepListener
{   
    /** The game in which the player is playing. */
    private Game game;    
    
    /** The Player. */
    private Player player;
    
    /** The level. */
    private GameLevel level;
    
    /** The world view. */
    private WorldView worldView;
    
    /** move time. */
    private float moveTime;
    
    /** shake time. */
    private float shakeTime;
    
    /** zoom clock. */
    private float zoomCount;
    
    /** Array recording player's path. */
    private ArrayList<Vec2> playerHistory;
    
    /** index to iterate through player history array. */
    private int index;
    
    /** check for devil if to go out of tracing step. */
    private boolean check;
    
    /** shake the camera?. */
    private boolean shakeCamera;
    
    /** Flag zoom in or zoom out. */
    private boolean zoomFlag;
    
    /**
    * Initialise a new Devil.
    * @param game The game.
    */
    public Devil(Game game)
    {
        // create devil object
        super(game.getWorld(), new PolygonShape(-40.0f,43.5f, 33.0f,37.5f, 33.0f,-41.5f, -40.0f,-41.5f));

        // set game
        this.game = game;
        
        // set image for devil
        setImage(new BodyImage("devil.png", new Vec2(0, -1), 1));   
        
        // make invisible
        setVisible(false);
        
        // set player
        player = game.getPlayer();      
        
        // set level
        level = game.getLevel();
        
        // set world view
        worldView = game.getView();
        
        // add Collision Listener
        getWorld().addCollisionListener(this);
        
        // add Step Listener
        getWorld().addStepListener(this);
        
        // set default move time
        moveTime = 1.0f;
        
        // set default shake time
        shakeTime = 0.0f;
        
        // set default zoom count
        zoomCount = 0.0f;
        
        // set default zoom flag
        zoomFlag = false;
        
        // make new player history array
        playerHistory = new ArrayList<Vec2>();
        
        // set beginning index
        index = 0;
        
        // set default check state
        check = true;
        
        // set default shakeCamera state
        shakeCamera = false;
    }    
    
    
    /** Using Collision Listener:
     * When devil collides with player
     * shakeCamera is set to true
     * shake time to 1.0
     * zoom counter to 0.1
     * zoom flag to zoom out
    */
    public void collide(CollisionEvent e)
    {
        if (e.getOtherBody() == player)
        {
            shakeCamera = true;
            shakeTime = 1.0f;
            zoomCount = 0.1f;
            zoomFlag = true;
        }
    }
    
    /** Using Step Listener:
     * 1. Checking if devil exists to avoid run time errors
     * 2. Recording players walking/moving path into playerHistory array.
     * 3. Releasing the devil to chase them with 3 seconds delay.
    */
    public void postStep(StepEvent e)
    {        
        
        if (level.doesEnemyExist() == true)
        {            
            // get player position
            int targetPositionX = player.getPlayerPositionX();
            int targetPositionY = player.getPlayerPositionY();
            
            // constantly store each pair of coords in array
            playerHistory.add(new Vec2(targetPositionX, targetPositionY));
            
            // decrease devil move time with each animation step
            moveTime = moveTime - e.getStep();
    
            // if move time less than 0, release the devil
            if (moveTime < 0.0f) {
                
                // make devil visable
                setVisible(true);
                
                // do the loop for checking where devil should go according to player's past recorded steps
                while (check == true)
                {        
                    // getting players past step from array in order
                    Vec2 devilStep = playerHistory.get(index);
                    
                    // rounding them to ints
                    int devilStepX = Math.round(devilStep.x);
                    int devilStepY = Math.round(devilStep.y);                      
                    
                    // set new position of the devil
                    setPosition(new Vec2(devilStepX, devilStepY));
                    
                    // 1 step finished - make to go out of loop
                    check = false;
                    
                    // increase index
                    index++;
                   
                }
                
            }
            
            // when time to shake the camera and time more than 0, shake the camera
            if (shakeCamera == true && shakeTime > 0.0f)
            {
                
                //decease time with each step
                shakeTime = shakeTime - e.getStep();
                
                //set new camera view center 
                zoomIn();
                
                //set the zoom to either in or out every 0.1
                if (zoomCount > 0.0f) 
                {
                    if (zoomFlag) 
                    {
                        zoomOut();
                    } 
                    else
                    {
                        zoomIn();
                    }
                    //decease time with each step
                    zoomCount = zoomCount - e.getStep();
                } 
                else 
                {
                    // reset zoom counter
                    zoomCount = 0.1f;
                    // flip the flag
                    if (zoomFlag) 
                    {
                        zoomFlag = false;
                    }
                    else
                    {
                        zoomFlag = true;
                    }
                }                                
                
                   // OH DEAR WHAT WAS I THINKING:
                
//                 // change it and the zoom with each 0.1 od step
//                 if (shakeTime < 0.9f && shakeTime > 0.8f)
//                 {
//                     zoomOut();
//                 }
//                 else
//                 {
//                     if (shakeTime < 0.8f && shakeTime > 0.7f)
//                     {
//                         zoomIn();
//                     }
//                     else
//                     {
//                         if (shakeTime < 0.7f && shakeTime > 0.6f)
//                         {
//                             zoomOut();
//                         }
//                         else
//                         {
//                             if (shakeTime < 0.6f && shakeTime > 0.5f)
//                             {
//                                 zoomIn();
//                             }
//                             else
//                             {
//                                 if (shakeTime < 0.5f && shakeTime > 0.4f)
//                                 {
//                                     zoomOut();
//                                 }
//                                 else
//                                 {
//                                     if (shakeTime < 0.4f && shakeTime > 0.3f)
//                                     {
//                                         zoomIn();
//                                     }
//                                     else
//                                     {
//                                         if (shakeTime < 0.3f && shakeTime > 0.2f)
//                                         {
//                                             zoomOut();
//                                         }
//                                         else
//                                         {
//                                             if (shakeTime < 0.2f && shakeTime > 0.1f)
//                                             {
//                                                 zoomIn();
//                                             }
//                                             else
//                                             {
//                                                 if (shakeTime < 0.1f && shakeTime > 0.0f)
//                                                 {
//                                                     zoomOut();
//                                                 }
//                                             }
//                                         }
//                                     }
//                                 }
//                             }
//                         }
//                     }
//                 }          
                
            }
            // set shake time to 0, make shaking false, set camera to normal state
            else
            {
                shakeTime = 0.0f;
                shakeCamera = false;
                zoomCount = 0.0f;
                zoomFlag = false;
                worldView.setCamera(new Vec2(0,0), 1.0f);
            }
        }
              

    }
    
    public void preStep(StepEvent e) {      
        
            // set check value to true to devil can proceed with chasing
            check = true;
            
    }
    
    private void zoomIn() {
        worldView.setCamera(new Vec2(5,5), 1.1f);
    }
            
    private void zoomOut() {
        worldView.setCamera(new Vec2(-5,-5), 0.9f);
    }

}
