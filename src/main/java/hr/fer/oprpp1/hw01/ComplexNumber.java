package hr.fer.oprpp1.hw01;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.PI;

/**
 * An immutable class representation of complex number.
 */
public class ComplexNumber {

  /**
   * Constructs complex number with default values (0,0).
   */
  public ComplexNumber() {
    re = 0;
    im = 0;
  }

  /**
   * Constructs complex number from passed parameters.
   * @param real a real part of complex number
   * @param imaginary an imaginary part of complex number
   */
  public ComplexNumber(double real, double imaginary) {
    this.re = real;
    this.im = imaginary;
  }


  /**
   * Constructs complex number containing only real part.
   * @param real a real part of complex number
   * @return an instance of complex number
   */
  public static ComplexNumber fromReal(double real) {
    return new ComplexNumber(real, 0);
  }

  /**
   * Constructs complex number containing only imaginary part.
   * @param imaginary an imaginary part of complex number
   * @return an instance of complex number
   */
  public static ComplexNumber fromImaginary(double imaginary) {
    return new ComplexNumber(0, imaginary);
  }

  /**
   * Constructs complex number from polar coordinates.
   * @param magnitude vector length
   * @param angle vector direction in radians
   * @return an instance of complex number
   */
  public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
    return new ComplexNumber(Math.cos(angle) * magnitude, Math.sin(angle) * magnitude);
  }

  /**
   * Parses {@link String} into new instance of complex number.
   * @param c {@link String} representation number to be parsed as new complex number.
   * @return an instance of complex number
   */
  public static ComplexNumber parse(String c) {
    Matcher m = regex.matcher(c);
    IllegalArgumentException ex = new IllegalArgumentException(
      String.format("Expression \"%s\" couldn't be parsed as %s", c, ComplexNumber.class)
    );
    if (!m.find())
      throw ex;
    String re = m.group("re");
    String im = m.group("im");
    String imstr = m.group("imstr");
    String imsgn = m.group("imsgn");
    if (imstr == null && re == null)
      throw ex;

    double reNum = re != null ? Double.parseDouble(re) : 0;
    double imNum = imstr == null ? 0 : (im == null ? ("-".equals(imsgn) ? -1 : 1) : Double.parseDouble(im) * ("-".equals(imsgn) ? -1 : 1));
    return new ComplexNumber(reNum, imNum);
  }

  /**
   * Extracts real part of complex number (stored as double).
   * @return a real part of complex number
   */
  public double getReal() {
    return re;
  }


  /**
   * Extracts imaginary part of complex number (stored as double).
   * @return an imaginary part of complex number
   */
  public double getImaginary() {
    return im;
  }


  /**
   * Computes length of complex vector.
   * @return a length of complex vector
   */
  public double getMagnitude() {
    return Math.sqrt(re * re + im * im);
  }

  /**
   * Computes angle of complex vector (in radians).
   * @return an angle of complex vector.
   */
  public double getAngle() {
    return Math.atan2(im, re);
  }

  /**
   * Sums two complex numbers into a new instance.
   * @param c second operand
   * @return a new instance equal to sum of added numbers
   */
  public ComplexNumber add(ComplexNumber c) {
    return new ComplexNumber(re + c.getReal(), im + c.getImaginary());
  }

  /**
   * Subtracts two complex numbers into a new instance.
   * @param c second operand
   * @return a new instance equal to difference of subtracted numbers
   */
  public ComplexNumber sub(ComplexNumber c) {
    return new ComplexNumber(re - c.getReal(), im - c.getImaginary());
  }

  /**
   * Multiplies two complex numbers into a new instance.
   * @param c second operand
   * @return a new instance equal to product of multiplied numbers
   */
  public ComplexNumber mul(ComplexNumber c) {
    return new ComplexNumber(re * c.getReal() - im * c.getImaginary(), re * c.getImaginary() + im * c.getReal());
  }

  /**
   * Divides two complex numbers into a new instance.
   * @param c second operand
   * @return a new instance equal to ratio of operands
   */
  public ComplexNumber div(ComplexNumber c) {
    return new ComplexNumber((re * c.getReal() + im * c.getImaginary()) / Math.pow(c.getMagnitude(),2),
      (im * c.getReal() - re * c.getImaginary()) / Math.pow(c.getMagnitude(), 2));
  }

  /**
   * Raises a complex number to a given power.
   * @param n power to raise to
   * @return a new instance of complex number raised to passed power
   */
  public ComplexNumber pow(int n) {
    return ComplexNumber.fromMagnitudeAndAngle(Math.pow(getMagnitude(), n), getAngle() * n);
  }

  /**
   * Takes n-th root of a complex number.
   * @param n root to take of a complex number
   * @return an array of calculated n-th roots of given number
   */
  public ComplexNumber[] root(int n) {
    double computedMagnitude = Math.pow(getMagnitude(), 1. / n);
    double computedAngle = getAngle();
    var roots = new ComplexNumber[n];
    for (int k = 0; k < n; k++)
      roots[k] = ComplexNumber.fromMagnitudeAndAngle(computedMagnitude, (computedAngle + 2 * k * PI) / n);
    return roots;
  }

  @Override
  public String toString() {
    return re + " " + im + "i";
  }

  /**
   * Numbers are equal if both their complex and real parts are equal.
   * @param o object to compare against.
   * @return true if equal
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ComplexNumber that = (ComplexNumber) o;
    return Double.compare(that.re, re) == 0 &&
      Double.compare(that.im, im) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(re, im);
  }

  private final double re;
  private final double im;

  private static final Pattern regex = Pattern.compile(
    "^(?:(?<re>[-+]?\\d+(?:(?:\\.\\d+)?(?:e[-+]?\\d+)?)?)?)?" +
    "(?<imstr>(?<imsgn>[-+])?(?<im>\\d+(?:(?:\\.\\d+)?(?:e[-+]?\\d+)?)?)?[iI])?$"
  );
}
