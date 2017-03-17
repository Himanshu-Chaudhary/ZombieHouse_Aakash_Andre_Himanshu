package map_generation;

import general.GameMain;
import general.MaterialsManager;
import general.MeshManager;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import pathfinding.PathNode;

import java.util.ArrayList;
import java.util.List;

public class BoardManager
{

  private static ArrayList<Mesh> mapMeshes = new ArrayList<>();

  static
  {
    mapMeshes.add( MeshManager.getMesh("Rib_Wall_0"));
    mapMeshes.add( MeshManager.getMesh("Rib_Peninsula_0"));
    mapMeshes.add( MeshManager.getMesh("Rib_Corner_0"));
  }

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
          //board_boxes[0][x][y].setHeight(60);
          //board_boxes[0][x][y].setTranslateY(20);

//          boolean above = y+1 < GameMain.board_size-1 || (map[x][y+1].isWall || map[x][y+1].isBorder);
//          boolean below = y-1 > 0 || (map[x][y-1].isWall || map[x][y-1].isBorder);
//          boolean left = x+1 < GameMain.board_size-1 || (map[x+1][y].isWall || map[x+1][y].isBorder);
//          boolean right = x-1 > 0 || (map[x-1][y].isWall || map[x-1][y].isBorder);
          boolean above = !( y+1 < GameMain.board_size-1 && (!map[x][y+1].isWall && !map[x][y+1].isBorder) );
          boolean below = !( y-1 > 0 && (!map[x][y-1].isWall && !map[x][y-1].isBorder) );
          boolean left = !( x+1 < GameMain.board_size-1 && (!map[x+1][y].isWall && !map[x+1][y].isBorder) );
          boolean right = !( x-1 > 0 && (!map[x-1][y].isWall && !map[x-1][y].isBorder) );
          int total = 0;
          if(above) total++;
          if(below) total++;
          if(left) total++;
          if(right) total++;

          MeshView wall = new MeshView();
          wall.setScaleX(8);
          wall.setScaleY(-8);
          wall.setScaleZ(8);
          wall.setTranslateX(x*10);
          wall.setTranslateZ(y*10);
          wall.setTranslateY(20);
          wall.setRotationAxis(Rotate.Y_AXIS);

          wall.setMaterial( temp_material ); // Just for now. I'll sort this later.

          if ( (left && right) || (above && below) ) // It's a wall, or won't do harm acting as one.
          {
            wall.setMesh( MeshManager.getMesh("Rib_Wall_0") );
            GameMain.game_root.getChildren().add(wall);
//            wall.setMaterial( temp_material );
//            wall.setMaterial( MeshManager.getMaterial("Rib_Wall_0"));
//            ((PhongMaterial) wall.getMaterial()).setDiffuseColor( Color.WHITE );
          }
          else if ( total == 1 ) // Peninsula.
          {
            wall.setMesh( MeshManager.getMesh("Rib_Peninsula_0") );
            GameMain.game_root.getChildren().add(wall);
//            wall.setMaterial( temp_material );
//            wall.setMaterial( MeshManager.getMaterial("Rib_Wall_0"));
//            ((PhongMaterial) wall.getMaterial()).setDiffuseColor( Color.BLUE );
          }
          else
          {
            wall.setMesh( MeshManager.getMesh("Rib_Corner_0") );
            GameMain.game_root.getChildren().add(wall);
//            wall.setMaterial( temp_material );
//            wall.setMaterial( MeshManager.getMaterial("Rib_Corner_0"));
//            ((PhongMaterial) wall.getMaterial()).setDiffuseColor( Color.SALMON );
          }

          // Rotate as needed for each mesh.
          if( above && below ){ /* No rotation. */ }
          else if( left && right){ wall.setRotate( 90 ); }
          else if( above && !below && !left && !right){ /* No rotation. */ }
          else if( below && !above && !left && !right){ wall.setRotate( 180 ); }
          else if( left && !above && !below && !right){ wall.setRotate( 90 ); }
          else if( right && !below && !left && !above){ wall.setRotate( 270 ); }
          else if( left && below ){ wall.setRotate(180); }
          else if ( right && above ){ /*No rotation. */ }
          else if ( left && above ){ wall.setRotate(90);}
          else if ( right && below ){ wall.setRotate(270);}

          // And now set the ceiling above the wall.
          // Set the ceiling to a material based on that of the ceiling material for this region.
          // [ The reason for the separate materials (new materials for each) is for our smooth FoW effect. ]
          temp_material = new PhongMaterial( MaterialsManager.CEILING_MATERIALS[map[x][y].getRegion()-1].getDiffuseColor() );
          temp_material.setDiffuseMap( MaterialsManager.CEILING_MATERIALS[map[x][y].getRegion()-1].getDiffuseMap() );
          temp_material.setSpecularMap( MaterialsManager.CEILING_MATERIALS[map[x][y].getRegion()-1].getDiffuseMap() );
          temp_material.setBumpMap( MaterialsManager.CEILING_MATERIALS[map[x][y].getRegion()-1].getBumpMap() );

          board_boxes[1][x][y].setMaterial( temp_material );


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

          if( map[x][y].isObstacle )
          {
            MeshView pillar = new MeshView();
            pillar.setMesh( MeshManager.getMesh("Rib_Wall_0") );
            pillar.setScaleX(8);
            pillar.setScaleY(-8);
            pillar.setScaleZ(8);
            pillar.setTranslateX(x*10);
            pillar.setTranslateZ(y*10);
            pillar.setTranslateY(20);
            GameMain.game_root.getChildren().add(pillar);

            pillar.setMaterial( temp_material ); // Again, just for now. Will sort out later.
//            pillar.setMaterial( MeshManager.getMaterial("Rib_Wall_0"));
//            ((PhongMaterial) pillar.getMaterial()).setDiffuseColor( Color.WHITE );
          }

          // Set the ceiling to a material based on that of the ceiling material for this region.
          // [ The reason for the separate materials (new materials for each) is for our smooth FoW effect. ]
          temp_material = new PhongMaterial( MaterialsManager.CEILING_MATERIALS[map[x][y].getRegion()-1].getDiffuseColor() );
          temp_material.setDiffuseMap( MaterialsManager.CEILING_MATERIALS[map[x][y].getRegion()-1].getDiffuseMap() );
          temp_material.setSpecularMap( MaterialsManager.CEILING_MATERIALS[map[x][y].getRegion()-1].getDiffuseMap() );
          temp_material.setBumpMap( MaterialsManager.CEILING_MATERIALS[map[x][y].getRegion()-1].getBumpMap() );

          board_boxes[1][x][y].setMaterial( temp_material );

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
      if(child instanceof Box || (child instanceof MeshView &&  mapMeshes.contains(((MeshView) child).getMesh())))
      {
        remove_list.add( child );
      }
    }
    group.getChildren().removeAll(remove_list);
  }
}
