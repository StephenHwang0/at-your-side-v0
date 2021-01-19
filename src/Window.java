import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.event.*;
/**
 * Window class that creates the game's JFrame window and adds a listener. <br>
 * Time spent: 5 hours <br>
 * Updates: 2.0 - now calls the intro method instead of the play method in Game <br>
 *          3.0 - will now not detect key presses <br>
 *          4.0 - no notable updates
 * 
 * <h2>Course Info:</h2>
 * ICS4U0 with Ms. Krasteva
 * 
 * @author Stephen Hwang and Juleen Chen
 * @version 4.0 - 10/06/2019
 * 
 * <pre>
 * Global Variable Library
 * Name      Type        Description
 * game      Game        Instance of the Game class used to run the game
 * </pre>
 */
public class Window
{
  Game game;
  
  /**
   * Class constructor that creates and sets up the JFrame <br>
   * Updates: 2.0 - now calls the intro method instead of the play method in Game
   * 
   * @param width the width of the window
   * @param height the height of the window
   * @param title the title of the JFrame
   * @param newGame instance of the Game class that will be run
   */
  public Window (int width, int height, String title, Game newGame)
  {
    game = newGame;
    game.setFocusable (true);
    game.setPreferredSize (new Dimension (width, height));
    game.setMaximumSize (new Dimension (width, height));
    game.setMinimumSize (new Dimension (width, height));
    game.addKeyListener (game);
    JFrame frame = new JFrame (title);
    frame.add (game);
    frame.pack ();
    frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo (null);
    frame.setResizable (false);
    frame.setVisible (true);
    game.start ();
  }
}