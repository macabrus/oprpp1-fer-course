package hr.fer.oprpp1;

public class ComplexPolynomial {

  private final Complex[] factors;

  public ComplexPolynomial(Complex ...factors) {
    this.factors = factors;
  }

  // returns order of this polynom; eg. For (7+2i)z^3+2z^2+5z+1 returns 3
  public short order() {
    for (int i = factors.length - 1; i >= 0; i--)
      if (factors[i] != null)
        return (short) i;
    return 0;
  }

  // computes a new polynomial this*p
  public ComplexPolynomial multiply(ComplexPolynomial p) {
    var product = new Complex[factors.length + p.factors.length];
    var max = 0;
    for (int i = 0; i < factors.length; i++) {
      for (int j = 0; j < p.factors.length; j++) {
        if (factors[i] == null || p.factors[j] == null)
          continue;
        var index = i + j;
        if (product[index] == null) {
          product[index] = factors[i].mul(p.factors[j]);
        } else {
          product[index] = product[index].add(factors[i].mul(p.factors[j]));
        }
      }
    }
    return new ComplexPolynomial(product);
  }

  // computes first derivative of this polynomial; for example, for
  // (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
  public ComplexPolynomial derive() {
    var derivative = new Complex[factors.length - 1];
    for (int i = 1; i < factors.length; i++) {
      if (factors[i] == null)
        continue;
      derivative[i-1] = factors[i].mul(new Complex(i, 0));
    }
    return new ComplexPolynomial(derivative);
  }

  // computes polynomial value at given point z
  public Complex apply(Complex z) {
    var cumSum = Complex.ZERO;
    for (int i = 0; i < factors.length ; i++) {
      if (factors[i] == null)
        continue;
      cumSum = cumSum.add(z.pow(i).mul(factors[i]));
    }
    return cumSum;
  }

  @Override
  public String toString() {
    var sb = new StringBuilder("f(z) = ");
    for (int i = factors.length - 1; i >= 0; i--) {
      if (factors[i] == null)
        continue;
      if (i == 0)
        sb.append(factors[i]);
      else
        sb.append("(%s) * z^%s".formatted(factors[i], i));
      if (i != 0)
        sb.append(" + ");
    }
    return sb.toString();
  }
}
