package Entities;
/**
 * Created by hssak on 3/4/2017.
 */
public class FightEatingBrains implements FightingBehavior
{
  @Override
  public void fight()
  {
    System.out.println("I am fighting by eating brains");
  }
}
