/**
 * Mara class that extends the Character class so that the character, Mara, only appears only at a specified stage <br>
 * Time spent: 2 hours <br>
 * Updates: 4.0 - class created <br>
 * 
 * <h2>Course Info:</h2>
 * ICS4U0 with Ms. Krasteva
 * 
 * @author Stephen Hwang and Juleen Chen
 * @version 4.0 - 10/06/2019
 * 
 * <pre>
 * Global Variable Library
 * Name      Type       Description
 * stage     int        stage that the character appears on
 * </pre>
 */
public class Mara extends Character
{
  private int stage;
  
  /**
   * Class constructor that calls the superclass constructor
   * 
   * @param r this object's row number
   * @param c this object's column number
   * @param name this object's name
   */
  public Mara (int r, int c, String name)
  {
    super (r, c, name);
  }
  
  /**
   * Sets the stage this object is supposed to appear at
   * 
   * @param stage the stage this object is supposed to appear at
   */
  public void setStage (int stage)
  {
    this.stage = stage;
  }
  
  /**
   * Checks if this object should appear
   * 
   * @param stage the current stage of the game
   * @return true if it should appear and false if not
   */
  public boolean checkStage (int stage)
  {
    return this.stage == stage;
  }
}