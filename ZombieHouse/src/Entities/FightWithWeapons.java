package Entities;
/**
 * Created by Aakash on 3/4/2017.
 */
public class FightWithWeapons implements FightingBehavior
{
  @Override
  public void fight()
  {
    System.out.println("I am fighting with weapon");
  }
}
