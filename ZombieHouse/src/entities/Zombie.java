package entities;

import general.GameMain;
import input.InputHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import pathfinding.PathNode;
import pathfinding.Pathfinding;

import java.util.Map;
import java.util.Random;

public class Zombie extends Entity
{
  private double decision_timer;
  private int smell_range;
  private boolean isLineWalkZombie;
  public Integer[] position_start;
  public Box healthbar;
  private PhongMaterial healthbar_material;
  private Entity target;

  public Zombie( int x, int y, int z )
  {
    super.name = "PLAYER";
    super.state = "RUN";
    super.priority = 0;

    super.state_timer = System.currentTimeMillis();
    super.frame_timer = super.state_timer;
    super.update_timer = super.frame_timer;
    this.decision_timer = super.update_timer;

    super.health = 30;
    super.damage = 10;
    super.speed = 1.00;
    this.smell_range = 10;
    this.isLineWalkZombie = Math.random() > 0.5;

    this.healthbar = new Box(5,5,5);
    this.healthbar.setTranslateY(20);
    this.healthbar_material = new PhongMaterial( Color.WHITE );
    this.healthbar.setMaterial( this.healthbar_material );
    GameMain.game_root.getChildren().add( this.healthbar );

    super.direction = 0;
    super.position_x = x;
    super.position_y = y;
    super.position_z = z;
    this.position_start = new Integer[]{x,y,z};

    super.material = new PhongMaterial( Color.RED );
    super.meshview = new MeshView();
    super.meshview.setMesh( null );
  }

  @Override
  public void update(double time)
  {

    /* Return prematurely if we're dead.
    I don't just remove from zombies, because when I restart the level on death the way it's set up right now
    makes removing each zombie early a pain in the rear. Cutting it off here reduces most of the processing cost
    anyhow so it's probably fine. */
    if( super.health <= 0 ){ return; }

    double dt_ms = time - super.update_timer;
    this.decision_timer += dt_ms;
    super.state_timer += dt_ms;

    String state_backup = super.state;

    instigateAttack();
    if( this.decision_timer > 2000 ) updateDirection();

    if(!state_backup.equals( super.state ))
    {
      super.frame = 0;
      super.frame_timer = 0;
      super.state_timer = 0;
    }

    double d = Math.sqrt(Math.pow(super.position_x-GameMain.player.position_x,2)+Math.pow(super.position_z-GameMain.player.position_z,2));
    d = d>100? 0 : 1-d/100;
    this.material.setDiffuseColor( Color.color(d,0,0));
    this.healthbar_material.setDiffuseColor( Color.color(d,d,d));
    super.display( dt_ms );
    this.drawHealthbar( );
    if( super.state.equals("RUN") ) { run(dt_ms); }
    if( super.state.equals("ATTACK")) { attack( dt_ms ); }

    super.update_timer = time;
  }


  private void drawHealthbar()
  {
    this.healthbar.setTranslateX( super.position_x );
    this.healthbar.setTranslateZ( super.position_z );
    this.healthbar.setHeight(this.health/6);
  }

  // Points towards the closest player on the list, I hope.
  private void updateDirection()
  {
    this.decision_timer = 0;
    this.proposeState("RUN", 0, 1);
    super.speed = 0.5;
    if ( this.isLineWalkZombie && this.speed == 0){ this.direction = Math.random()*360; }
    else if ( !this.isLineWalkZombie ){ this.direction = Math.random()*360; }

    PathNode myNode, playerNode, temp_node;
    Map<PathNode,PathNode> path;
    int best_length = Integer.MAX_VALUE;
    int path_length;
    double dy, dx;

    for( Entity p : GameMain.players )
    {
      double dist = Math.sqrt( Math.pow(p.position_x-super.position_x,2)+Math.pow(p.position_z-super.position_z,2));
      if(dist > 200){ return; } // Don't bother searching for the player if we're too far away anyway.

      myNode = GameMain.path_nodes[ (int) ((super.position_x+5)/10.0) ][ (int) ((super.position_z+5)/10.0) ];
      playerNode = GameMain.path_nodes[ (int) ((p.position_x+5)/10) ][ (int) ((p.position_z+5)/10) ];
      path = Pathfinding.getPath( GameMain.path_nodes, playerNode, myNode );

      path_length = 0;
      temp_node = myNode;

      while(temp_node != null && path != null)
      {
        temp_node = path.get(temp_node);
        path_length++;
      }

      if( path_length > 1 && path_length < this.smell_range && super.position_x - ((int) ((super.position_x+5)/10.0))*10 <= 5 &&
              super.position_z - ((int) ((super.position_z+5)/10.0))*10 <= 5 && path_length < best_length)
      {
        // ... then move towards the targeted path node.
        best_length = path_length;
        dx = super.position_z - path.get(myNode).y * 10;
        dy = super.position_x - path.get(myNode).x * 10;
        super.direction = Math.toDegrees(Math.atan2(dy, dx));
      }
    }

  }

  private void instigateAttack() {
    // Pick the closest player and try to attack it.
    double dist;
    double min_dist = 999;
    target = null;
    for( Entity p : GameMain.players )
    {
      dist = Math.sqrt( Math.pow(p.position_x-super.position_x,2)+Math.pow(p.position_z-super.position_z,2));
      if( dist < min_dist )
      {
        min_dist = dist;
        target = p;
      }
    }
    if( target != null && min_dist < 25 )
    {
      this.proposeState("ATTACK", 1, 1);
      //super.direction = Math.toDegrees(Math.atan2( target.position_z-super.position_z, target.position_x-super.position_x ));
    }

  }

  private void attack(double dt)
  {
    super.direction = Math.toDegrees(Math.atan2( super.position_x-target.position_x, super.position_z-target.position_z));
    if( super.state_timer < 400 )
    {
      // We only damage the player, because we don't care about past player's health.
      double distance = Math.sqrt(Math.pow(super.position_x-GameMain.player.position_x,2)+Math.pow(super.position_z-GameMain.player.position_z,2));
      if( distance < 20 )
      {
        GameMain.player.health -= 0.5;
      }
    }
    if( super.state_timer > 600) // 600 ms for attack to complete.
    {
      super.proposeState("IDLE", 0, 2);
    }
  }
  private void run(double dt)
  {
    double z_component = -Math.cos( Math.toRadians(this.direction));
    double x_component = -Math.sin( Math.toRadians(this.direction));

    //Stop walking if we're going to clip into a wall.
    int bx = (int) (((super.position_x+5)/10.0) + x_component);
    int by = (int) (((super.position_z+5)/10.0) + z_component);
    if(bx < GameMain.board_size && by < GameMain.board_size && GameMain.path_nodes[bx][by] == null)
    {
      super.speed = 0;
      super.proposeState("IDLE", 0, 1);
    }
    else // Otherwise keep on walking forwards.
    {
      super.position_x += x_component*super.speed;
      super.position_z += z_component*super.speed;
    }
  }



}
