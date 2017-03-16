package entities;

import general.GameMain;
import input.InputHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity
{
  public List<Double> past_position_x = new ArrayList<>();
  public List<Double> past_position_z = new ArrayList<>();
  public List<Double> past_direction = new ArrayList<>();
  public List<String> past_states = new ArrayList<>();

  public Player( int x, int y, int z )
  {
    super.name = "PLAYER";
    super.state = "IDLE";
    super.priority = 0;

    super.state_timer = System.currentTimeMillis();
    super.frame_timer = super.state_timer;
    super.update_timer = super.frame_timer;

    super.health = 100;
    super.damage = 10;
    super.speed = 1.00;

    super.direction = 0;
    super.position_x = x;
    super.position_y = y;
    super.position_z = z;

    super.material = new PhongMaterial( Color.MAGENTA );
    super.meshview = new MeshView();
    super.meshview.setMesh( null );
  }

  @Override public void update( double time )
  {
    // Set up information for the past player.
    this.past_position_x.add(super.position_x);
    this.past_position_z.add(super.position_z);
    this.past_direction.add(this.direction);
    this.past_states.add(this.state);

    // And now begin to update the player.
    double dt_ms = time - super.update_timer;
    super.state_timer += dt_ms;
    String state_backup = super.state;

    instigateIdle();
    instigateRun();
    instigateAttack();

    if(!state_backup.equals( super.state ))
    {
      super.frame = 0;
      super.frame_timer = 0;
      super.state_timer = 0;
    }

    if( super.state.equals("IDLE")){ /* Do nothing. */ }
    if( super.state.equals("RUN")){ run( dt_ms ); }
    if( super.state.equals("ATTACK")){ attack( dt_ms ); }

    double d = Math.sqrt(Math.pow(super.position_x-GameMain.player.position_x,2)+Math.pow(super.position_z-GameMain.player.position_z,2));
    d = d>100? 0 : 1-d/100;
    this.material.setDiffuseColor( Color.color(d,d,d));
    super.display( dt_ms );

    super.update_timer = time;
  }

  private void run(double dt )
  {
    double x_component = 0;
    double z_component = 0;

    if( InputHandler.isKeyDown(KeyCode.W))
    {
      x_component -= Math.sin(Math.toRadians(this.direction));
      z_component -= Math.cos(Math.toRadians(this.direction));
    }
    if( InputHandler.isKeyDown(KeyCode.A))
    {
      x_component += Math.sin(Math.toRadians(this.direction-90));
      z_component += Math.cos(Math.toRadians(this.direction-90));
    }
    if( InputHandler.isKeyDown(KeyCode.S))
    {
      x_component += Math.sin(Math.toRadians(this.direction));
      z_component += Math.cos(Math.toRadians(this.direction));
    }
    if( InputHandler.isKeyDown(KeyCode.D))
    {
      x_component += Math.sin(Math.toRadians(this.direction+90));
      z_component += Math.cos(Math.toRadians(this.direction+90));
    }

    double magnitude = Math.sqrt( x_component*x_component + z_component*z_component );
    if( magnitude != 0)
    {
      x_component = x_component / magnitude * speed;
      z_component = z_component / magnitude * speed;

      // Slide off walls.
      double w2p_x;
      double w2p_z;

      // I am dumbly checking all walls for simplicity,
      // we can make this much faster if needed.
      for(int x = 0; x < GameMain.board_size-1; x++)
      {
        for(int y = 0; y < GameMain.board_size-1; y++)
        {
          if(GameMain.map[x][y].isWall)
          {
            w2p_x = x*10 - super.position_x;
            w2p_z = y*10 - super.position_z;

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
      super.position_x += x_component;
      super.position_z += z_component;
    }
  }
  private void attack(double dt)
  {
    if( super.state_timer > 400 )
    {
      double distance;
      for( Zombie z : GameMain.zombies )
      {
        distance = Math.sqrt( Math.pow(z.position_x-super.position_x,2)+Math.pow(z.position_z-super.position_z,2));
        if( distance < 20 )
        {
          z.health -= 1;
        }
      }
    }
    if( super.state_timer > 600) // 600 ms for attack to complete.
    {
      super.proposeState("IDLE", 0, 2);
    }
  }

  private void instigateAttack() {
    if(InputHandler.isKeyDown(KeyCode.SPACE))
    {
      super.proposeState("ATTACK", 1, 1);
    }
  }
  private void instigateIdle()
  {
    super.proposeState("IDLE", 0, 1);
  }
  private void instigateRun()
  {
    if (    InputHandler.isKeyDown(KeyCode.W) ||
            InputHandler.isKeyDown(KeyCode.A) ||
            InputHandler.isKeyDown(KeyCode.S) ||
            InputHandler.isKeyDown(KeyCode.D))
    {
      super.proposeState("RUN", 0, 1);
    }
  }

}
