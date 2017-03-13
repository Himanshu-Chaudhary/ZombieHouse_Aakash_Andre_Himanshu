package general;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;

public class MyParticle
{
  Shape3D mesh = new Sphere(Math.random()/2,10); //+0.5,5);

  private double x_velocity;
  private double y_velocity;
  private double z_velocity;

  private double x_position;
  public double y_position;
  private double z_position;

  private PhongMaterial material;
  Image illumination = new Image(getClass().getResourceAsStream("energymap.png"));
  private boolean flicker = false;

  public MyParticle(double x, double y, double z, double xv, double yv, double zv)
  {
    x_position = x;
    y_position = y;
    z_position = z;
    x_velocity = xv;
    y_velocity = yv;
    z_velocity = zv;
    double intensity = Math.random();
    this.material = new PhongMaterial(Color.color(0,intensity,intensity));
    this.material.setSelfIlluminationMap(illumination);
    //this.material.setSpecularColor( Color.WHITE );
    //his.material.setSpecularPower(0.1);
    this.mesh.setMaterial(this.material);
  }

  void update()
  {
      x_position += x_velocity/5;
      y_position += y_velocity/5;
      z_position += z_velocity/5;
      y_velocity -= 0.01;

      this.mesh.setTranslateX(x_position);
      this.mesh.setTranslateY(y_position);
      this.mesh.setTranslateZ(z_position);
  }
}
