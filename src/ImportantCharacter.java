import java.util.ArrayList;
/**
 * ImportantCharacter class that extends the Character class so that it can say different lines based on the stage the player is in. <br>
 * Time spent: 5 hours <br>
 * Updates: 2.0 - class created <br>
 *          3.0 - no notable updates <br>
 *          4.0 - instances of this class now advance the storyline when talked to at the right moment <br>
 * 
 * <h2>Course Info:</h2>
 * ICS4U0 with Ms. Krasteva
 * 
 * @author Stephen Hwang and Juleen Chen
 * @version 4.0 - 10/06/2019
 * 
 * <pre>
 * Global Variable Library
 * Name             Type               Description
 * stages           int[]              Array that stores the indexes to start reading dialogue from at which stage
 * dialogueOptions  String[][]         2D array in which each row contains all the dialogue lines at a particular stage 
 * currentDialogue  ArrayList&lt;String&gt;  The dialogue lines for the current stage
 * </pre>
 */
public class ImportantCharacter extends Character
{
  private int[] stages;
  private String[][] dialogueOptions;
  private ArrayList<String> currentDialogue = new ArrayList<String> ();
  
  /**
   * Class constructor that initiates all the object's arrays, calls the superclass, and fills the stages array with negative values <br>
   * Updates 4.0 - modified to initiate arrays and take in the number of stages
   * 
   * @param r the ImportantCharacter object's row number
   * @param c the ImportantCharacter object's column number
   * @param numOfStages the number of stages for a level
   * @param name the ImportantCharacter object's name
   */
  public ImportantCharacter (int r, int c, int numOfStages, String name)
  {
    super (r, c, name);
    dialogueOptions = new String[numOfStages][20];
    stages = new int[numOfStages];
    for (int x = 0; x < stages.length; x++)
      stages[x] = - 1;
  }
  
  /**
   * Void method that adds a stage to array stages <br>
   * Updates 4.0 - method created
   * 
   * @param stage the current stage of the game
   */
  public void addStage (int stage)
  {
    for (int x = 0; x < stages.length; x++)
    {
      if (stages[x] == - 1)
      {
        stages[x] = stage;
        break;
      }
    }
  }
  
  /**
   * Void method that adds dialogue for a particular stage <br>
   * Updates 4.0 - method created
   * 
   * @param line the dialogue line being added
   * @param stage the current stage of the game
   */
  public void addDialogue (String line, int stage)
  {
    for (int x = 0; x < dialogueOptions[stage].length; x++)
    {
      if (dialogueOptions[stage][x] == null)
      {
        dialogueOptions[stage][x] = line;
        break;
      }
    }
  }
  
  /**
   * Overriden void method that updates the character dialogue to the current stage's dialogue <br>
   * Updates 4.0 - method created
   * 
   * @param stage the current stage of the game
   */
  @Override
  public void update (int stage)
  {
    currentDialogue = new ArrayList<String> ();
    for (int x = 0; x < dialogueOptions[stage].length; x++)
    {
      if (dialogueOptions[stage][x] == null)
        break;
      currentDialogue.add (dialogueOptions[stage][x]);
    }
    setTotal (currentDialogue.size ());
  }
  
  /**
   * Overriden return method that returns true if this character advances the storyline and false if not <br>
   * Updates 4.0 - method created
   * 
   * @param stage the current stage of the game
   * @return true if this character advances the storyline and false if not
   */
  @Override
  public boolean checkStage (int stage)
  {
    for (int x = 0; x < stages.length; x++)
    {
      if (stages[x] == stage)
        return true;
    }
    return false;
  }
  
  /**
   * Overriden return method that returns the next unviewed dialogue line <br>
   * Updates 3.0 - method created <br>
   *         4.0 - now returns the dialogue from the currentDialogue ArrayList
   * 
   * @param index which dialogue option is called
   * @return the next unviewed dialogue line for the current stage
   */
  @Override
  public String getDialogue (int index)
  {
    addCount ();
    return currentDialogue.get (index);
  }
}