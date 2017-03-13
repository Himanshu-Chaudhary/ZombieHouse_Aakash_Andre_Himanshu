package entities;

import input.InputHandler;
import general.GameMain;
import general.MeshManager;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.Rotate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static general.GameMain.board;

public class Player extends Creature
{
  // Attributes
  private double stamina = 0;
  private double stamina_max = 100;
  private double stamina_regen = 1;
  private double stamina_decay = 2;
  private double walking_speed = 0.8;
  private double running_speed = 2;
  private double health_max = 30;

  // Timing variables
  private int frame = 0;
  private double frame_timer = 0;
  private double last_update_time = 0;

  // State management
  private String state = "IDLE";
  private double state_timer = 0;
  private int priority = 0;

  // Animations & State->Animation map.
  private ArrayList<Node[]> current_animation;
  private Map<String,ArrayList<Node[]>> state_to_animation = new HashMap<>();

  public Player()
  {
    this.health = health_max;
    this.stamina = stamina_max;

    this.state_to_animation.put("WALK", MeshManager.getAnimation("ZombieHouse/src/Meshes/Zombie_Run/run", 12));
    this.state_to_animation.put("IDLE", MeshManager.getAnimation("ZombieHouse/src/Meshes/Zombie_Idle/idle", 1));
    this.state_to_animation.put("ATTACK", MeshManager.getAnimation("ZombieHouse/src/Meshes/Zombie_Attack/attack",10));
    for( ArrayList<Node[]> animation : this.state_to_animation.values()){ animationTransform(animation); }
    display(); // Call in constructor so that everything's read for our main in case we want to fiddle with stuff.
  }

  @Override public void update( double time )
  {
    double dt = (time - this.last_update_time)/Math.pow(10,3);
    this.state_timer += dt;
    this.frame_timer += dt;

    String temp = state;

    // Instigate all state transformations.
    instigateIdle();
    instigateWalk();
    instigateRun();
    instigateAttack();

    // If there was a net change in states, reset frame & timer.
    if(!state.equals(temp))
    {
      this.state_timer = 0;
      this.frame = 0;
    }

    // Actually perform the function dictated by our state.
    if(state.equals("IDLE")) idle();
    if(state.equals("WALK") || state.equals("RUN")) move();
    if(state.equals("ATTACK")) attack();

    display();
    last_update_time = time;

  }

  // STATE MANAGERS
  private void idle()
  {
    // Nothing happens here.
    // Animation is handled in display.
  }
  private void move()
  {
    if(state.equals("RUN")) speed = running_speed;
    if(state.equals("WALK")) speed = walking_speed;

    double x_component = 0;
    double z_component = 0;

    if( InputHandler.isKeyDown(KeyCode.W))
    {
      x_component += Math.sin(Math.toRadians(this.direction));
      z_component += Math.cos(Math.toRadians(this.direction));
    }
    if( InputHandler.isKeyDown(KeyCode.A))
    {
      x_component += Math.sin(Math.toRadians(this.direction+90));
      z_component += Math.cos(Math.toRadians(this.direction+90));
      this.direction += 3;
    }
    if( InputHandler.isKeyDown(KeyCode.S))
    {
      x_component -= Math.sin(Math.toRadians(this.direction));
      z_component -= Math.cos(Math.toRadians(this.direction));
    }
    if( InputHandler.isKeyDown(KeyCode.D))
    {
      x_component += Math.sin(Math.toRadians(this.direction-90));
      z_component += Math.cos(Math.toRadians(this.direction-90));
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
      for(int x = 0; x < GameMain.board_size; x++)
      {
        for(int y = 0; y < GameMain.board_size; y++)
        {
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
      this.positionX += x_component;
      this.positionZ += z_component;
    }
  }
  private void attack()
  {
    if( state_timer == 0) // Start of state.
    {

    }
    else if( state_timer > 0.5 ) // End of state.
    {
      proposeState("IDLE", 0, 2);
    }
    else if (state_timer > 0 && state_timer < 0.5 ) // Middle of state.
    {

    }
  }

  // STATE INSTIGATORS
  private void instigateIdle()
  {
    // The default state, so no condition.
    this.proposeState("IDLE",0,1);
  }
  private void instigateWalk()
  {
    if( InputHandler.isKeyDown(KeyCode.W) ||
        InputHandler.isKeyDown(KeyCode.A) ||
        InputHandler.isKeyDown(KeyCode.S) ||
        InputHandler.isKeyDown(KeyCode.D))
    {
      this.proposeState("WALK", 0,1);
    }
  }
  private void instigateRun()
  {
    if( state.equals("WALK") && InputHandler.isKeyDown(KeyCode.SHIFT) && stamina > 0 )
    {
      this.proposeState("RUN", 0, 1);
    }
  }
  private void instigateAttack()
  {
    if ( InputHandler.isKeyDown(KeyCode.SPACE))
    {
      this.proposeState("ATTACK", 1, 1);
    }
  }

  public void flipFrame()
  {
    if(frame_timer > 0.04)
    {
      frame = (frame + 1) % 100;
      frame_timer = 0;
    }
  }
  public void proposeState( String state_name, int new_priority, int override_priority)
  {
    if( state_name.equals(this.state)) return;
    if( override_priority > this.priority )
    {
      this.priority = new_priority;
      this.state = state_name;
    }
  }
  public void animationTransform( ArrayList<Node[]> a )
  {
    for(int i = 0; i < a.size(); i++) { meshTransform(a.get(i)); }
  }
  public void meshTransform ( Node[] m )
  {
    for(int i = 0;i< m.length;i++)
    {
      m[i].setScaleX(6);
      m[i].setScaleY(-6);
      m[i].setScaleZ(6);
    }
  }

  @Override public void display()
  {
    flipFrame(); // Update the current frame #.
    this.current_animation = this.state_to_animation.get(this.state);
    if(this.current_animation == null) { this.current_animation = this.state_to_animation.get("IDLE"); }
    this.mesh = this.current_animation.get(frame % this.current_animation.size());

    for(Node n : mesh)
    {
      n.setTranslateX(this.positionX);
      n.setTranslateY(this.positionY);
      n.setTranslateZ(this.positionZ);
      n.setRotationAxis(Rotate.Y_AXIS);
      n.setRotate(this.direction+180);
    }
  }

  // I don't really use these.
  @Override public void setSpeed(double sp)
  {
    speed = sp;
  }
  @Override public void setDamage(double dm)
  {
    damage = dm;
  }
  @Override public void setStepDistance(double sDis)
  {
    stepDistance = sDis;
  }
  @Override public void setHealth(double heal)
  {
    health = heal;
  }

}
