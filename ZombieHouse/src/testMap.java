import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class testMap extends Application {

    private final Group root = new Group();
    public MapGeneration map = new MapGeneration();


    @Override public void start(Stage stage)
    {
        Button viewMap = new Button("View Map");
     root.getChildren().add(viewMap);
     viewMap.setOnAction(e->{
         MapGeneration map = new MapGeneration();
         stage.setScene(map.mapViewerScene(stage));
     });
        Scene scene = new Scene( root, 1024, 768, true );
        stage.setScene(scene);

        stage.show();
    }
    public static void main (String[] args){launch(args);}
}