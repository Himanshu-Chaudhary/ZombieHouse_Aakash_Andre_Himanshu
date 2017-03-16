package entities;

import general.GameMain;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;

import java.util.List;

public class PastSelf extends Entity
{
  public int currentStep;
  private int totalStep;

  private List<Double> past_position_x;
  private List<Double> past_position_z;
  private List<Double> past_direction;
  private List<String> past_states;

  public PastSelf (List<Double> ppx, List<Double> ppz, List<String> ss, List<Double> pd)
  {
    super.name = "PLAYER";
    this.past_position_x = ppx;
    this.past_position_z = ppz;
    this.position_y = 40;

    this.past_states = ss;
    this.past_direction = pd;

    totalStep = ss.size();

    super.meshview = new MeshView();
    super.meshview.setMesh( null );
  }

  public void update( double time )
  {
    double dt_ms = time - super.update_timer;
    state_timer += dt_ms;
    if( currentStep < totalStep )
    {
      String state_backup = super.state;

      this.position_x = this.past_position_x.get( currentStep );
      this.position_z = this.past_position_z.get( currentStep );
      super.direction = this.past_direction.get( currentStep );
      this.state = this.past_states.get( currentStep );

      if(super.state != null && state_backup != null && !state_backup.equals( super.state ))
      {
        super.frame = 0;
        super.frame_timer = 0;
        super.state_timer = 0;
      }

      if( super.state.equals("ATTACK")){ attack (dt_ms); }
      display( dt_ms );

      currentStep++;
    }
    super.update_timer = time;
  }

  // When the past self attacks, it still needs to do damage.
  private void attack(double dt)
  {
    if( super.state_timer > 400 )
    {
      double distance;
      for( Zombie z : GameMain.zombies )
      {
        distance = Math.sqrt( Math.pow(z.position_x-super.position_x,2)+Math.pow(z.position_z-super.position_z,2));
        if( distance < 20 ) { z.health -= 1; }
      }
    }

    if( super.state_timer > 400 )
    {
      double distance;
      distance = Math.sqrt( Math.pow(GameMain.player.position_x-super.position_x,2)+Math.pow(GameMain.player.position_z-super.position_z,2));
      if( distance < 20 ){ GameMain.player.health -= 1; }
    }

  }

}
