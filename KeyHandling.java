import city.soi.platform.*;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.Array;

import javax.swing.JFrame;

import java.util.*;

/**
 * Class to separately handle key handling used in the Game.
 */
public class KeyHandling
{
    
    /** Needed vars from game class. */
    private Game game;   
    private Game g; 
    private World world;   
    private Player player;
    private Boolean isOver;
    private GameLevel level;
    private JFrame frame;
    
    /** Bottle that Player throws. */
    private FlyingBottle flyingBottle;
    
    /** A debug display. */
    private DebugViewer debugViewer;

    /**
     * Initialize class KeyHandling
     * @param game the game
     */
    public KeyHandling(Game game)
    {
        
        // set vars got from game class
        world = game.getWorld();      
        player = game.getPlayer();
        isOver = game.isOver();
        level = game.getLevel();
        frame = game.getFrame();
        
        // set game var for internal classes
        g = game;
        
        // add some keyboard handling
        frame.addKeyListener(new java.awt.event.KeyAdapter() {
            
            /** Handle key press events for walking and jumping. */
            public void keyPressed(java.awt.event.KeyEvent e)
            {
                if (isOver) return;
                int code = e.getKeyCode();
                // SPACE = jump
                if (code == java.awt.event.KeyEvent.VK_SPACE) {
                    // only jump if player is not already jumping
                    if (!player.isJumping()) {
                        player.jump(player.getJumpSpeed());
                    }
                // LEFT ARROW = walk left
                } else if (code == java.awt.event.KeyEvent.VK_LEFT) {
                    player.walkLeft(player.getWalkSpeed());
                // RIGHT ARROW = walk right
                } else if (code == java.awt.event.KeyEvent.VK_RIGHT) {
                    player.walkRight(player.getWalkSpeed());
                // N KEY = throw bottle to the right   
                } else if (code == java.awt.event.KeyEvent.VK_N) {
                    // do it only if on level 3
                    if (g.getLevelNumber() == 3) {
                        flyingBottle = new FlyingBottle(g);
                        flyingBottle.placeBottle();
                        flyingBottle.throwBottleRight();
                    }  
                // shortcut for level 1
                } else if (code == java.awt.event.KeyEvent.VK_1) {
                    g.setCurrentLevelPosition(-1);
                    g.goToNextLevel();
                // shortcut for level 2    
                } else if (code == java.awt.event.KeyEvent.VK_2) {
                    g.setCurrentLevelPosition(0);
                    g.goToNextLevel();
                // shortcut for level 3   
                } else if (code == java.awt.event.KeyEvent.VK_3) {
                    g.setCurrentLevelPosition(1);
                    g.goToNextLevel();
                    
                // F1 key toggles display of debug view
                } else if (code == java.awt.event.KeyEvent.VK_F1) {
                    if (debugViewer == null) debugViewer = new DebugViewer(new DebugSettings(world));
                    if (debugViewer.isRunning()) {
                        debugViewer.stopViewer();
                    } else {
                        debugViewer.startViewer();
                    }
                // M KEY = throw bottle to the left
                } else if (code == java.awt.event.KeyEvent.VK_M) {
                    // do it only if on level 3
                    if (g.getLevelNumber() == 3) {
                        flyingBottle = new FlyingBottle(g);
                        flyingBottle.placeBottle();
                        flyingBottle.throwBottleLeft();
                    } 
                }
            }
            
            /** Handle key release events (stop walking). */
            public void keyReleased(java.awt.event.KeyEvent e)
            {
                if (isOver) return;
                int code = e.getKeyCode();
                if (code == java.awt.event.KeyEvent.VK_LEFT) {
                    player.stopWalking();
                } else if (code == java.awt.event.KeyEvent.VK_RIGHT) {
                    player.stopWalking();
                }
            }
        });
        
    }
    
}
