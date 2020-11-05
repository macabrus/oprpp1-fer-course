package hr.fer.zemris.lsystems.impl;

import hr.fer.oprpp1.math.Vector2D;

import java.awt.*;

public class TurtleState {
  private Vector2D position;
  private Vector2D orientation;
  private Color color;
  private double scale;

  public TurtleState(Vector2D position, Vector2D orientation, Color color, double scale) {
    this.position = position;
    this.orientation = orientation;
    this.color = color;
    this.scale = scale;
  }

  public Vector2D getPosition() {
    return position.copy();
  }

  public Vector2D getOrientation() {
    return orientation.copy();
  }

  public Color getColor() {
    return color;
  }

  public double getScaleFactor() {
    return scale;
  }

  public void setPosition(Vector2D position) {
    this.position = position;
  }

  public void setOrientation(Vector2D orientation) {
    this.orientation = orientation;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public void setScaleFactor(double scale) {
    this.scale = scale;
  }

  public TurtleState copy() {
    return new TurtleState(position, orientation, color, scale);
  }
}
