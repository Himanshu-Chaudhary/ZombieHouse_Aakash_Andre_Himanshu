package Entities;

/**
 * Created by Aakash on 3/4/2017.
 */
public class Player extends Creature
{
  public double stamina = 0;

  public Player()
  {
    setSpeed(3);
    setDamage(25);
    setHealth(100);
    setStepDistance(5);
    setStamina(100);
    walkingBehavior = new WalkingFromInput();
    fightingBehavior = new FightWithWeapons();
  }
  @Override
  public void update()
  {

  }

  @Override
  public void display()
  {

  }

  @Override
  public void setSpeed(double sp)
  {
   speed = sp;
  }

  @Override
  public void setDamage(double dm)
  {
    damage = dm;

  }

  @Override
  public void setStepDistance(double sDis)
  {
    stepDistance = sDis;

  }

  @Override
  public void setHealth(double heal)
  {
    health = heal;
  }


  public void setStamina(double st){
    stamina = st;
  }
  public void setPosition(double x , double y, double z){
    positionX = x;
    positionY = y;
    positionZ = z;
  }
}
