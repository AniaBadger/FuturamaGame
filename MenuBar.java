import city.soi.platform.*;

import javax.swing.event.*;
import javax.swing.*;

import java.util.HashMap;

import java.io.ObjectOutputStream;
import java.io.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Window;

import java.lang.Math;

/**
 * Menu Bar - JMenuBar area for user to be able to start new game or load existing game state.
 */
public class MenuBar extends JMenuBar
{
   
   /** Needed vars from game class. */
   private Game game;   
   private Game g; 
   private World world;   
   private Player player;
   private Boolean isOver;
   private GameLevel level;
   private JFrame frame;
    
   /**
   * Initialise a new Menu Bar.
   * @param game The game.
   */
   public MenuBar (Game game)
   {
      // call superclass
      super();
      
      // set game var for internal classes
      g = game;
      
      // set vars got from game class
      world = game.getWorld();      
      player = game.getPlayer();
      isOver = game.isOver();
      level = game.getLevel();
      frame = game.getFrame();
      
      // make menu bar top menu item
      JMenu gameMenu = new JMenu("Game");
      this.add(gameMenu);
      
      // make menu items
      JMenuItem newGameItem = new JMenuItem("New Game");
      gameMenu.add(newGameItem);
      
      JMenuItem loadGameStateItem = new JMenuItem("Load Latest Saved Game State");
      gameMenu.add(loadGameStateItem);
      
      // make new game action to close (dispose) the current game window and make new game
      newGameItem.addActionListener(new ActionListener() 
      {
          public void actionPerformed(ActionEvent e) {    
              
              frame.dispose();
              Game game = new Game();
              
            }
      });
      
      // make load game action to load saved game
      loadGameStateItem.addActionListener(new ActionListener() 
      {
          public void actionPerformed(ActionEvent e) {          
              
              g.loadGame();
              
            }
      });
      
      // make items not focusable
      newGameItem.setFocusable(false);
      loadGameStateItem.setFocusable(false);  
      
   }

}
