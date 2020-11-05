package hr.fer.oprpp1.hw01;

import hr.fer.oprpp1.hw01.ComplexNumber;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static java.lang.Math.PI;
import static org.junit.jupiter.api.Assertions.*;

class ComplexNumberTest {

  @Test
  public void parsingCorrectly() {
    var c = ComplexNumber.parse("2.3+3.4i");
    assertEquals(new ComplexNumber(2.3, 3.4), c);

    c = ComplexNumber.parse("2-4.0i");
    assertEquals(new ComplexNumber(2, -4), c);

    c = ComplexNumber.parse("-4589-4.2i");
    assertEquals(new ComplexNumber(-4589, -4.2), c);

    c = ComplexNumber.parse("-4e10-4.2e5I");
    assertEquals(new ComplexNumber(-4e10, -4.2e5), c);

    c = ComplexNumber.parse("I");
    assertEquals(new ComplexNumber(0, 1), c);

    c = ComplexNumber.parse("+1");
    assertEquals(new ComplexNumber(1, 0), c);

    c = ComplexNumber.parse("-I");
    assertEquals(new ComplexNumber(0, -1), c);

    c = ComplexNumber.parse("-1");
    assertEquals(new ComplexNumber(-1, 0), c);

    assertThrows(IllegalArgumentException.class, () -> {
      ComplexNumber.parse("");
    });
  }

  @Test
  public void testFromMagnitudeAndAngle() {
    var c = ComplexNumber.fromMagnitudeAndAngle(Math.sqrt(2), PI / 4);
    assertEquals(1, c.getReal(), 0.001);
    assertEquals(1, c.getImaginary(), 0.001);
  }

  @Test
  public void addition() {
    var c1 = new ComplexNumber(1,1);
    var c2 = new ComplexNumber(2,2);
    assertEquals(new ComplexNumber(3,3), c1.add(c2));
  }

  @Test
  public void subtraction() {
    var c1 = new ComplexNumber(1,1);
    var c2 = new ComplexNumber(2,2);
    assertEquals(new ComplexNumber(-1,-1), c1.sub(c2));
  }

  @Test
  public void multiplication() {
    var c1 = new ComplexNumber(1,4);
    var c2 = new ComplexNumber(5,1);
    var expected = new ComplexNumber(1,21);
    var actual = c1.mul(c2);
    assertEquals(expected.getReal(), actual.getReal(), 0.001);
    assertEquals(expected.getImaginary(), actual.getImaginary(), 0.001);
  }

  @Test
  public void division() {
    var c1 = ComplexNumber.fromMagnitudeAndAngle(4, PI / 4);
    var c2 = ComplexNumber.fromMagnitudeAndAngle(2, PI / 4);
    var expected = ComplexNumber.fromMagnitudeAndAngle(2, 0);
    var actual = c1.div(c2);
    assertEquals(expected.getMagnitude(), actual.getMagnitude(), 0.001);
    assertEquals(expected.getAngle(), actual.getAngle(), 0.001);
  }

  @Test
  public void raisingToPower() {
    var c1 = ComplexNumber.fromMagnitudeAndAngle(4, PI / 4);
    var expected = ComplexNumber.fromMagnitudeAndAngle(16, PI / 2);
    var actual = c1.pow(2);
    assertEquals(expected.getMagnitude(), actual.getMagnitude(), 0.001);
    assertEquals(expected.getAngle(), actual.getAngle(), 0.001);
  }

  @Test
  public void takingRoot() {
    var c1 = ComplexNumber.fromMagnitudeAndAngle(16, PI / 4);
    var expected = new ComplexNumber[] {
      ComplexNumber.fromMagnitudeAndAngle(2, PI / 4 / 4),
      ComplexNumber.fromMagnitudeAndAngle(2, (PI / 4 + 2 * PI) / 4),
      ComplexNumber.fromMagnitudeAndAngle(2, (PI / 4 + 4 * PI) / 4),
      ComplexNumber.fromMagnitudeAndAngle(2, (PI / 4 + 6 * PI) / 4),
    };
    var actual = c1.root(4);
    int i = 0;
    for(var root : expected) {
      assertEquals(root.getMagnitude(), actual[i].getMagnitude(), 0.001);
      assertEquals(root.getAngle(), actual[i++].getAngle(), 0.001);
    }
  }

}