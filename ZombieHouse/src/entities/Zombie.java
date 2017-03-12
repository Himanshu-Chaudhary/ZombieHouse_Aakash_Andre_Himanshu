package entities;

import pathfinding.PathNode;
import pathfinding.Pathfinding;
import input.InputHandler;
import general.*;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Zombie extends Creature
{

  PhongMaterial body_material;
  PhongMaterial healthbar_material;

  // Attributes everyone wants.
  private Player player;
  private PathNode[][] board;

  private int frame = 0;
  private double frame_timer = 0;

  // Attributes for ourselves.
  private int smellRange = 20;
  private int max_health = 30;
  private double default_speed = 1.0;
  public boolean randomWalk;

  // State & Priority
  private String state = "IDLE";
  private double state_timer = 0;
  private int priority = 0;

  // Animations, and the mapping for state to animation.
  private ArrayList<Node[]> current_animation;
  private Map<String,ArrayList<Node[]>> state_to_animation = new HashMap<>();
  public Box healthbar = new Box(3,3,3);

  private double last_update_time = 0;
  private double last_direction_update = 0;

  public Zombie ( int x, int y, int z, Player player, PathNode[][] board )
  {
    this.body_material = new PhongMaterial(Color.BLUE);
    this.healthbar_material = new PhongMaterial(Color.RED);

    this.positionX = x;
    this.positionY = y;
    this.positionZ = z;
    this.player = player;
    this.board = board;

    this.health = max_health;
    this.speed = default_speed;

    // Import our meshes, and transform them as appropriate for this type of object.
    this.state_to_animation.put("WALKING", MeshManager.getAnimation("ZombieHouse/src/Meshes/Zombie_Run/run", 12));
    this.state_to_animation.put("IDLE", MeshManager.getAnimation("ZombieHouse/src/Meshes/Zombie_Idle/idle", 1));
    this.state_to_animation.put("ATTACKING", MeshManager.getAnimation("ZombieHouse/src/Meshes/Zombie_Attack/attack",10));
    for( ArrayList<Node[]> animation : this.state_to_animation.values()){ animationTransform(animation); }


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

  public void proposeState( String state_name, int new_priority, int override_priority)
  {
    if( state_name.equals(this.state)) return;
    if( override_priority > this.priority )
    {
      this.priority = new_priority;
      this.state = state_name;
      this.state_timer = 0;
      this.frame = 0;
    }
  }

  @Override
  public void update(double time)
  {
    double dt = (time-this.last_update_time)/Math.pow(10,3);
    this.state_timer += dt;

    // Move along the frames at a solid pace.
    frame_timer += dt;
    if(frame_timer > 0.04)
    {
      frame = (frame + 1) % 12;
      frame_timer = 0;
    }

    // If we've waited long enough for an update, then update the direction.
    if( (time-this.last_direction_update)/Math.pow(10,3) > 1 && (this.state.equals("IDLE") || this.state.equals("WALKING")) )
    {
      this.last_direction_update = time;
      this.proposeState("WALKING", 0, 1);
      if ( this.randomWalk ) this.direction = Math.random()*360;
      if ( !this.randomWalk && this.speed == 0) this.direction = Math.random()*360;
      this.setSpeed(default_speed);
      this.updateDirection();
    }
    this.walk(); // Move in the direction given, but collide with walls.
    this.attack(); // Always call attack - it can occur in-between decisions on direction.
    this.display(); // Update the mesh to match the model.

    this.last_update_time = time;
  }

  @Override
  public void display()
  {
    this.healthbar.setTranslateX(this.positionX);
    this.healthbar.setTranslateY(this.positionY+15);
    this.healthbar.setTranslateZ(this.positionZ);
    this.healthbar.setRotationAxis(Rotate.Y_AXIS);
    this.healthbar.setRotate(90-this.direction);
    this.healthbar.setHeight(this.health/10);

    // Set animation to that specified by our state.
    this.current_animation = this.state_to_animation.get(this.state);
    if(this.current_animation == null) { this.current_animation = this.state_to_animation.get("IDLE"); }
    this.mesh = this.current_animation.get(frame % this.current_animation.size());

    // Transform mesh to match our position.
    double dist = Math.sqrt(Math.pow(this.positionX-player.positionX,2)+Math.pow(this.positionX-player.positionX,2));
    dist/=20;
    if(dist > 1) dist = 1;
    dist = 1-dist;
    body_material = new PhongMaterial(Color.color(dist,dist,dist));
    healthbar_material = new PhongMaterial(Color.color(dist,0,0));
    healthbar.setMaterial(healthbar_material);
    for(Node n : mesh)
    {
      ((Shape3D) n).setMaterial(body_material);
      n.setTranslateX(this.positionX);
      n.setTranslateY(this.positionY);
      n.setTranslateZ(this.positionZ);
      n.setRotationAxis(Rotate.Y_AXIS);
      n.setRotate(90-this.direction);
    }
  }

  public void attack()
  {
    double distanceToPlayer = Math.sqrt( Math.pow(this.positionX-player.positionX,2) + Math.pow(this.positionZ-player.positionZ,2));
    if(distanceToPlayer < 20 && InputHandler.isKeyDown(KeyCode.SPACE)) { this.health -= 1; }
    if( distanceToPlayer < 12 && this.speed != 0)
    {
      double dy = this.positionZ - player.positionZ;
      double dx = this.positionX - player.positionX;
      this.direction = Math.toDegrees(Math.atan2(dy, dx));
      this.proposeState("ATTACKING", 1, 2);
      setSpeed(0);
    }
    if( this.state.equals("ATTACKING") && this.state_timer > 0.3) // If the player's in range and we're still attacking,
                                                                // then deal some damage to the player.
    {
      if( distanceToPlayer < 12 )
      {
        player.setHealth(player.health-5);
      }
      this.proposeState("IDLE",0,2);
    }
  }

  public void updateDirection()
  {
    // Find the path from the player to us.
    PathNode myNode = this.board[ (int) ((this.positionX+5)/10.0) ][ (int) ((this.positionZ+5)/10.0) ];
    PathNode playerNode = this.board[ (int) ((this.player.positionX+5)/10) ][ (int) ((this.player.positionZ+5)/10) ];
    Map<PathNode,PathNode> path = Pathfinding.getPath( this.board, playerNode, myNode );

    // Figure out the length of the path.
    int path_length = 0;
    PathNode temp_node = myNode;
    while(temp_node != null && path != null)
    {
      temp_node = path.get(temp_node);
      path_length++;
    }

    // If we're not too far or too close, and we're adequately close to the center of a square, point at the player.
    if( path_length > 1 && path_length < this.smellRange && this.positionX - ((int) ((this.positionX+5)/10.0))*10 <= 5 &&
            this.positionZ - ((int) ((this.positionZ+5)/10.0))*10 <= 5)
    {
      // ... then move towards the targeted path node.
      double dy = this.positionZ - path.get(myNode).y * 10;
      double dx = this.positionX - path.get(myNode).x * 10;
      this.direction = Math.toDegrees(Math.atan2(dy, dx));
    }
  }

  public void walk()
  {
    double x_component = -Math.cos( Math.toRadians(this.direction));
    double z_component = -Math.sin( Math.toRadians(this.direction));

    //Stop walking if we're going to clip into a wall.
    int bx = (int) (((this.positionX+5)/10.0) + x_component);
    int by = (int) (((this.positionZ+5)/10.0) + z_component);
    if(bx < GameMain.board_size && by < GameMain.board_size && GameMain.board[bx][by] == null)
    {
      this.setSpeed(0);
      this.proposeState("IDLE", 0, 1);
    }
    else // Otherwise keep on walking forwards.
    {
      this.positionX += x_component*this.speed;
      this.positionZ += z_component*this.speed;
    }
  }

// --- I don't really use these much. Why not just reference the variables directly?

  @Override
  public void setSpeed(double sp)
  {
    this.speed = sp;
  }

  @Override
  public void setDamage(double dm)
  {
    this.damage = dm;
  }

  @Override
  public void setStepDistance(double sDis)
  {
    this.stepDistance = sDis;
  }

  @Override
  public void setHealth(double heal)
  {
    this.health = heal;
  }

}
