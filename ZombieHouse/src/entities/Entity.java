package entities;
// Aakash

import javafx.scene.Node;

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
  public Node[] mesh; // Seems reasonable to expect every entity to have a mesh/body.
}

