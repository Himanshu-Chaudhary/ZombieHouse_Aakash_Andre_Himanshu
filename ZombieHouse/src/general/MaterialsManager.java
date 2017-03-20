package general;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

public class MaterialsManager
{

  public static final PhongMaterial PLAYER_MATERIAL = new PhongMaterial(
          Color.WHITE, null, null, null,null);

  public static final PhongMaterial PAST_PLAYER_MATERIAL = new PhongMaterial(
          Color.WHITE, null, null, null,null);

  public static final PhongMaterial[] ZOMBIE_MATERIALS = new PhongMaterial[]
          {
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:ZombieHouse/src/images/z_texture_1.png"),
                          null, null,null),
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:ZombieHouse/src/images/z_texture_2.png"),
                          null, null,null),
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:ZombieHouse/src/images/z_texture_3.png"),
                          null, null,null),
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:ZombieHouse/src/images/z_texture_4.png"),
                          null, null,null)
          };
    public static final PhongMaterial[] MASTER_ZOMBIE_MATERIALS = new PhongMaterial[]
            {
                    new PhongMaterial(
                            Color.WHITE,
                            new Image("File:ZombieHouse/src/images/master_zombie.png"),
                            null, null,null)
            };


  // Admittedly this wastes space by importing healthbar images twice.
  public static final PhongMaterial[] HEALTHBAR_MATERIALS = new PhongMaterial[]
          {
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:ZombieHouse/src/meshes/healthbar_1.png"), null, null,
                          new Image("File:ZombieHouse/src/meshes/healthbar_1.png")),
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:ZombieHouse/src/meshes/healthbar_2.png"), null, null,
                          new Image("File:ZombieHouse/src/meshes/healthbar_2.png")),
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:ZombieHouse/src/meshes/healthbar_3.png"), null, null,
                          new Image("File:ZombieHouse/src/meshes/healthbar_3.png")),
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:ZombieHouse/src/meshes/healthbar_4.png"), null, null,
                          new Image("File:ZombieHouse/src/meshes/healthbar_4.png")),
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:ZombieHouse/src/meshes/healthbar_5.png"), null, null,
                          new Image("File:ZombieHouse/src/meshes/healthbar_5.png"))
          };

  public static final PhongMaterial[] PLAYER_HEALTHBAR_MATERIALS = new PhongMaterial[]
          {
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:ZombieHouse/src/meshes/phealthbar_1.png"), null, null,
                          new Image("File:ZombieHouse/src/meshes/phealthbar_1.png")),
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:ZombieHouse/src/meshes/phealthbar_2.png"), null, null,
                          new Image("File:ZombieHouse/src/meshes/phealthbar_2.png")),
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:ZombieHouse/src/meshes/phealthbar_3.png"), null, null,
                          new Image("File:ZombieHouse/src/meshes/phealthbar_3.png")),
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:ZombieHouse/src/meshes/phealthbar_4.png"), null, null,
                          new Image("File:ZombieHouse/src/meshes/phealthbar_4.png")),
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:ZombieHouse/src/meshes/phealthbar_5.png"), null, null,
                          new Image("File:ZombieHouse/src/meshes/phealthbar_5.png"))
          };


  public static final PhongMaterial[] FLOOR_MATERIALS = new PhongMaterial[]
          {
                  new PhongMaterial( Color.WHITE,
                          new Image("File:ZombieHouse/src/images/white_brick.jpg", 128, 128, true, true, true), // Diffuse
                          new Image("File:ZombieHouse/src/images/specular_brick.jpg", 128, 128, true, true, false), // Specular
                          new Image("File:ZombieHouse/src/images/normal_brick_2.jpg", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illumination
                  ),
                  new PhongMaterial( Color.WHITE,
                          new Image("File:ZombieHouse/src/images/big_checker.png", 128, 128, true, true, true), // Diffuse
                          new Image("File:ZombieHouse/src/images/big_checker_specular.png", 128, 128, true, true, false), // Specular
                          new Image("File:ZombieHouse/src/images/big_checker_bump.png", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illumination
                  ),
                  new PhongMaterial( Color.WHITE,
                          new Image("File:ZombieHouse/src/images/red_carpet.png", 128, 128, true, true, true), // Diffuse
                          new Image("File:ZombieHouse/src/images/carpet_bump.png", 128, 128, true, true, false), // Specular
                          new Image("File:ZombieHouse/src/images/carpet_normal.png", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illumination
                  ),
                  new PhongMaterial( Color.WHITE,
                          new Image("File:ZombieHouse/src/images/wood_planks.jpg", 128, 128, true, true, true), // Diffuse
                          new Image("File:ZombieHouse/src/images/wood_planks_specular.jpg", 128, 128, true, true, false), // Specular
                          new Image("File:ZombieHouse/src/images/wood_planks_normal.jpg", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illumination
                  )
          };

  public static final PhongMaterial[] WALL_MATERIALS = new PhongMaterial[]
          {
                  new PhongMaterial( Color.WHITE,
                          null, //new Image("File:ZombieHouse/src/images/brick_compress_test.png", 400, 400, true, true, false), // Diffuse
                          null, //new Image("File:ZombieHouse/src/images/brick_compress_test_s.png", 128, 128, true, true, false), // Specular
                          null, //new Image("File:ZombieHouse/src/images/brick_compress_test_n.png", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illumination
                  ),
                  new PhongMaterial( Color.WHITE,
                          null, //new Image("File:ZombieHouse/src/images/wood_compress.png", 400, 400, true, true, false), // Diffuse
                          null, //new Image("File:ZombieHouse/src/images/checker_specular.jpg", 128, 128, true, true, false), // Specular
                          null, //new Image("File:ZombieHouse/src/images/checker_normal.jpg", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illumination
                  ),
                  new PhongMaterial( Color.WHITE,
                          null, //new Image("File:ZombieHouse/src/images/tile_compress.png", 400, 400, true, true, false), // Diffuse
                          null, //new Image("File:ZombieHouse/src/images/checker_specular.jpg", 128, 128, true, true, false), // Specular
                          null, //new Image("File:ZombieHouse/src/images/checker_normal.jpg", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illumination
                  ),
                  new PhongMaterial( Color.WHITE,
                          null, //new Image("File:ZombieHouse/src/images/wallpaper_compress.png", 400, 400, true, true, false), // Diffuse
                          null, //new Image("File:ZombieHouse/src/images/checker_specular.jpg", 128, 128, true, true, false), // Specular
                          null, //new Image("File:ZombieHouse/src/images/checker_normal.jpg", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illumination
                  )
          };

  public static final PhongMaterial[] CEILING_MATERIALS = new PhongMaterial[]
          {
                  new PhongMaterial( Color.WHITE,
                          new Image("File:ZombieHouse/src/images/white_brick.jpg", 128, 128, true, true, true), // Diffuse
                          new Image("File:ZombieHouse/src/images/specular_brick.jpg", 128, 128, true, true, false), // Specular
                          new Image("File:ZombieHouse/src/images/normal_brick_2.jpg", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illumination
                  ),
                  new PhongMaterial( Color.WHITE,
                          new Image("File:ZombieHouse/src/images/checker_plain_2.jpg", 128, 128, true, true, true), // Diffuse
                          new Image("File:ZombieHouse/src/images/checker_specular.jpg", 128, 128, true, true, false), // Specular
                          new Image("File:ZombieHouse/src/images/checker_normal.jpg", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illumination
                  ),
                  new PhongMaterial( Color.WHITE,
                          new Image("File:ZombieHouse/src/images/red_carpet.png", 128, 128, true, true, true), // Diffuse
                          new Image("File:ZombieHouse/src/images/carpet_bump.jpg", 128, 128, true, true, false), // Specular
                          new Image("File:ZombieHouse/src/images/carped_normal.jpg", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illumination
                  ),
                  new PhongMaterial( Color.WHITE,
                          new Image("File:ZombieHouse/src/images/wood_planks.jpg", 128, 128, true, true, true), // Diffuse
                          new Image("File:ZombieHouse/src/images/wood_planks_specular.jpg", 128, 128, true, true, false), // Specular
                          new Image("File:ZombieHouse/src/images/wood_planks_normal.jpg", 128, 128, true, true, false), // Normal/Bump
                          null) // Self-Illumination
          };

}
