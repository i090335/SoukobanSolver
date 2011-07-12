/**
 * Floor.java
 * SoukobanSolver
 *
 * Created by giginet on 2011/07/08
 * 
 */
package map;

/**
 * 床を表すクラスです
 * @author giginet
 */
public class Floor extends Chip{
  protected final boolean goal = false;
  protected final boolean through = true;
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString(){
    return ".";
  }
}