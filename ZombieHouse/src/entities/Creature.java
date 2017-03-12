package entities;
/**
 * Created by Aakash on 3/2/2017.
 */
public abstract class Creature extends Entity
{
  public double speed = 0.00; //speed of zombie or Player
  public double health = 0.00; // health of zombie or player
  public double damage = 0.00;
  public double stepDistance = 0.00; //step distance of zombie or player.

  public abstract void update(double time);
  public abstract void display();
  public abstract void setSpeed(double sp);
  public abstract void setDamage(double dm);
  public abstract void setStepDistance(double sDis);
  public abstract void setHealth(double heal);

  // both having same behavior
  public void footStepSound(){
    System.out.println("Playing footstep.");
  }

}
