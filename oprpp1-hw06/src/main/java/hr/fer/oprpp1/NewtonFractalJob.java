package hr.fer.oprpp1;

import java.util.concurrent.atomic.AtomicBoolean;

public class NewtonFractalJob implements Runnable{

  private int maxIter;
  private double rootThreshold;
  private double convergenceThresholdSquared;
  private ComplexPolynomial cp;
  private ComplexPolynomial cpDerivative;
  private ComplexRootedPolynomial crp;
  double reMin;
  double reMax;
  double imMin;
  double imMax;
  int width;
  int height;
  int yMin;
  int yMax;
  int m;
  short[] data;
  AtomicBoolean cancel;
  public static NewtonFractalJob NO_JOB = new NewtonFractalJob();

  private NewtonFractalJob() {

  }

  public NewtonFractalJob(double reMin, double reMax, double imMin,
                          double imMax, int width, int height, int yMin, int yMax,
                          int m, short[] data, AtomicBoolean cancel,
                          // additional params
                          ComplexRootedPolynomial crp, double convergenceThreshold, double rootThreshold) {
    super();
    this.reMin = reMin;
    this.reMax = reMax;
    this.imMin = imMin;
    this.imMax = imMax;
    this.width = width;
    this.height = height;
    this.yMin = yMin;
    this.yMax = yMax;
    this.m = m;
    this.data = data;
    this.cancel = cancel;
    // additional params
    this.cp = crp.toComplexPolynom();
    this.cpDerivative = this.cp.derive();
    this.crp = crp;
    this.convergenceThresholdSquared = convergenceThreshold * convergenceThreshold; // 1e-3
    this.rootThreshold = rootThreshold;
    this.maxIter = 64;
  }

  @Override
  public void run() {
//    System.out.println("Thread " + Thread.currentThread().getId() + " my yMin is " + yMin);
    var offset = yMin * width;
    for (int y = yMin; y <= yMax && !cancel.get(); y ++) {
      for (int x = 0; x < width; x ++) {
        double cre = (double)x / ((double)width - 1.0D) * (reMax - reMin) + reMin;
        double cim = (double)(height - 1 - y) / ((double)height - 1.0D) * (imMax - imMin) + imMin;
        var zn = new Complex(cre, cim);
        Complex znOld;
        var iter = 0;
        do {
          znOld = zn;
          zn = zn.sub(cp.apply(zn).div(cpDerivative.apply(zn)));
          iter++;
        } while (zn.sub(znOld).getMagnitudeSquared() > convergenceThresholdSquared && iter < maxIter);
        int index = crp.indexOfClosestRootFor(zn, rootThreshold);
        data[offset++] = (short) (index + 1);
      }
    }
  }
}
