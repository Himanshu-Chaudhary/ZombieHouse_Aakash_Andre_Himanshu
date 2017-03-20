package sound;

import javafx.application.Application;
import javafx.stage.Stage;

public class testSound extends Application{


  public static void main(String[] args){
    launch(args);

  }

  @Override
  public void start(Stage primaryStage) throws Exception
  {
    SoundManager soundManager = new SoundManager();
    soundManager.playSwordSwing();
  }
}