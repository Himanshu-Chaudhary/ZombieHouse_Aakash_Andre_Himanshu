package general;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import entities.Entity;
import javafx.scene.shape.Mesh;
import java.util.HashMap;
import java.util.Map;

public class MeshManager
{
  private static ObjModelImporter importer = new ObjModelImporter();
  private static Map<String, Mesh> meshes = new HashMap<>();
  private static Map<String, Integer[]> animation_start_and_finish = new HashMap<>();

  // Sadly, for the moment this has to be done manually.
  // Enter in the starting and ending frames for each animation.
  static
  {
    animation_start_and_finish.put("PLAYER_IDLE", new Integer[]{0, 1});
    animation_start_and_finish.put("PLAYER_RUN", new Integer[]{0, 12});
    animation_start_and_finish.put("PLAYER_ATTACK", new Integer[]{0, 40});
  }

  public static Mesh updateMesh ( Entity e )
  {
    String animation_name = String.format("%s_%s", e.name, e.state);
    int start_frame = animation_start_and_finish.get(animation_name)[0];
    int num_frames = animation_start_and_finish.get(animation_name)[1];
    int display_frame = (((int) e.frame)+start_frame) % num_frames;
    String mesh_name = String.format("%s%s_%06d.obj",
            "ZombieHouse/src/meshes/", animation_name, display_frame);
    if( meshes.containsKey(mesh_name) ) return meshes.get(mesh_name);
    importer.read(mesh_name);
    Mesh mesh = importer.getImport()[0].getMesh();
    meshes.put( mesh_name, mesh );
    return mesh;
  }

}