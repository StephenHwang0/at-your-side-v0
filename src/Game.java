import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.lang.*;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import javax.sound.sampled.*;
/**
 * Game class that runs the main game and acts as the program's driver class. <br>
 * Time spent: 48 hours <br>
 * Updates: 2.0 - added an initiation sequence, unfinished menu, and basic graphics <br>
 *          3.0 - added movement animation, interaction, and a ticking timer to update the game <br>
 *          4.0 - completed game by adding events, rooms, levels, and screens <br>
 * 
 * <h2>Course Info:</h2>
 * ICS4U0 with Ms. Krasteva
 * 
 * @author Stephen Hwang and Juleen Chen
 * @version 4.0 - 10/06/2019
 * 
 * <pre>
 * Global Variable Library
 * Name         Type                      Description
 * r                int                   Player's row value on the map
 * c                int                   Player's column value on the map
 * xPos             int                   X Position of the background
 * yPos             int                   Y Position of the background
 * facing           int                   Direction the player is currently facing (1 for north, 2 for east, 3 for south, 4 for west)
 * level            int                   The current game level
 * phase            int                   The current phase that the game is in (0 for menu, 1 for game, 2 for level select, 3 for scores screen, 4 for about, 5 for recording scores)
 * stage            int                   The current stage the game is on (phase 1) or the current menu option (other phases)
 * key              int                   The current key pressed
 * count            int                   The current number of animation frames completed
 * whichCharacter   int                   The index of the character currently being interacted with
 * whichRoom        int                   The current room's number
 * scores           int[]                 The player's scores for the 3 levels
 * map              int[][]               Current level's map represented by a 2D array
 * pause            boolean               Checks if the game should allow the player to move or not
 * text             boolean               Checks if the game should be displaying text or not
 * intro            boolean               Checks if the intro of the level is playing
 * ending           boolean               Checks if the ending of the level is playing
 * lastStage        boolean               Checks if Mara should have appeared
 * single           boolean               Checks if the player is playing a single level or the whole game
 * currentText      String                The current line of text being displayed in the game (phase 1) or the current line of text typed (phase 5)
 * characters       ArrayList&lt;Character&gt;  List of all the characters in the current room
 * rooms            ArrayList&lt;Room&gt;       List of all the rooms in the current level
 * background       BufferedImage         Background image file
 * pro              BufferedImage         Protagonist image file
 * arrows           BufferedImage         Direction keys image file
 * t                Timer                 Instance of timer that updates the screen continuously during the game
 * clip1            Clip                  Menu music clip
 * clip2            Clip                  Level music clip
 * clip3            Clip                  Ending music clip
 * </pre>
 */
public class Game extends JPanel implements ActionListener, KeyListener
{
  private int r, c, xPos, yPos, facing, level, phase, stage, key, count, whichCharacter, whichRoom;
  private int[] scores = new int[3];
  private int[][] map;
  private boolean pause, text, intro, lastStage, ending, single;
  private String currentText;
  private ArrayList<Character> characters = new ArrayList<Character> ();
  private ArrayList<Room> rooms = new ArrayList<Room> ();
  private BufferedImage background, pro, arrows;
  private Timer t = new Timer (1, this);
  private Clip clip1, clip2, clip3;
  
  /**
   * Void method that initiates the game with an intro screen and displays the menu <br>
   * Updates: 2.0 - created intro method <br>
   *          4.0 - now initiates music
   * 
   * <pre>
   * Local Variable Library
   * Name      Type               Description
   * audioIn   AudioInputStream   Instance of AudioInputStream to initiate music
   * </pre>
   */
  public void start ()
  {
    pause = true;
    phase = 0;
    try //opening sequence
    {
      background = ImageIO.read (new File ("graphics/intro7.png"));
      repaint ();
      Thread.sleep (1000);
      for (int x = 7; x >= 1; x--)
      {
        background = ImageIO.read (new File ("graphics/intro" + x + ".png"));
        repaint ();
        Thread.sleep (10);
      }
      Thread.sleep (3500);
      for (int x = 1; x <= 7; x++)
      {
        background = ImageIO.read (new File ("graphics/intro" + x + ".png"));
        repaint ();
        Thread.sleep (10);
      }
      Thread.sleep (1000);
    }
    catch (Exception e) {}
    try //loading music
    {
      AudioInputStream audioIn = AudioSystem.getAudioInputStream (new File ("music/menu.wav"));
      clip1 = AudioSystem.getClip ();
      clip1.open (audioIn);
      audioIn = AudioSystem.getAudioInputStream (new File ("music/level.wav"));
      clip2 = AudioSystem.getClip ();
      clip2.open (audioIn);
      audioIn = AudioSystem.getAudioInputStream (new File ("music/ending.wav"));
      clip3 = AudioSystem.getClip ();
      clip3.open (audioIn);
    }
    catch (Exception e) {}
    pause = false;
    stage = 1;
    playMusic (1);
  }
  
  /**
   * Overriden method that detects keys that are pressed and allows the program to respond accordingly <br>
   * Updates 3.0 - created method <br>
   *         4.0 - now controls and regulates all 6 different phases
   * 
   * @param e KeyEvent that occurs
   */
  @Override
  public void keyPressed (KeyEvent e)
  {
    if (phase != 1)
      key = e.getKeyCode ();
    else if (!pause) //used for tile snapping
    {
      key = e.getKeyCode ();
      pause = true;
    }
    if (key == KeyEvent.VK_ESCAPE) //resets game to the menu
    {
      t.stop ();
      playMusic (1);
      phase = 0;
      stage = 1;
      repaint ();
    }
    else if (phase == 0) //menu
    {
      if (key == KeyEvent.VK_LEFT && stage != 1)
        stage--;
      else if (key == KeyEvent.VK_RIGHT && stage != 3)
        stage++;
      else if (key == KeyEvent.VK_SPACE && stage == 1)
      {
        phase = 2;
        stage = 1;
      }
      else if (key == KeyEvent.VK_SPACE && stage == 2)
      {
        phase = 3;
        stage = 1;
      }
      else if (key == KeyEvent.VK_SPACE && stage == 3)
      {
        phase = 4;
        stage = 1;
      }
      repaint ();
    }
    else if (phase == 1 && key == KeyEvent.VK_SPACE) //game
    {
      if (facing == 1 && map[r - 1][c] == 3 || facing == 2 && map[r][c + 1] == 3 || facing == 3 && map[r + 1][c] == 3 || facing == 4 && map[r][c - 1] == 3 || text && pause)
      {
        t.stop ();
        interact ();
      }
      else if (intro)
        interact ();
    }
    else if (phase == 2) //level select
    {
      if (key == KeyEvent.VK_UP && stage != 1)
        stage--;
      else if (key == KeyEvent.VK_DOWN && stage != 4)
        stage++;
      else if (key == KeyEvent.VK_SPACE && stage == 1)
      {
        single = false;
        play (1);
      }
      else if (key == KeyEvent.VK_SPACE)
      {
        single = true;
        play (stage - 1);
      }
      else if (key == KeyEvent.VK_DELETE)
      {
        phase = 0;
        stage = 1;
      }
      repaint ();
    }
    else if (phase == 3) //high scores screen
    {
      if (key == KeyEvent.VK_SPACE)
      {
        phase = 0;
        stage = 2;
      }
      else if (key == KeyEvent.VK_DELETE)
        updateScores (null, true);
      repaint ();
    }
    else if (phase == 4) //about screen
    {
      if (key == KeyEvent.VK_LEFT && stage != 1)
        stage--;
      else if (key == KeyEvent.VK_RIGHT && stage != 3)
        stage++;
      else if (key == KeyEvent.VK_SPACE)
      {
        phase = 0;
        stage = 3;
      }
      repaint ();
    }
    else if (phase == 5) //entering name screen
    {
      if ((key >= 65 && key <= 90 || key == KeyEvent.VK_SPACE) && currentText.length () <= 12)
      {
        currentText = currentText + (char) key;
      }
      else if (key == KeyEvent.VK_BACK_SPACE && currentText.length () > 0)
        currentText = currentText.substring (0, currentText.length () - 1);
      else if (key == KeyEvent.VK_ENTER)
      {
        updateScores (currentText, false);
        currentText = "";
        phase = 0;
        stage = 1;
        playMusic (1);
      }
      repaint ();
    }
  }
  
  /**
   * Overriden method that detects keys that are released and allows the program to respond accordingly <br>
   * Updates 3.0 - created method
   * 
   * @param e KeyEvent that occurs
   */
  @Override
  public void keyReleased (KeyEvent e)
  {
    if (count == 0)
    {
      key = 0;
      pause = false;
    }
  }
  
  /**
   * Overriden method that must be overriden because of implementation <br>
   * Updates 3.0 - created method
   * 
   * @param e KeyEvent that occurs
   */
  @Override
  public void keyTyped (KeyEvent e) {}
  
  /**
   * Void method that initiates the level, setting up all the values <br>
   * Updates: 2.0 - changed the file name <br>
   *          3.0 - added the intro sequence <br>
   *          4.0 - added stages and unique level set up
   * 
   * @param level the level that is to be loaded
   */
  public void play (int level)
  {
    this.level = level;
    pause = true;
    phase = 1;
    stage = - 1;
    rooms = Map.initializeLevel (level);
    if (level == 1) //levels have unique setups
    {
      facing = 1;
      whichRoom = 0;
    }
    else if (level == 2)
    {
      facing = 1;
      whichRoom = 6;
    }
    else if (level == 3)
    {
      whichRoom = 0;
      facing = 2;
    }
    characters = rooms.get (whichRoom).getCharacters ();
    map = rooms.get (whichRoom).getMap ();
    r = rooms.get (0).getStartingR ();
    c = rooms.get (0).getStartingC ();
    xPos = rooms.get (0).getStartingX ();
    yPos = rooms.get (0).getStartingY ();
    background = rooms.get (whichRoom).getBackground ();
    changeStage ();
    intro = true;
    pause = true;
    ending = false;
    lastStage = false;
    try
    {
      arrows = ImageIO.read (new File ("graphics/arrows.png"));
    }
    catch (Exception e) {}
    playMusic (2);
    repaint ();
    interact ();
  }
  
  /**
   * Overriden method that updates (includes animation) the game every time the timer ticks during phase 1 <br>
   * Updates 3.0 - created method <br>
   *         4.0 - added changing rooms
   * 
   * @param ae ActionEvent that occurs
   */
  @Override
  public void actionPerformed (ActionEvent ae)
  {
    if (key == KeyEvent.VK_UP)
    {
      facing = 1;
      if (count != 0)
      {
        yPos += 18;
        count += 18;
      }
      else if (count == 0 && r - 1 >= 0 && map[r - 1][c] != 0 && map[r - 1][c] != 3)
      {
        r--;
        yPos += 18;
        count += 18;
        if (map[r][c] < 0 || map[r][c] == 2)
          changeRoom ();
      }
    }
    else if (key == KeyEvent.VK_RIGHT)
    {
      facing = 2;
      if (count != 0)
      {
        xPos -= 18;
        count += 18;
      }
      else if (count == 0 && c + 1 < map[0].length && map[r][c + 1] != 0 && map[r][c + 1] != 3)
      {
        c++;
        xPos -= 18;
        count += 18;
        if (map[r][c] < 0 || map[r][c] == 2)
          changeRoom ();
      }
    }
    else if (key == KeyEvent.VK_DOWN)
    {
      facing = 3;
      if (count != 0)
      {
        yPos -= 18;
        count += 18;
      }
      else if (count == 0 && r + 1 >= 0 && map[r + 1][c] != 0 && map[r + 1][c] != 3)
      {
        r++;
        yPos -= 18;
        count += 18;
        if (map[r][c] < 0 || map[r][c] == 2)
          changeRoom ();
      }
    }
    else if (key == KeyEvent.VK_LEFT)
    {
      facing = 4;
      if (count != 0)
      {
        xPos += 18;
        count += 18;
      }
      else if (count == 0 && c - 1 < map[0].length && map[r][c - 1] != 0 && map[r][c - 1] != 3)
      {
        c--;
        xPos += 18;
        count += 18;
        if (map[r][c] < 0 || map[r][c] == 2)
          changeRoom ();
      }
    }
    if (count == 36)
    {
      key = 0;
      count = 0;
      pause = false;
    }
    repaint ();
  }
  
  /**
   * Void method that changes the room in game <br>
   * Updates 4.0 - created method
   */
  public void changeRoom ()
  {
    t.stop ();
    pause = true;
    try
    {
      Thread.sleep (100);
    }
    catch (Exception e) {}
    count = 0;
    if (map[r][c] == 2) //back to main map
    {
      r = rooms.get (whichRoom).getExitR ();
      c = rooms.get (whichRoom).getExitC ();
      xPos = rooms.get (whichRoom).getExitX ();
      yPos = rooms.get (whichRoom).getExitY ();
      background = rooms.get (0).getBackground ();
      characters = rooms.get (0).getCharacters ();
      map = rooms.get (0).getMap ();
      whichRoom = 0;
    }
    else //go to mini map
    {
      whichRoom = - map[r][c];
      characters = rooms.get (whichRoom).getCharacters ();
      r = rooms.get (whichRoom).getStartingR ();
      c = rooms.get (whichRoom).getStartingC ();
      xPos = rooms.get (whichRoom).getStartingX ();
      yPos = rooms.get (whichRoom).getStartingY ();
      background = rooms.get (whichRoom).getBackground ();
      map = rooms.get (whichRoom).getMap ();
    }
    repaint ();
    pause = false;
    t.start ();
  }
  
  /**
   * Void method that changes the in game stage <br>
   * Updates 4.0 - created method
   */
  public void changeStage ()
  {
    stage++;
    for (int x = 0; x < rooms.size (); x++) //updates all ImportantCharacter objects
    {
      for (int y = 0; y < rooms.get (x).getCharacters ().size (); y++)
      {
        if (rooms.get (x).getCharacters ().get (y) instanceof ImportantCharacter)
        {
          rooms.get (x).getCharacters ().get (y).update (stage);
        }
      }
    }
    if (rooms.get (0).getCharacters ().get (rooms.get (0).getCharacters ().size () - 1).checkStage (stage)) //checks if it's the last stage
    {
      if (level == 3) //level 3's last stage is unique
      {
        for (int x = rooms.get (0).getCharacters ().size () - 1; x >= 0; x--)
        {
          if (rooms.get (0).getCharacters ().get (x).getName ().equals ("mara2") || rooms.get (0).getCharacters ().get (x).getName ().equals ("villager11"))
          {
            rooms.get (0).setMara (rooms.get (0).getCharacters ().get (x).getR (), rooms.get (0).getCharacters ().get (x).getC ());
            rooms.get (0).getCharacters (). remove (x);
          }
        }
      }
      rooms.get (0).setMara (rooms.get (0).getCharacters ().get (rooms.get (0).getCharacters ().size () - 1).getR (), rooms.get (0).getCharacters ().get (rooms.get (0).getCharacters ().size () - 1).getC ());
      lastStage = true;
      if (level != 2) //level 2's last stage is unique as well
        map = rooms.get (0).getMap ();
    }
  }
  
  /**
   * Void method that allows the player to speak with an in-game character, viewing all of its dialogue lines <br>
   * Updates: 2.0 - added the text variable, and called repaint() to paint the text in game <br>
   *          3.0 - added intro text and animation cancelling <br>
   *          4.0 - added endings, level switches, and phase switching
   */
  public void interact ()
  {
    if (intro && !text) //checks if displaying intro
    {
      if (level == 2)
        whichCharacter = characters.size () - 1;
      else
        whichCharacter = characters.size () - 3;
    }
    else if (ending && !text) //checks if displaying ending
    {
      xPos = 0;
      yPos = 0;
      try
      {
        background = ImageIO.read (new File ("graphics/black.png"));
        repaint ();
      }
      catch (Exception e) {}
      whichCharacter = characters.size () - 2;
    }
    else if (!text) //makes the character face the player
    {
      if (facing == 1)
      {
        for (int x = 0; x < characters.size (); x++)
        {
          if (characters.get (x).getR () == r - 1 && characters.get (x).getC () == c)
          {
            characters.get (x).setFacing (3);
            whichCharacter = x;
            break;
          }
        }
      }
      else if (facing == 2)
      {
        for (int x = 0; x < characters.size (); x++)
        {
          if (characters.get (x).getR () == r && characters.get (x).getC () == c + 1)
          {
            characters.get (x).setFacing (4);
            whichCharacter = x;
            break;
          }
        }
      }
      else if (facing == 3)
      {
        for (int x = 0; x < characters.size (); x++)
        {
          if (characters.get (x).getR () == r + 1 && characters.get (x).getC () == c)
          {
            characters.get (x).setFacing (1);
            whichCharacter = x;
            break;
          }
        }
      }
      else if (facing == 4)
      {
        for (int x = 0; x < characters.size (); x++)
        {
          if (characters.get (x).getR () == r && characters.get (x).getC () == c - 1)
          {
            characters.get (x).setFacing (2);
            whichCharacter = x;
            break;
          }
        }
      }
      if (!characters.get (whichCharacter).getTalkedTo ())
        scores[level - 1]++;
    }
    pause = true;
    if (!text && characters.get(whichCharacter) instanceof Mara)
      playMusic (3);
    currentText = characters.get (whichCharacter).getDialogue (characters.get (whichCharacter).getCount ());
    if (!currentText.equals ("end")) //prints dialogue
    {
      text = true;
      repaint ();
    }
    if (characters.get (whichCharacter).getCount () == characters.get (whichCharacter).getTotal ()) //checks if the end of dialogue is reached
    {
      if (characters.get (whichCharacter).checkStage (stage) && !lastStage) //checks if stage needs to be updated
        changeStage ();
      characters.get (whichCharacter).reset ();
      pause = false;
      text = false;
      intro = false;
      if (characters.get(whichCharacter) instanceof Mara) //checks if last character has been talked to, thus ending the level
      {
        ending = true;
        interact ();
      }
      else if (!ending) //restarts timer and resumes game
        t.start ();
      else if (ending && level != 3 && !single) //continues to next level
        play (level + 1);
      else if (ending && (level == 3 || single)) //goes to enter name screen
      {
        currentText = "";
        phase = 5;
        stage = 1;
        pause = false;
        repaint ();
      }
    }
  }
  
  /**
   * Return method that that returns a list of scores from the high score file <br>
   * Updates: 4.0 - created method
   * 
   * <pre>
   * Local Variable Library
   * Name        Type               Description
   * topScores   ArrayList&lt;Score&gt;   ArrayList of scores from the score file
   * reader      BufferedReader     Instance of BufferedReader to read from a file
   * temp        String             Temporary String variable
   * </pre>
   * 
   * @return ArrayList of scores from the score file
   */
  public ArrayList<Score> getScores ()
  {
    ArrayList<Score> topScores = new ArrayList<Score> ();
    try
    {
      BufferedReader reader = new BufferedReader (new FileReader ("scores/scores.txt"));
      while (true)
      {
        String temp = reader.readLine ();
        if (temp == null)
          break;
        else
          topScores.add (new Score (Integer.parseInt (reader.readLine ()), Integer.parseInt (reader.readLine ()), Integer.parseInt (reader.readLine ()), temp));
      }
    }
    catch (Exception e) {}
    return topScores;
  }
  
  /**
   * Void method that updates the high score file <br>
   * Updates: 4.0 - created method
   * 
   * <pre>
   * Local Variable Library
   * Name        Type               Description
   * topScores   ArrayList&lt;Score&gt;   ArrayList of scores from the score file
   * reader      BufferedReader     Instance of BufferedReader to read from a file
   * writer      PrintWriter        Instance of PrintWriter to write to a file
   * temp        String             Temporary String variable
   * temp        Score              Temporary instance of Score used in selection sort
   * maximum     int                Temporary integer variable used in selection sort
   * </pre>
   * 
   * @param name player's name
   * @param delete whether or not the scores are being deleted
   */
  public void updateScores (String name, boolean delete)
  {
    ArrayList<Score> topScores = new ArrayList<Score> ();
    if (delete) //deletes scores
    {
      try
      {
        PrintWriter writer = new PrintWriter (new FileWriter ("scores/scores.txt"));
        writer.print ("");
        writer.close ();
      }
      catch (Exception ex) {}
    }
    else //rewrites scores
    {
      topScores.add (new Score (scores[0], scores[1], scores[2], name));
      try //read scores from a file
      {
        BufferedReader reader = new BufferedReader (new FileReader ("scores/scores.txt"));
        while (true)
        {
          String temp = reader.readLine ();
          if (temp == null)
            break;
          else
            topScores.add (new Score (Integer.parseInt (reader.readLine ()), Integer.parseInt (reader.readLine ()), Integer.parseInt (reader.readLine ()), temp));
        }
      }
      catch (Exception e) {}
      for (int x = 0; x < topScores.size (); x++) //selection sort to sort scores
      {
        int maximum = x; 
        for (int y = x + 1; y < topScores.size (); y++)
        {
          if (topScores.get (y).getTotal () > topScores.get (maximum).getTotal ())
            maximum = y;
        }
        Score temp = topScores.get (maximum);
        topScores.set (maximum, topScores.get (x));
        topScores.set (x, temp);
      }
      try //rewrite scores file
      {
        PrintWriter writer = new PrintWriter (new FileWriter ("scores/scores.txt"));
        for (int x = 0; x < 10; x++)
        {
          if (x >= topScores.size ())
            break;
          writer.println (topScores.get (x).getName ());
          writer.println (topScores.get (x).getScore1 ());
          writer.println (topScores.get (x).getScore2 ());
          writer.println (topScores.get (x).getScore3 ());
        }
        writer.close ();
      }
      catch (Exception e) {}
      scores = new int[3]; //resets player scores
    }
  }
  
  /**
   * Overriden method that allows graphics to be painted, both during the menu and in game <br>
   * Updates: 2.0 - added paint method <br>
   *          3.0 - modified to fit with animation timer <br>
   *          4.0 - draws every phase now
   * 
   * @param g instance of the Graphics class that allows things to be drawn
   */
  @Override
  public void paint (Graphics g)
  {
    if (phase == 0) //menu
    {
      try
      {
        background = ImageIO.read (new File ("graphics/menu" + stage + ".png"));
      }
      catch (Exception e) {}
      g.drawImage (background, 0, 0, null);
    }
    else if (phase == 1) //game
    {
      g.drawImage (background, xPos, yPos, null);
      try
      {
        pro = ImageIO.read (new File ("graphics/pro" + facing + ".png"));
      }
      catch (Exception e) {}
      if (whichRoom == 0 && !ending) //drawing characters
      {
        if (level == 1) //difference in art between level 1 and the others
        {
          for (int x = 0; x < characters.size () - 3; x++)
            g.drawImage (characters.get (x).getImage (), 900 + 36 * characters.get (x).getC () + xPos, 900 + 36 * characters.get (x).getR () + yPos, null);
          if (lastStage)
            g.drawImage (characters.get (characters.size () - 1).getImage (), 900 + 36 * characters.get (characters.size () - 1).getC () + xPos, 900 + 36 * characters.get (characters.size () - 1).getR () + yPos, null);
        }
        else
        {
          for (int x = 0; x < characters.size () - 3; x++)
            g.drawImage (characters.get (x).getImage (), 900 + 36 * characters.get (x).getC () + xPos, 897 + 36 * characters.get (x).getR () + yPos, null);
          if (lastStage)
            g.drawImage (characters.get (characters.size () - 1).getImage (), 900 + 36 * characters.get (characters.size () - 1).getC () + xPos, 897 + 36 * characters.get (characters.size () - 1).getR () + yPos, null);
        }
      }
      else
      {
        for (int x = 0; x < characters.size (); x++)
          g.drawImage (characters.get (x).getImage (), 900 + 36 * characters.get (x).getC () + xPos, 900 + 36 * characters.get (x).getR () + yPos, null);
      }
      g.drawImage (pro, 432, 252, null);
      g.drawImage (arrows, 800, 10, null);
      if (text) //drawing in-game text
      {
        g.setColor (Color.WHITE);
        g.fillRoundRect (32, 390, 840, 150, 50, 50);
        g.setColor (Color.BLACK);
        g.setFont (new Font ("Courier", Font.BOLD, 27));
        if (currentText.indexOf ("/") == - 1)
          g.drawString (currentText, 60, 435);
        else if (currentText.indexOf ("/") == currentText.lastIndexOf ("/"))
        {
          g.drawString (currentText.substring (0, currentText.indexOf ("/")), 60, 435);
          g.drawString (currentText.substring (currentText.indexOf ("/") + 1, currentText.length ()), 60, 470);
        }
        else
        {
          g.drawString (currentText.substring (0, currentText.indexOf ("/")), 60, 435);
          g.drawString (currentText.substring (currentText.indexOf ("/") + 1, currentText.lastIndexOf ("/")), 60, 470);
          g.drawString (currentText.substring (currentText.lastIndexOf ("/") + 1, currentText.length ()), 60, 505);
        }
      }
    }
    else if (phase == 2) //level select
    {
      try
      {
        background = ImageIO.read (new File ("graphics/select" + stage + ".png"));
      }
      catch (Exception e) {}
      g.drawImage (background, 0, 0, null);
    }
    else if (phase == 3) //high scores screen
    {
      try
      {
        background = ImageIO.read (new File ("graphics/scores.png"));
      }
      catch (Exception e) {}
      g.drawImage (background, 0, 0, null);
      g.setColor (Color.BLACK);
      g.setFont (new Font ("Courier", Font.BOLD, 27));
      for (int x = 0; x < getScores ().size (); x++) //read and draw all scores
      {
        g.drawString (getScores ().get (x).getName (), 95, 220 + x * 30);
        g.drawString (getScores ().get (x).getStringScore1 (), 366, 220 + x * 30);
        g.drawString (getScores ().get (x).getStringScore2 (), 465, 220 + x * 30);
        g.drawString (getScores ().get (x).getStringScore3 (), 564, 220 + x * 30);
        g.drawString (getScores ().get (x).getStringTotal (), 743, 220 + x * 30);
      }
    }
    else if (phase == 4) //about screen
    {
      try
      {
        background = ImageIO.read (new File ("graphics/about" + stage + ".png"));
      }
      catch (Exception e) {}
      g.drawImage (background, 0, 0, null);
    }
    else if (phase == 5) //enter name screen
    {
      try
      {
        background = ImageIO.read (new File ("graphics/record.png"));
      }
      catch (Exception e) {}
      g.drawImage (background, 0, 0, null);
      g.setColor (Color.BLACK);
      g.setFont (new Font ("Courier", Font.BOLD, 45));
      g.drawString (currentText, 75, 310);
    }
    repaint ();
  }
  
  /**
   * Void method that plays background music <br>
   * Updates: 4.0 - created method
   * 
   * @param track the track to be played
   */
  public void playMusic (int track)
  {
    try
    {
      if (track == 1)
      {
        if (clip2.isRunning ())
          clip2.stop ();
        if (clip3.isRunning ())
          clip3.stop ();
        clip1.setFramePosition (0);
        clip1.loop (Clip.LOOP_CONTINUOUSLY);
      }
      else if (track == 2)
      {
        if (clip1.isRunning ())
          clip1.stop ();
        if (clip3.isRunning ())
          clip3.stop ();
        clip2.setFramePosition (0);
        clip2.loop (Clip.LOOP_CONTINUOUSLY);
      }
      else if (track == 3)
      {
        if (clip1.isRunning ())
          clip1.stop ();
        if (clip2.isRunning ())
          clip2.stop ();
        clip3.setFramePosition (0);
        clip3.loop (Clip.LOOP_CONTINUOUSLY);
      }
    }
    catch (Exception e) {}
  }
  
  /* 
   * Main method that creates an instance of Window to run the game <br>
   * Updates: 2.0 - changed game title to "At Your Side" because there is only one deuteragonist
   * 
   * @param args[] parameter required for all main methods to run
   */
  public static void main (String args[])
  {
    new Window (900, 540, "At Your Side", new Game ());
  }
}