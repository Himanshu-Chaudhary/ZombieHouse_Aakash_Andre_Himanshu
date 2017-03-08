package Entities;

import general.PathNode;
import general.Test_Pathfinding_and_Map;
import general.Test_Texture_and_Game;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

/**
 * Created by Aakash on 3/4/2017.
 */
public class Zombie extends Creature
{

  public Player player;

  public Zombie()
  {
    setSpeed(3);
    setDamage(5);
    setHealth(100);
    setStepDistance(5);
    walkingBehavior = new WalkingFromAI();
    fightingBehavior = new FightEatingBrains();
    this.mesh = new Box(10,10,10);
  }
  @Override
  public void update()
  {
    walk( Test_Texture_and_Game.board );
    display();
  }

  public void walk( PathNode[][] board )
  {
    this.setPosition(positionX-Math.cos(Math.toRadians(this.direction))*0.8,positionY,positionZ-Math.sin(Math.toRadians(this.direction))*0.8);
  }

  @Override
  public void display()
  {
    // Update the position & direction of mesh to match entity.
    this.mesh.setTranslateX(this.positionX);
    this.mesh.setTranslateY(this.positionY);
    this.mesh.setTranslateZ(this.positionZ);
    this.mesh.setRotationAxis(Rotate.Y_AXIS);
    this.mesh.setRotate( this.direction );
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
