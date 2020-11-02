package hr.fer.oprpp1.math;

import org.junit.jupiter.api.Test;

import static java.lang.Math.*;
import static org.junit.jupiter.api.Assertions.*;

class Vector2DTest {

  @Test
  void testGetX() {
    var vec = new Vector2D(2,3);
    assertEquals(2, vec.getX());
  }

  @Test
  void getY() {
    var vec = new Vector2D(2,3);
    assertEquals(3, vec.getY());
  }

  @Test
  void add() {
    var v1 = new Vector2D(1,1);
    v1.add(new Vector2D(4,5));
    assertEquals(5, v1.getX());
    assertEquals(6, v1.getY());
  }

  @Test
  void added() {
    var v1 = new Vector2D(1,1);
    var v2 = v1.added(new Vector2D(4,5));
    assertEquals(1, v1.getX());
    assertEquals(1, v1.getY());
    assertEquals(5, v2.getX());
    assertEquals(6, v2.getY());
  }

  @Test
  void rotate() {
    var v1 = new Vector2D(0, 1);
    v1.rotate(PI / 2);
    assertEquals(-1, v1.getX() , 0.001);
    assertEquals(0, v1.getY(), 0.001);
  }

  @Test
  void rotated() {
    var northeast = new Vector2D(1,1);
    assertEquals(1, northeast.getX(), 0.001);
    assertEquals(1, northeast.getY(), 0.001);

    var north = northeast.rotated(PI/4);
    assertEquals(0, north.getX(), 0.001);
    assertEquals(sqrt(2), north.getY(), 0.001);

    var northwest = north.rotated(PI/4);
    assertEquals(-1, northwest.getX(), 0.001);
    assertEquals(1, northwest.getY(), 0.001);

    var west = northwest.rotated(PI/4);
    assertEquals(-sqrt(2), west.getX(), 0.001);
    assertEquals(0, west.getY(), 0.001);

    var south = west.rotated(PI/2);
    assertEquals(0, south.getX(), 0.001);
    assertEquals(-sqrt(2), south.getY(), 0.001);
  }

  @Test
  void scale() {
    var v1 = new Vector2D(1, 1);
    v1.scale(4);
    assertEquals(4, v1.getX());
    assertEquals(4, v1.getY());
  }

  @Test
  void scaled() {
    var v1 = new Vector2D(1,1);
    var v2 = v1.scaled(2);
    assertEquals(1, v1.getX());
    assertEquals(1, v1.getY());
    assertEquals(2, v2.getX());
    assertEquals(2, v2.getY());
  }

  @Test
  void copy() {
    var v1 = new Vector2D(100, 1434);
    var cpy = v1.copy();
    assertEquals(v1.getX(), cpy.getX());
    assertEquals(v1.getY(), cpy.getY());
  }
}