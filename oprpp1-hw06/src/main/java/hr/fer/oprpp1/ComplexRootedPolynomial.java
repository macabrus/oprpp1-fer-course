package hr.fer.oprpp1;

import java.util.Arrays;

public class ComplexRootedPolynomial {

  private final Complex[] roots;
  private final Complex constant;

  public ComplexRootedPolynomial(Complex constant, Complex... roots) {
    if (roots == null || roots.length == 0) {
      throw new IllegalArgumentException("Polynomial must have at least one root");
    }
    this.roots = roots;
    this.constant = constant;
  }

  // computes polynomial value at given point z
  public Complex apply(Complex z) {
    Complex accumulated = constant;
    for (Complex c : roots)
      accumulated = accumulated.mul(z.sub(c));
    return accumulated;
  }

  // converts this representation to ComplexPolynomial type
  public ComplexPolynomial toComplexPolynom() {
    var cpResult = new ComplexPolynomial(constant);
    for (var c : roots) {
      cpResult = cpResult.multiply(new ComplexPolynomial(c.negate(), Complex.ONE));
    }
    return cpResult;
  }

  @Override
  public String toString() {
    var sb = new StringBuilder("f(z) = (%s) * ".formatted(constant));
    for (int i = 0; i < roots.length; i++) {
      sb.append("(z - (%s))".formatted(roots[i]));
      if (i != roots.length - 1)
        sb.append(" * ");
    }
    return sb.toString();
  }

  // finds index of closest root for given complex number z that is within
  // threshold; if there is no such root, returns -1
  // first root has index 0, second index 1, etc
  public int indexOfClosestRootFor(Complex z, double threshold) {
    int idx = -1;
    double currentMin = Double.POSITIVE_INFINITY;
    for (int i = 0; i < roots.length; i ++){
      var tmp = roots[i].sub(z).getMagnitudeSquared();
      if (tmp < currentMin && tmp < threshold * threshold) {
        idx = i;
        currentMin = tmp;
      }
    }
    return idx;
  }

  public Complex[] toArray() {
    return Arrays.copyOf(roots, roots.length);
  }


}
