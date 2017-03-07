package Entities;

import Input.InputHandler;
import general.PathNode;
import general.Test_Pathfinding_and_Map;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

/**
 * Created by Aakash on 3/4/2017.
 */
public class Player extends Creature
{
  public double stamina = 0;
  PathNode last_good_node = null;

  public Player()
  {
    setSpeed(3);
    setDamage(25);
    setHealth(100);
    setStepDistance(5);
    setStamina(100);
    walkingBehavior = new WalkingFromInput();
    fightingBehavior = new FightWithWeapons();
    this.mesh = new Box(10,10,10); // Add some basic mesh for the player
  }
  @Override
  public void update()
  {
    walk(Test_Pathfinding_and_Map.board ); // Update the player position based on input.
    display(); // Update the player's mesh.
  }

  public void walk( PathNode board[][] )
  {
    double x_component = 0;
    double z_component = 0;

    if( InputHandler.isKeyDown(KeyCode.SHIFT) && this.stamina > 0)
    {
      this.setSpeed(3);
      this.stamina -= 5;
    }
    else
    {
      if(this.stamina < 100) this.stamina += 1;
      this.setSpeed(1);
    }

    if( InputHandler.isKeyDown(KeyCode.A))
    {
      x_component += Math.sin(Math.toRadians(this.direction+90));
      z_component += Math.cos(Math.toRadians(this.direction+90));
    }
    if( InputHandler.isKeyDown(KeyCode.D))
    {
      x_component += Math.sin(Math.toRadians(this.direction-90));
      z_component += Math.cos(Math.toRadians(this.direction-90));
    }
    if( InputHandler.isKeyDown(KeyCode.W))
    {
      x_component += Math.sin(Math.toRadians(this.direction));
      z_component += Math.cos(Math.toRadians(this.direction));
    }
    if( InputHandler.isKeyDown(KeyCode.S))
    {
      x_component -= Math.sin(Math.toRadians(this.direction));
      z_component -= Math.cos(Math.toRadians(this.direction));
    }
    double magnitude = Math.sqrt(x_component*x_component+z_component*z_component);
    if(magnitude != 0)
    {
      // for any nearby walls, if the player is too close, subtract the vector component of the vector from wall to
      // player from the player -> player vector.
      x_component = x_component / magnitude * this.speed;
      z_component = z_component / magnitude * this.speed;

      double w2p_x;
      double w2p_z;

      // I am dumbly checking all walls for simplicity,
      // we can make this much faster if needed.
      for(int x = 0; x < 50; x++)
      {
        for(int y = 0; y < 50; y++)
        {
          // Collide properly with nulled out blocks/square walls.
          // to do: make the pillars/obstacles have slightly smaller hitboxes
          // or maybe we can change the center of mass to 5,5 instead of 0,0.
          if(board[x][y] == null)
          {
            w2p_x = x*10 - this.positionX;
            w2p_z = y*10 - this.positionZ;

            if(Math.abs(w2p_x) < 10 && Math.abs(w2p_z) < 10)
            {
              if (w2p_x > 0 && x_component > 0 && Math.abs(w2p_z) < 9)
              {
                x_component = 0;
              } else if (w2p_x < 0 && x_component < 0 && Math.abs(w2p_z) < 9)
              {
                x_component = 0;
              } else if (w2p_z > 0 && z_component > 0 && Math.abs(w2p_x) < 9)
              {
                z_component = 0;
              } else if (w2p_z < 0 && z_component < 0 && Math.abs(w2p_x) < 9)
              {
                z_component = 0;
              }

            }
          }

        }
      }

      this.setPosition( this.positionX+x_component, this.positionY, this.positionZ+z_component );
    }
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


  public void setStamina(double st){
    stamina = st;
  }
  public void setPosition(double x , double y, double z){
    positionX = x;
    positionY = y;
    positionZ = z;
  }
}
