package hr.fer.zemris.java.gui.layouts;

import java.util.Arrays;
import java.util.Objects;

public class RCPosition {
  int row;
  int col;

  public RCPosition(int r, int c) {
    this.row = r;
    this.col = c;
  }

  public static RCPosition parse(String expr) {
    String[] coordinates = Arrays.stream(expr.split(",")).map(String::trim).toArray(String[]::new);
    if (coordinates.length != 2)
      throw new IllegalArgumentException("Can't parse this expression into an instance of " + RCPosition.class.getName());
    return new RCPosition(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RCPosition that = (RCPosition) o;
    return row == that.row &&
      col == that.col;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col);
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }
}
