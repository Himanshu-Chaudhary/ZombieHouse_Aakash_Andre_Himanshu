package general;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;

public class MyParticle
{
  Shape3D mesh = new Sphere(Math.random()+0.5,5);

  private double x_velocity;
  private double y_velocity;
  private double z_velocity;

  private double x_position;
  public double y_position;
  private double z_position;

  private PhongMaterial material;

  public MyParticle(double x, double y, double z, double xv, double yv, double zv)
  {
    x_position = x;
    y_position = y;
    z_position = z;
    x_velocity = xv;
    y_velocity = yv;
    z_velocity = zv;
    this.material = new PhongMaterial(Color.color(0,Math.random(),0));
    //this.material.setSpecularColor( Color.WHITE );
    //his.material.setSpecularPower(0.1);
    this.mesh.setMaterial(this.material);
  }

  void update()
  {
      x_position += x_velocity;
      y_position += y_velocity;
      z_position += z_velocity;
      y_velocity -= 0.05;

      this.mesh.setTranslateX(x_position);
      this.mesh.setTranslateY(y_position);
      this.mesh.setTranslateZ(z_position);
  }
}
