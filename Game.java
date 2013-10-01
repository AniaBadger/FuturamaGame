import city.soi.platform.*;

import java.awt.BorderLayout;

import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JTextField;

import java.io.*;

import org.jbox2d.common.Vec2;

/**
 * A basic platform game. The user controlls Bender. 
 * The aim is to collect as many bottles as possible, giving Bender the strength to move and jump.
 * When certain amount of bottles are collected within a level, player proceeds to next level.
 * When all levels are completed - the game is won.
 * The player should avoid falling off the bottom platforms as it results with it's life being lost.
 * When player looses all their lives - game over.
 * Level 3 gives Player ability to throw bottles at enemies and kill them.
 * 
 * User is asked for they name and based on that the file with most recent level and player info is saved, after completing each level.
 */
public class Game implements StepListener
{
    /** The player (a specialised Actor). */
    private Player player;
    
    /** Game over flag. */
    private boolean isOver;
    
    /** Is game won flag. */
    private boolean isWon;
    
    /** The World in which the game bodies move and interact.*/
    private World world;
    
    /** A graphical display of the world (a specialised JPanel). */
    private WorldView view;
    
    /** Array of game existing levels. */
    private GameLevel[] levelList;
    
    /** Level position in array. */
    private int currentLevelPosition;
    
    /** Level. */
    private GameLevel level;
    
    /** Status View. */
    private StatusView statusView;
    
    /** Menu Bar. */
    private MenuBar menuBar;
    
    /** A key handler. */
    private KeyHandling keyHandling;
    
    /** Body that carries a final message graphic. */
    private Body final_message; 
    
    /** Body that carries a moving graphic. */
    private Body moving_bg; 
    
    /** Body that carries a background graphic. */
    private Body bg;
    
    /** Game window. */
    private JFrame frame;
    
    /** Game variable for inner classes to access. */
    private Game g;
    
    /** Bottle that Player throws. */
    private FlyingBottle flyingBottle;
    
    /** User's name. */
    private String userName;
    
    
    /** Initialise a new Game. 
    */
    public Game() {
        
        // set default isOver state
        isOver = false;
        
        // set default isWon state
        isWon = false;
        
        // make the world
        world = new World();    
        
        // make a player
        player = new Player(this); 
        
        // make array of existing levels and put levels into it
        levelList = new GameLevel[3];        
        levelList[0] = new Level1(this);
        levelList[1] = new Level2(this);
        levelList[2] = new Level3(this);
        
        // level array starting position
        currentLevelPosition = 0;
        
        // make level1
        level = new Level1(this);
        
        // poulate level with bodies
        level.populate();
        
        // put player at start of new level
        level.putPlayerAtStart();
        
        // add step listener
        world.addStepListener(this);
        
        // make the final message body, set its default image and make it invisible
        final_message = new Body(world);
        final_message.setPosition(new Vec2(0, 0));
        final_message.setImage(new BodyImage("over.png", new Vec2(0, 0), 1));
        final_message.setRenderLayer(7);
        final_message.setGravityStrength(0);
        final_message.setVisible(false);
        
        // make the static body with background image
        bg = new Body(world);
        bg.setPosition(new Vec2(0, 0));
        bg.setImage(new BodyImage("space_bg.jpg", new Vec2(0, 15), 1));
        bg.setRenderLayer(-7);  
        
        // make the moving background   
        moving_bg = new Body(world);
        moving_bg.setPosition(new Vec2(0, 0));
        moving_bg.setImage(new BodyImage("skycrapers_bg.png", new Vec2(0, 0), 1));
        moving_bg.setRenderLayer(-6);
        moving_bg.setGravityStrength(0);              
        
        // make a view
        view = new WorldView(world, 1000, 600);
        
        //view.setDrawStats(true); // uncomment this line to show simulation stats in game display
        
        // display the view in a frame
        frame = new JFrame("Futurama Game");  
        
        // display the status view in a frame
        statusView = new StatusView(this); 
        
        // display menu bar in a frame
        menuBar = new MenuBar(this); 
        
        // add menu bar to the frame
        frame.setJMenuBar(menuBar);
        
        // add key handling
        keyHandling = new KeyHandling(this);
        
        // quit the application when the game window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // display the world in the window
        frame.add(view);
        
        // don't let the game window be resized
        frame.setResizable(false);        
        
        // size the game window to fit the world view
        frame.pack();
        
        // make the window visible
        frame.setVisible(true); 
        
        // get user's name to create saved game file
        obtainUserName();
        
        // display welcome dialog
        welcomeDialog();
        
        // add status view to the window
        frame.add(statusView, BorderLayout.SOUTH);
        
        // start the game!
        world.start();
        
        // save the game 1st level
        saveGame();        
        
    }
    
    /** Get user's name using input dialog. Keep displaying the dialog until user enters input. */
    public void obtainUserName()
    {
        // display input window
        userName = JOptionPane.showInputDialog(frame, "What is your name? (case sensitive)", "Enter your name:", JOptionPane.QUESTION_MESSAGE);
        
        // if closed or canceled, display window again
        if (userName == null)
        {
            obtainUserName();
        }
    }
    
    /** Get user name variable. */
    public String getUserName()
    {
        return userName;
    }
    
    /** Check if user with provided username already exists by checking if saved game file with that name exists and return true or false.
    */
    public boolean isExistingUser()
    {
        FileInputStream in = null;
        
        try {
            
            in = new FileInputStream(userName);
            
            if (in != null) {
                in.close();
                return true;
            }  
            else
            {
                return false;
            }
            
        } catch (Exception e) {
            
            return false;     
            
        }
    }
    
    
    /** Display dialog depending on user's file existence.
    * 1. If file exists, display welcome dialog with choice of loading game or not, and extract the answer.
    * 2. If file doesn't exist, display welcome dialog and start new game.
    */
    public void welcomeDialog()
    {
        boolean existingUser = isExistingUser();
        
        if (existingUser == false)
        {
            JOptionPane.showMessageDialog(frame, 
            "Welcome " + userName + "! Have a nice play!", 
            "Welcome Message", 
            JOptionPane.PLAIN_MESSAGE);
        }
        else
        {
            Object[] options = {"Yes, please!", "Start New Game"};
            int chosenOption = JOptionPane.showOptionDialog(frame, 
            "Welcome back " + userName + "! Would you like to continue the game?", 
            "Welcome Message", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, options, options[0]);
            
            loadOrNot(chosenOption);

        }
    }
    
    /** Method that gets the choice of user's decision to load game or not:
    * 1. Load the game - load game.
    * 2. Don't load - display confirmation message.
    * 3. Dialog closed - display dialog with message.
    * @param cho the choice user made
    */
    public void loadOrNot(int cho)
    {
        int chosenOption = cho;
        
        if (chosenOption == JOptionPane.NO_OPTION)
        {
            JOptionPane.showMessageDialog(frame, 
            "Alright, have a nice play!", 
            "Confirmation", 
            JOptionPane.PLAIN_MESSAGE);
        }
        else
        {
            if (chosenOption == JOptionPane.YES_OPTION)
            {
                loadGame();
            }
            else
            {
                JOptionPane.showMessageDialog(frame, 
                "Decisions, decisions... ha? Have a nice play!", 
                "Confirmation", 
                JOptionPane.PLAIN_MESSAGE);
            }
        }
    }
    
    /** Save the game level:
    * 1. Use user name to name the file.
    * 2. Gather player's strenght, level number and player's lives info and put it into HashMap.
    * 3. Save collection to file.
    * 4. If failed - catch the exeption and display message to user.
    */
    public void saveGame()
    {
        String userName = getUserName();
        
        float playerStrenghtF = player.getStrength();
        int playerStrenght = Math.round((playerStrenghtF));
       
        HashMap<String,Integer> gameMap = new HashMap<String,Integer>();
        
        gameMap.put("level", getLevelNumber());
        gameMap.put("lives", player.getLives());
        gameMap.put("strenght", playerStrenght);
        
        FileOutputStream out = null;
        ObjectOutputStream objectStream = null;
        try {
            
            out = new FileOutputStream(userName);
            objectStream = new ObjectOutputStream(out);
            objectStream.writeObject(gameMap);
            
            if (objectStream != null) {
                objectStream.flush();
                objectStream.close();
            }
            
            if (out != null) {
                out.flush();
                out.close();
            }
            
        } catch (IOException e) {
            
            JOptionPane.showMessageDialog(frame,
            "We are sorry, the level could not be saved.",
            "Save error",
            JOptionPane.ERROR_MESSAGE);
            
        }
        
    }
    
    /** Load saved game level:
    * 1. Use user name to find the file.
    * 2. Open file, reconstruct HashMap and get player's strenght, level number and player's lives info.
    * 3. Set loaded info.
    * 4. Go to the saved level.
    * 5. If failed - catch the exeption and display message to user.
    */
    public void loadGame()
    {
        String userName = getUserName();
        
        FileInputStream in = null;
        ObjectInputStream objectStream = null;
        try {
            
            in = new FileInputStream(userName);
            objectStream = new ObjectInputStream(in);
            HashMap<String,Integer> loadedMap = (HashMap<String,Integer>)objectStream.readObject();
            
            if (objectStream != null) {
                objectStream.close();
            }
            
            if (in != null) {
                in.close();
            }
            
            int level = loadedMap.get("level");
            int lives = loadedMap.get("lives");
            int strenght = loadedMap.get("strenght");
            
            level = level - 2;
            
            float newStrength;
            
            newStrength = strenght;
            
            player.setStrength(newStrength);            
            player.setLives(lives);
            
            setCurrentLevelPosition(level);
            goToNextLevel();
            
            
        } catch (Exception e) {
            
            JOptionPane.showMessageDialog(frame,
            "We are sorry, but the game could not be loaded.",
            "Load error",
            JOptionPane.ERROR_MESSAGE);
            
        }
    }
    
    
    /** Get frame */
    public JFrame getFrame()
    {
        return frame;
    }
    
    /** Is the game over? */
    public boolean isOver()
    {
        return isOver;
    }
    
    /** End the game. */
    public void gameOver()
    {
        // clear level of bodies
        level.clearLevel();
        // destroy player
        player.destroy();
        
        // if game is won
        if (isWon == true)
        {
            // set final message to you won!
            final_message.setImage(new BodyImage("won.png", new Vec2(0, 0), 1));
            
            // display the game over message
            final_message.setVisible(true);
        
            isOver = true;
            
            // stop moving bg
            moving_bg.setLinearVelocity(new Vec2(0, 0));
        }
        else
        {
            // display the game over message
            final_message.setVisible(true);
        
            isOver = true;
            
            // stop moving bg
            moving_bg.setLinearVelocity(new Vec2(0, 0));
        }
    }
    
    /** The world in which this game is played. */
    public World getWorld()
    {
        return world;
    }
    
    /** The world view. */
    public WorldView getView()
    {
        return view;
    }
    
    /** The player. */
    public Player getPlayer()
    {
        return player;
    }
    
    /** The Level. */
    public GameLevel getLevel()
    {
        return level;
    }
    
    /** Set Current Level Position 
    * @param clp new current level position number
    */
    public void setCurrentLevelPosition(int clp)
    {
        currentLevelPosition = clp;
    }
    
    /** Current level number */
    public int getLevelNumber()
    {
        return currentLevelPosition + 1;
    }
    
    /** Final Message. */
    public Body getFinalMessage()
    {
        return final_message;
    }
    
    /** Bg. */
    public Body getBg()
    {
        return bg;
    }
    
    /** Moving Bg. */
    public Body getMovingBg()
    {
        return moving_bg;
    }
    
    /** Thrown bottle. */
    public Body getFlyingBottle()
    {
        return flyingBottle;
    }
    
    /** Play a game. */
    public static void main(String[] args) {
        new Game();
    }
    
    /** Go the next level method. */
    public void goToNextLevel() {
        
        // increase array position by 1
        currentLevelPosition++;
        
        // if level exists
        if (currentLevelPosition < levelList.length)
        {            
            // clear level of bodies
            level.clearLevel();
            // create new level from array
            level = levelList[currentLevelPosition];    
            // populate level with bodies
            level.populate();            
            // put player at start
            level.putPlayerAtStart();
            // make player velocity 0 at start
            player.setLinearVelocity(new Vec2(0, 0));
            // reset bottles count
            player.setBottles(0);             
            // save game
            saveGame();
        }
        //if got through all levels
        else
        {
            // game is won
            isWon = true;
            // game is over
            gameOver();
        }
    }
    
    public void preStep(StepEvent e) {
        // check if level is completed. If yes, go to next level
        if (level.isCompleted() == true)
        {
            goToNextLevel();            
        }
        
        // if player has 0 lives, make game over
        if (player.getLives() < 1)
        {
            gameOver();
        }
    } 
    
    /** Using StepListener - making the moving background move while the player is in motion.
    * If player's horizontal velocity is less than 0, then the player moves left. Then making the bg move to the right.
    * If player's horizontal velocity is more than 0, then the player moves right. Then making the bg move to the left.
    * If player's horizontal velocity is equal to 0, then the player doesn't move. Then making the bg to stop.
    */    
    public void postStep(StepEvent e)
    {       
        // check player's horizontal velocity
        if (player.getLinearVelocity().x < 0.0) {
            //move bg to the right
            moving_bg.setLinearVelocity(new Vec2(20, 0));
        }
        else
        {
            // check player's horizontal velocity
            if (player.getLinearVelocity().x > 0.0) {
                //move bg to the left
                moving_bg.setLinearVelocity(new Vec2(-20, 0));
            }
            else
            {
                // check player's horizontal velocity
                if (player.getLinearVelocity().x == 0.0) {
                    // don't apply any velocity to the bg
                    moving_bg.setLinearVelocity(new Vec2(0, 0));
                }                
            }
        }
    }
        
}