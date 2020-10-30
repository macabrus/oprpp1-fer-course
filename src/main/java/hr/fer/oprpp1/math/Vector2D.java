package hr.fer.oprpp1.math;

public class Vector2D {

  private double x;
  private double y;

  public Vector2D(double x, double y) {

    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public void add(Vector2D offset) {
    x += offset.getX();
    y += offset.getY();
  }

  public Vector2D added(Vector2D offset) {
    return new Vector2D(x + offset.getX(), y + offset.getY());
  }

  public void rotate(double angle) {
    double newAngle = Math.atan2(y, x);
    double magnitude = Math.hypot(x, y);
    x = Math.cos(newAngle) * magnitude;
    y = Math.sin(newAngle) * magnitude;
  }

  public Vector2D rotated(double angle) {
    var vec = copy();
    vec.rotate(angle);
    return vec;
  }

  public void scale(double scaler) {
    x *= scaler;
    y *= scaler;
  }

  public Vector2D scaled(double scaler) {
    return new Vector2D(x * scaler, y * scaler);
  }

  public Vector2D copy() {
    return new Vector2D(x, y);
  }

}
