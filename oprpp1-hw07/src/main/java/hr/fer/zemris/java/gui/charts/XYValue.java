package hr.fer.zemris.java.gui.charts;

import java.util.Arrays;
import java.util.Objects;

public class XYValue {
  int x;
  int y;

  public XYValue(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    XYValue that = (XYValue) o;
    return x == that.x &&
      y == that.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }
}
