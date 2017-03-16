package general;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

public class MaterialsManager
{
  public static final PhongMaterial[] FLOOR_MATERIALS = new PhongMaterial[]
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
                          new Image("File:ZombieHouse/src/images/carpet.png", 128, 128, true, true, true), // Diffuse
                          new Image("File:ZombieHouse/src/images/carpet_bump.jpg", 128, 128, true, true, false), // Specular
                          new Image("File:ZombieHouse/src/images/carped_normal.jpg", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illumination
                  ),
                  new PhongMaterial( Color.WHITE, null, null, null, null )
          };

  public static final PhongMaterial[] WALL_MATERIALS = new PhongMaterial[]
          {
                  new PhongMaterial( Color.WHITE,
                          new Image("File:ZombieHouse/src/images/brick_compress_test.png", 400, 400, true, true, false), // Diffuse
                          new Image("File:ZombieHouse/src/images/brick_compress_test_s.png", 128, 128, true, true, false), // Specular
                          null, //new Image("File:ZombieHouse/src/images/brick_compress_test_n.png", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illumination
                  ),
                  new PhongMaterial( Color.WHITE,
                          new Image("File:ZombieHouse/src/images/wood_compress.png", 400, 400, true, true, false), // Diffuse
                          null, //new Image("File:ZombieHouse/src/images/checker_specular.jpg", 128, 128, true, true, false), // Specular
                          null, //new Image("File:ZombieHouse/src/images/checker_normal.jpg", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illuminationw
                  ),
                  new PhongMaterial( Color.WHITE,
                          new Image("File:ZombieHouse/src/images/tile_compress.png", 400, 400, true, true, false), // Diffuse
                          null, //new Image("File:ZombieHouse/src/images/checker_specular.jpg", 128, 128, true, true, false), // Specular
                          null, //new Image("File:ZombieHouse/src/images/checker_normal.jpg", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illumination
                  ),
                  new PhongMaterial( Color.WHITE,
                          new Image("File:ZombieHouse/src/images/wallpaper_compress.png", 400, 400, true, true, false), // Diffuse
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
                          new Image("File:ZombieHouse/src/images/carpet.png", 128, 128, true, true, true), // Diffuse
                          new Image("File:ZombieHouse/src/images/carpet_bump.jpg", 128, 128, true, true, false), // Specular
                          new Image("File:ZombieHouse/src/images/carped_normal.jpg", 128, 128, true, true, false), // Normal/Bump
                          null // Self-Illumination
                  ),
                  new PhongMaterial( Color.WHITE, null, null, null, null )
          };

}
