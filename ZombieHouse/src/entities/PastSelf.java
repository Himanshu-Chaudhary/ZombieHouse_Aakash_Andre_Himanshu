package entities;

import general.MeshManager;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hIM on 3/13/2017.
 */
public class PastSelf extends Player{


        private int currentStep;
         private int totalStep;



        public PastSelf (List<Double> walkBehaviorsX, List<Double> walkBehaviorsZ, List<String> stateBehaviors,  List<Double> directionBehaviour)
        {
            this.walkBehaviorsX.addAll(walkBehaviorsX);
            this.walkBehaviorsZ.addAll(walkBehaviorsZ);
            this.stateBehaviors.addAll(stateBehaviors);
            this.directionBehaviour.addAll(directionBehaviour);
            this.positionY = 5;

          this.state_to_animation.put("WALK", MeshManager.getAnimation("ZombieHouse/src/Meshes/Zombie_Run/run", 12));
          this.state_to_animation.put("IDLE", MeshManager.getAnimation("ZombieHouse/src/Meshes/Zombie_Idle/idle", 1));
          this.state_to_animation.put("ATTACK", MeshManager.getAnimation("ZombieHouse/src/Meshes/Zombie_Attack/attack",10));
          for( ArrayList<Node[]> animation : this.state_to_animation.values()){ animationTransform(animation); }
          totalStep = walkBehaviorsX.size();
          display();

        }

  @Override
  public void update (double time){
    String temp = this.state;
    if (currentStep < totalStep){
      this.positionX = walkBehaviorsX.get(currentStep);
      this.positionZ = walkBehaviorsZ.get(currentStep);
      this.state = stateBehaviors.get(currentStep);
      this.direction = directionBehaviour.get(currentStep);


      double dt = (time - this.last_update_time) / Math.pow(10, 3);
      this.state_timer += dt;
      this.frame_timer += dt;

      if(!state.equals(temp))
      {
        this.state_timer = 0;
        this.frame = 0;
      }

      this.display();
      last_update_time = time;
    }
    currentStep++;
    System.out.println("PasstSelf Updated");
  }

  }











