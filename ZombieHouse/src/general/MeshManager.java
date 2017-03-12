package general;

import com.interactivemesh.jfx.importer.obj.ObjImportOption;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.scene.CacheHint;
import javafx.scene.Node;

import java.util.ArrayList;

public class MeshManager
{
  private static ObjModelImporter importer = new ObjModelImporter();

  /**
   *
   * @param modelName
   * @return mesh
   *
   * User asks for a model using its name, and we return it.
   */
  public static Node[] getMesh(String modelName)
  {
    importer.setOptions(ObjImportOption.NONE);
    importer.read(modelName);
    Node[] mesh = importer.getImport();
    for(int i = 0;i<mesh.length;i++)
    {
      mesh[i].setCache(true);
      mesh[i].setCacheHint(CacheHint.SPEED);
    }
    importer.clear();
    return mesh;
  }

  public static ArrayList<Node[]> getAnimation(String animation_name, int animation_length)
  {
    ArrayList<Node[]> animation = new ArrayList<>();
    for(int i = 0; i < animation_length; i++)
    {
      animation.add(getMesh(animation_name+"_"+i+".obj"));
    }
    return animation;
  }

}
