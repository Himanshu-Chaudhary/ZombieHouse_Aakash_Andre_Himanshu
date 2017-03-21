package entities;

import general.GameMain;
import general.MaterialsManager;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;

import java.util.List;

/**
 * @author Himanshu Chaudhary, Andre' Green
 *
 * has all the settings and attributes for pastself in the game
 */

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
    super.material = new PhongMaterial(Color.CYAN);
    super.material.setDiffuseMap(MaterialsManager.PAST_PLAYER_MATERIAL.getDiffuseMap());
    super.name = "player";
    this.past_position_x = ppx;
    this.past_position_z = ppz;
    this.position_y = 40;

    this.past_states = ss;
    this.past_direction = pd;

    totalStep = ss.size();

    super.meshview = new MeshView();
    super.meshview.setMesh( null );
  }

  public boolean isDead()
  {
    return currentStep>=totalStep;
  }

  /**
   * @param time
   *      the time at which the update takes place
   *
   * Updates the state of the pastself by looking at the right index of the stored moves of the player
   */
  public void update( double time )
  {
    /* if pastself is dead, it doesn't need to update*/
    if (currentStep==(totalStep+2)) return;

    super.meshview.setMaterial( super.material );
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
    else if (currentStep == totalStep)
    {
      this.proposeState("DIE", 0, 10);
      display( dt_ms );
      currentStep++;

    }
    else
    {
      {
        this.proposeState("DEAD", 0, 10);
        display( dt_ms );
        currentStep++;

      }
    }
    super.update_timer = time;
  }

  /**
   * Attacks at the time player attacked and
   * When the past self attacks, it still  damages the player
   */

  private void attack(double dt)
  {
    if( super.state_timer > 400 )
    {
      double distance;
      for( Zombie z : GameMain.zombies )
      {
        distance = Math.sqrt( Math.pow(z.position_x-super.position_x,2)+Math.pow(z.position_z-super.position_z,2));
        if( distance < 30 ) { z.health -= 1; }
      }
    }

    if( super.state_timer > 400 )
    {
      double distance;
      distance = Math.sqrt( Math.pow(GameMain.player.position_x-super.position_x,2)+Math.pow(GameMain.player.position_z-super.position_z,2));
      if( distance < 30 ){ GameMain.player.health -= 1; }
    }

  }




}
