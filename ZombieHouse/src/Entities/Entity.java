package Entities;
// Aakash

import javafx.scene.shape.Shape3D;

/**
 * This class contains all the fields that are common in the objects.
 * The position of the objects
 */

public abstract class Entity
{
  public double positionX;
  public double positionY;
  public double positionZ;
  public double direction;
  public Shape3D mesh; // Seems reasonable to expect every entity to have a mesh/body.
}
