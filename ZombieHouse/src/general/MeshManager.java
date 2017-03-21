package general;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import entities.Entity;
import javafx.scene.Node;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;

import java.util.HashMap;
import java.util.Map;

public class MeshManager
{
  private static ObjModelImporter importer = new ObjModelImporter();
  private static Map<String, Mesh> meshes = new HashMap<>();
  private static Map<String, Material> materials = new HashMap<>();
  private static Map<String, Integer[]> animation_start_and_finish = new HashMap<>();

  // Sadly, for the moment this has to be done manually.
  // Enter in the starting and ending frames for each animation.
  static
  {
    animation_start_and_finish.put("player_IDLE", new Integer[]{0, 1});
    animation_start_and_finish.put("player_RUN", new Integer[]{0, 12});
    animation_start_and_finish.put("player_ATTACK", new Integer[]{0, 40});
    animation_start_and_finish.put("player_WALK", new Integer[]{1, 32});
    animation_start_and_finish.put("zombie_IDLE", new Integer[]{0, 1});
    animation_start_and_finish.put("zombie_WALK", new Integer[]{0, 40});
    animation_start_and_finish.put("zombie_ATTACK", new Integer[]{0, 35});
    animation_start_and_finish.put("zombie_DIE", new Integer[]{0, 13});
    animation_start_and_finish.put("player_DIE", new Integer[]{0, 13});
    animation_start_and_finish.put("zombie_DEAD", new Integer[]{0, 1});
    animation_start_and_finish.put("player_DEAD", new Integer[]{0, 1});
  }

  public static Mesh updateMesh ( Entity e )
  {
    String animation_name = String.format("%s_%s", e.name, e.state);
    int start_frame = animation_start_and_finish.get(animation_name)[0];
    int num_frames = animation_start_and_finish.get(animation_name)[1];
    int display_frame = (((int) e.frame)%num_frames) + start_frame;

    String mesh_name = String.format("%s%s/%s_%06d.obj",
            "ZombieHouse/resources/meshes/", e.name, animation_name, display_frame);
    if( meshes.containsKey(mesh_name) ) return meshes.get(mesh_name);
    importer.read(mesh_name);
    Mesh mesh = importer.getImport()[0].getMesh();
    PhongMaterial m = (PhongMaterial) getMaterial( String.format("%s/%s_%06d", e.name, animation_name, display_frame));
    // Once upon a time I thought we might have time to implement neat changing textures per mesh but maybe not this time.
    //if( m != null) e.material = (PhongMaterial) getMaterial( String.format("%s/%s_%06d", e.name, animation_name, display_frame));
    meshes.put( mesh_name, mesh );
    return mesh;
  }

  public static Mesh getMesh( String name )
  {
    // Add the quick reference import here so we don't import a million times over.
    if( meshes.containsKey(name) ){ return meshes.get(name); }
    importer.read("ZombieHouse/resources/meshes/"+name+".obj");
    Node[] temp = importer.getImport();


    Mesh mesh = ((MeshView)temp[0]).getMesh();
    //System.out.println(" " + ((MeshView) temp[0]).getMaterial());

    meshes.put( name, mesh );
    return mesh;
  }

  public static Material getMaterial(String name )
  {
    if( materials.containsKey(name) ){ return materials.get(name); }
    importer.read("ZombieHouse/resources/meshes/"+name+".obj");
    Node[] temp = importer.getImport();
    materials.put( name, ((MeshView) temp[0]).getMaterial());
    return ((MeshView) temp[0]).getMaterial();
  }



}