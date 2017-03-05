package Entities;
/**
 * Created by Aakash on 3/4/2017.
 */
public class Zombie extends Creature
{

  public Zombie()
  {
    setSpeed(3);
    setDamage(5);
    setHealth(100);
    setStepDistance(5);
    walkingBehavior = new WalkingFromAI();
    fightingBehavior = new FightEatingBrains();
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

  public void setPosition(double x , double y, double z){
    positionX = x;
    positionY = y;
    positionZ = z;
  }
}
