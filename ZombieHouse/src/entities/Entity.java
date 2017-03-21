package entities;

import general.GameMain;
import general.MeshManager;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;

public abstract class Entity
{
  public String name;

  public String state;
  public int priority;

  public double state_timer;
  public double update_timer;
  public double frame_timer;
  public double frame;

  public int health;
  public double damage;
  public double speed;

  public double direction;
  public double position_x;
  public double position_y;
  public double position_z;

  public MeshView meshview;
  public PhongMaterial material;

  public abstract void update( double time );
  public void proposeState( String new_state, int new_priority, int override_priority)
  {
    if( this.state.equals( new_state )) return;
    if( override_priority > this.priority )
    {
      this.state = new_state;
      this.priority = new_priority;
    }
  }
  public void display( double dt_ms )
  {
    frame += dt_ms / 25; // About 40fps, I think.
    this.meshview.setMesh(MeshManager.updateMesh( this ));
    this.meshview.setTranslateX( this.position_x );
    this.meshview.setTranslateY( this.position_y );
    this.meshview.setTranslateZ( this.position_z );
    this.meshview.setScaleX(6);
    this.meshview.setScaleY(-6);
    this.meshview.setScaleZ(6);
    this.meshview.setRotationAxis(Rotate.Y_AXIS);
    this.meshview.setRotate( this.direction );
    //this.material = (PhongMaterial) (MeshManager.getMaterial("green_box"));
    this.meshview.setMaterial( this.material );

    if( this.meshview != null && !GameMain.game_root.getChildren().contains( this.meshview ))
    {
      GameMain.game_root.getChildren().add( this.meshview );
    }
  }

}

