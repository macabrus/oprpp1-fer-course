package hr.fer.oprpp1;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;

public class Main {
  public static void main(String[] args) {
    ComplexRootedPolynomial crp = new ComplexRootedPolynomial(
      Complex.ONE,
      Complex.ONE,
      Complex.ONE_NEG,
      Complex.IM,
      Complex.IM_NEG
    );
    System.out.println(crp);
    ComplexPolynomial cp = crp.toComplexPolynom();
    System.out.println(cp);
    System.out.println(cp.derive());
    FractalViewer.show(new NewtonFractalProducer(crp, 0.001, 0.002));
  }
}
