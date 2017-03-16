package map_generation;

import general.GameMain;
import general.MaterialsManager;
import general.MeshManager;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import pathfinding.PathNode;

import java.util.ArrayList;
import java.util.List;

public class BoardManager
{

  public static void configurePathNodes( Tile[][] map, PathNode[][] path_nodes )
  {
    for( int x = 0; x < map.length; x++ )
    {
      for( int y = 0; y < map[0].length; y++ )
      {
        path_nodes[x][y] = null;
        if( map[x][y].getRegion() == -1 || map[x][y].isWall ){}
        else{ path_nodes[x][y] = new PathNode(x,y,0); }
      }
    }
  }

  public static void addMapMeshes(Box[][][] board_boxes, Tile[][] map, Group group )
  {
    PhongMaterial temp_material;
    for(int x = 0; x < map.length; x++)
    {
      for(int y = 0; y < map[0].length; y++)
      {
        board_boxes[0][x][y] = null;
        board_boxes[0][x][y] = new Box(10,10,10);
        board_boxes[0][x][y].setTranslateX(x*10);
        board_boxes[0][x][y].setTranslateZ(y*10);
        board_boxes[0][x][y].setTranslateY(-10);

        board_boxes[1][x][y] = new Box(10,10,10);
        board_boxes[1][x][y].setTranslateX(x*10);
        board_boxes[1][x][y].setTranslateZ(y*10);
        board_boxes[1][x][y].setTranslateY(40);

        group.getChildren().add(board_boxes[0][x][y]);
        group.getChildren().add(board_boxes[1][x][y]);

        if( map[x][y].getRegion() == -1)
        {
          GameMain.exit_x = x*10;
          GameMain.exit_z = y*10;
        }
        else if( map[x][y].isWall && !map[x][y].isObstacle )
        {
          // Set the wall to a material based on that of the wall material for this region.
          // [ The reason for the separate materials (new materials for each) is for our smooth FoW effect. ]
          temp_material = new PhongMaterial( MaterialsManager.WALL_MATERIALS[map[x][y].getRegion()-1].getDiffuseColor() );
          temp_material.setDiffuseMap( MaterialsManager.WALL_MATERIALS[map[x][y].getRegion()-1].getDiffuseMap() );
          temp_material.setSpecularMap( MaterialsManager.WALL_MATERIALS[map[x][y].getRegion()-1].getDiffuseMap() );
          temp_material.setBumpMap( MaterialsManager.WALL_MATERIALS[map[x][y].getRegion()-1].getBumpMap() );

          board_boxes[0][x][y].setMaterial( temp_material );
          board_boxes[0][x][y].setHeight(60);
          board_boxes[0][x][y].setTranslateY(20);
        }
        else
        {
          // Set the floor to a material based on that of the floor material for this region.
          // [ The reason for the separate materials (new materials for each) is for our smooth FoW effect. ]
          temp_material = new PhongMaterial( MaterialsManager.FLOOR_MATERIALS[map[x][y].getRegion()-1].getDiffuseColor() );
          temp_material.setDiffuseMap( MaterialsManager.FLOOR_MATERIALS[map[x][y].getRegion()-1].getDiffuseMap() );
          temp_material.setSpecularMap( MaterialsManager.FLOOR_MATERIALS[map[x][y].getRegion()-1].getDiffuseMap() );
          temp_material.setBumpMap( MaterialsManager.FLOOR_MATERIALS[map[x][y].getRegion()-1].getBumpMap() );

          board_boxes[0][x][y].setMaterial(temp_material);

          // Set the ceiling to a material based on that of the ceiling material for this region.
          // [ The reason for the separate materials (new materials for each) is for our smooth FoW effect. ]
          temp_material = new PhongMaterial( MaterialsManager.CEILING_MATERIALS[map[x][y].getRegion()-1].getDiffuseColor() );
          temp_material.setDiffuseMap( MaterialsManager.CEILING_MATERIALS[map[x][y].getRegion()-1].getDiffuseMap() );
          temp_material.setSpecularMap( MaterialsManager.CEILING_MATERIALS[map[x][y].getRegion()-1].getDiffuseMap() );
          temp_material.setBumpMap( MaterialsManager.CEILING_MATERIALS[map[x][y].getRegion()-1].getBumpMap() );

          board_boxes[1][x][y].setMaterial( temp_material );

          if( map[x][y].isObstacle )
          {
            MeshView pillar = new MeshView();
            pillar.setMesh( MeshManager.getMesh("tex_tall_spike") );
            pillar.setScaleX(3);
            pillar.setScaleY(-3);
            pillar.setScaleZ(3);
            pillar.setTranslateX(x*10);
            pillar.setTranslateZ(y*10);
            pillar.setTranslateY(28);
            pillar.setMaterial( new PhongMaterial(Color.GRAY, new Image("File:ZombieHouse/src/images/white_brick.jpg", 128, 128, true, true, true), null, null, null));
            GameMain.game_root.getChildren().add(pillar);
          }

        }
      }
    }
  }

  public static void removeMapMeshes(Group group )
  {
    ArrayList<Node> remove_list = new ArrayList<>();
    List<Node> groupChildren = group.getChildren();
    for( Node child :  groupChildren )
    {
      if(child instanceof Box || (child instanceof MeshView &&  ((MeshView) child).getMesh() == MeshManager.getMesh("tex_tall_spike")) ) // || ((MeshView) child).getMesh() == MeshManager.getMesh("tex_tall_spike"))
      {
        remove_list.add( child );
      }
    }
    group.getChildren().removeAll(remove_list);
  }
}
