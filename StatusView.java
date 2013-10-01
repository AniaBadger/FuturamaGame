import city.soi.platform.*;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.event.*;
import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.Window;

import java.lang.Math;

/**
 * Status view JPanel area to display numbers of lives, bottles and strength of player, and additional info, in real time.
 * Also contains 1 buttons - Pause/Resume.
 */
public class StatusView extends JPanel implements ChangeListener
{
   /** Labels to display. */
   private JLabel bottlesLabel;   
   private JLabel strengthLabel;   
   private JLabel livesLabel;
   
   /** Needed vars from game class. */
   private Game game;   
   private Game g; 
   private World world;   
   private Player player;
   private Boolean isOver;
   private GameLevel level;
   private JFrame frame;
   
   /** Buttons. */
   private JButton pauseButton;
    
   /**
   * Initialise a new Status View.
   * @param game The game.
   */
   public StatusView (Game game)
   {
      // call superclass
      super();
      
      // set game var for internal classes
      g = this.game;
      
      // dimension of JPanel
      setPreferredSize(new java.awt.Dimension(300, 75));
      
      // set vars got from game class
      world = game.getWorld();      
      player = game.getPlayer();
      isOver = game.isOver();
      level = game.getLevel();
      frame = game.getFrame();
      
      // make labels
      bottlesLabel = new JLabel();      
      strengthLabel = new JLabel();
      livesLabel = new JLabel();
      
      // make buttons
      pauseButton = new JButton("Pause");
      
      // make pause button to pause and repause the game
      pauseButton.addActionListener(new ActionListener() 
      {
          public void actionPerformed(ActionEvent e) {
              
              // when button has pause text, pause the world and change text to resume
              if (pauseButton.getText().equals("Pause") )
              {
                  world.pause();
                  pauseButton.setText("Resume");
              }
              // when button has resume text, unpause the world and change text to pause
              else
              {
                  world.unpause();
                  pauseButton.setText("Pause");
              }
            }
      });
      
      // make buttons not focusable
      pauseButton.setFocusable(false);
      
      // update labels
      updateLabels();
      
      // add all labels and buttons
      add(livesLabel);
      add(bottlesLabel);
      add(strengthLabel);
      add(pauseButton);
      
      // add change listener
      world.addChangeListener(this);
   }
   
   /** Update labels method - display all labels and buttons. */
   public void updateLabels() {
       livesLabel.setText("Lives: " + player.getLives() + "  |");
       bottlesLabel.setText("Bottles: " + player.getBottles() + "  |");
       strengthLabel.setText("Strength: " + Math.round(player.getStrength()) + "  |");
   }
   
   /** Update labels when a change occurs. */
   public void stateChanged(ChangeEvent e) {
       updateLabels();
   }

}
