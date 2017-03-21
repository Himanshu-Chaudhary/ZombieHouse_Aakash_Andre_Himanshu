package general;

/**
 * @author Andre' Green
 * uses different textures to create material and assigns them to static final variables
 */

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
                          new Image("File:Resources/images/z_texture_1.png"),
                          null, null,null),
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:Resources/images/z_texture_2.png"),
                          null, null,null),
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:Resources/images/z_texture_3.png"),
                          null, null,null),
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:Resources/images/z_texture_4.png"),
                          null, null,null)
          };
    public static final PhongMaterial MASTER_ZOMBIE_MATERIALS = new PhongMaterial(
                            Color.WHITE,
                            new Image("File:Resources/images/master_zombie_texture.png"),
                            null, null,null);



  // Admittedly this wastes space by importing healthbar images twice.
  public static final PhongMaterial[] HEALTHBAR_MATERIALS = new PhongMaterial[]
          {
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:Resources/meshes/healthbar_1.png"), null, null,
                          new Image("File:Resources/meshes/healthbar_1.png")),
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:Resources/meshes/healthbar_2.png"), null, null,
                          new Image("File:Resources/meshes/healthbar_2.png")),
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:Resources/meshes/healthbar_3.png"), null, null,
                          new Image("File:Resources/meshes/healthbar_3.png")),
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:Resources/meshes/healthbar_4.png"), null, null,
                          new Image("File:Resources/meshes/healthbar_4.png")),
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:Resources/meshes/healthbar_5.png"), null, null,
                          new Image("File:Resources/meshes/healthbar_5.png"))
          };

  public static final PhongMaterial[] PLAYER_HEALTHBAR_MATERIALS = new PhongMaterial[]
          {
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:Resources/meshes/phealthbar_1.png"), null, null,
                          new Image("File:Resources/meshes/phealthbar_1.png")),
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:Resources/meshes/phealthbar_2.png"), null, null,
                          new Image("File:Resources/meshes/phealthbar_2.png")),
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:Resources/meshes/phealthbar_3.png"), null, null,
                          new Image("File:Resources/meshes/phealthbar_3.png")),
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:Resources/meshes/phealthbar_4.png"), null, null,
                          new Image("File:Resources/meshes/phealthbar_4.png")),
                  new PhongMaterial(
                          Color.WHITE,
                          new Image("File:Resources/meshes/phealthbar_5.png"), null, null,
                          new Image("File:Resources/meshes/phealthbar_5.png"))
          };


  public static final PhongMaterial[] FLOOR_MATERIALS = new PhongMaterial[]
          {
                  new PhongMaterial( Color.WHITE,
                          new Image("File:Resources/images/white_brick.jpg", 128, 128, true, true, true), // Diffuse
                          new Image("File:Resources/images/specular_brick.jpg", 128, 128, true, true, false), // Specular
                          new Image("File:Resources/images/normal_brick_2.jpg", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illumination
                  ),
                  new PhongMaterial( Color.WHITE,
                          new Image("File:Resources/images/big_checker.png", 128, 128, true, true, true), // Diffuse
                          new Image("File:Resources/images/big_checker_specular.png", 128, 128, true, true, false), // Specular
                          new Image("File:Resources/images/big_checker_bump.png", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illumination
                  ),
                  new PhongMaterial( Color.WHITE,
                          new Image("File:Resources/images/red_carpet.png", 128, 128, true, true, true), // Diffuse
                          new Image("File:Resources/images/carpet_bump.png", 128, 128, true, true, false), // Specular
                          new Image("File:Resources/images/carpet_normal.png", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illumination
                  ),
                  new PhongMaterial( Color.WHITE,
                          new Image("File:Resources/images/wood_planks.jpg", 128, 128, true, true, true), // Diffuse
                          new Image("File:Resources/images/wood_planks_specular.jpg", 128, 128, true, true, false), // Specular
                          new Image("File:Resources/images/wood_planks_normal.jpg", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illumination
                  )
          };

  public static final PhongMaterial[] WALL_MATERIALS = new PhongMaterial[]
          {
                  new PhongMaterial( Color.WHITE,
                          null, //new Image("File:src/images/brick_compress_test.png", 400, 400, true, true, false), // Diffuse
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
                          new Image("File:ZombieHouse/Resources/images/white_brick.jpg", 128, 128, true, true, true), // Diffuse
                          new Image("File:ZombieHouse/Resources/images/specular_brick.jpg", 128, 128, true, true, false), // Specular
                          new Image("File:ZombieHouse/Resources/images/normal_brick_2.jpg", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illumination
                  ),
                  new PhongMaterial( Color.WHITE,
                          new Image("File:ZombieHouse/Resources/images/checker_plain_2.jpg", 128, 128, true, true, true), // Diffuse
                          new Image("File:ZombieHouse/Resources/images/checker_specular.jpg", 128, 128, true, true, false), // Specular
                          new Image("File:ZombieHouse/Resources/images/checker_normal.jpg", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illumination
                  ),
                  new PhongMaterial( Color.WHITE,
                          new Image("File:ZombieHouse/Resources/images/red_carpet.png", 128, 128, true, true, true), // Diffuse
                          new Image("File:ZombieHouse/Resources/images/carpet_bump.jpg", 128, 128, true, true, false), // Specular
                          new Image("File:ZombieHouse/Resources/images/carped_normal.jpg", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illumination
                  ),
                  new PhongMaterial( Color.WHITE,
                          new Image("File:ZombieHouse/Resources/images/wood_planks.jpg", 128, 128, true, true, true), // Diffuse
                          new Image("File:ZombieHouse/Resources/images/wood_planks_specular.jpg", 128, 128, true, true, false), // Specular
                          new Image("File:ZombieHouse/Resources/images/wood_planks_normal.jpg", 128, 128, true, true, false), // Normal/Bump
                          null) // Self-Illumination
          };

}
