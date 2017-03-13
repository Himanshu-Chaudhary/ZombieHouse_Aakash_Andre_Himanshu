package entities;

import javafx.scene.Group;
import javafx.scene.shape.Cylinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hIM on 3/13/2017.
 */
public class PastSelf{
        private List<Double> walkBehaviorsX = new ArrayList<>(); //stores x direction
        private List<Double> walkBehaviorsZ = new ArrayList<>(); // stores z directiom
        private List<Double> actionBehaviors = new ArrayList<>(); // stores behaviour

        private Cylinder boundingCircle = new Cylinder();

        private int turnWorldEnds;  // stores the index of each move

        //for animation of a past player
        public Group selfMeshes = new Group();
        public int currentFrame = 0;
        private boolean direction = true;

        public PastSelf (List<Double> walkBehaviorsX, List<Double> walkBehaviorsZ, List<Double> actionBehaviors, int turnIndex)
        {
            this.walkBehaviorsX.addAll(walkBehaviorsX);
            this.walkBehaviorsZ.addAll(walkBehaviorsZ);
            this.actionBehaviors.addAll(actionBehaviors);
            this.turnWorldEnds = turnIndex;
        }

        public void replay (int turnIndex){

        }

        public void nextMesh(){

        }

    public int getTurnWorldEnds(){
        return turnWorldEnds;
    }




}
