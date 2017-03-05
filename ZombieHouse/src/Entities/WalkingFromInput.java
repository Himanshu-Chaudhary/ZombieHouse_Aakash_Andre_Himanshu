package Entities;
/**
 * Created by hssak on 3/4/2017.
 */
public class WalkingFromInput implements WalkingBehavior
{
  @Override
  public void walk()
  {
    System.out.println("I am walking from the input given by user.");
  }
}
