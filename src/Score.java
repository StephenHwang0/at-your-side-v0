/**
 * Score class that contains the attributes of a player's score <br>
 * Time spent: 2 hours <br>
 * Updates: 4.0 - class created
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
 * score1    int         The player's score for the first level
 * score2    int         The player's score for the second level
 * score3    int         The player's score for the third level
 * total     int         The player's total score over all levels
 * name      String      The player's name
 * </pre>
 */
public class Score
{
  private int score1, score2, score3, total;
  private String name;
  
  /**
   * Class constructor that initiates this object's attributes
   * 
   * @param score1 player's score for the first level
   * @param score2 player's score for the second level
   * @param score3 player's score for the third level
   * @param name player's name
   */
  public Score (int score1, int score2, int score3, String name)
  {
    this.score1 = score1;
    this.score2 = score2;
    this.score3 = score3;
    this.name = name;
    total = score1 + score2 + score3;
  }
  
  /**
   * Return method that returns the player's score for the first level
   * 
   * @return player's score for the first level
   */
  public int getScore1 ()
  {
    return score1;
  }
  
  /**
   * Return method that returns the player's score for the second level
   * 
   * @return player's score for the second level
   */
  public int getScore2 ()
  {
    return score2;
  }
  
  /**
   * Return method that returns the player's score for the third level
   * 
   * @return player's score for the third level
   */
  public int getScore3 ()
  {
    return score3;
  }
  
  /**
   * Return method that returns the player's total score
   * 
   * @return player's total score
   */
  public int getTotal ()
  {
    return total;
  }
  
  /**
   * Return method that returns the player's score for the first level as a two digit string
   * 
   * @return player's score for the first level as a two digit string
   */
  public String getStringScore1 ()
  {
    if (score1 >= 10)
      return "" + score1;
    else
      return "0" + score1;
  }
  
  /**
   * Return method that returns the player's score for the second level as a two digit string
   * 
   * @return player's score for the second level as a two digit string
   */
  public String getStringScore2 ()
  {
    if (score2 >= 10)
      return "" + score2;
    else
      return "0" + score2;
  }
  
  /**
   * Return method that returns the player's score for the third level as a two digit string
   * 
   * @return player's score for the third level as a two digit string
   */
  public String getStringScore3 ()
  {
    if (score3 >= 10)
      return "" + score3;
    else
      return "0" + score3;
  }
  
  /**
   * Return method that returns the player's total score as a two digit string
   * 
   * @return player's total score as a two digit string
   */
  public String getStringTotal ()
  {
    if (total >= 10)
      return "" + total;
    else
      return "0" + total;
  }
  
  /**
   * Return method that returns the player's name
   * 
   * @return player's name
   */
  public String getName ()
  {
    return name;
  }
}