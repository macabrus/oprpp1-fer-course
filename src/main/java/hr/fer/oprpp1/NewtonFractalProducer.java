package hr.fer.oprpp1;

import hr.fer.zemris.java.fractals.mandelbrot.Mandelbrot;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class NewtonFractalProducer implements IFractalProducer {

  private final ComplexRootedPolynomial crp;
  private final ComplexPolynomial cpDerivative;
  private final double convergenceThreshold;
  private ComplexPolynomial cp;
  private double convergenceThresholdSquared;
  private double rootThreshold;
  private int maxIter;
  private int workers;
  private int tracks;

  public NewtonFractalProducer(ComplexRootedPolynomial crp, double convergenceThreshold, double rootThreshold) {
    this.crp = crp;
    this.cp = crp.toComplexPolynom();
    this.cpDerivative = this.cp.derive();
    this.convergenceThresholdSquared = convergenceThreshold * convergenceThreshold; // 1e-3
    this.convergenceThreshold = convergenceThreshold;
    this.rootThreshold = rootThreshold;
    this.maxIter = 64;
  }

  public void setNumWorkers(int num) {
    this.workers = num;
  }

  public void setNumTracks(int num) {
    this.tracks = num;
  }

  @Override
  public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height, long requestNo,
                      IFractalResultObserver obs, AtomicBoolean cancel) {

    short[] data = new short[width * height];
    int brojYPoTraci = height / tracks;

    var q = new LinkedBlockingQueue<NewtonFractalJob>();

    Thread[] pool = new Thread[workers];
    for (int i = 0; i < pool.length; i ++) {
      pool[i] = new Thread(() -> {
        while (true){
          NewtonFractalJob job;
          try {
            job = q.take();
            if (job == NewtonFractalJob.NO_JOB){
              System.out.println(Thread.currentThread().getId() + " umire");
              break;
            }
          } catch (InterruptedException e) {
            continue;
          }
          job.run();
        }
      });
    }

    for(int i = 0; i < pool.length; i++) {
      pool[i].start();
    }

    for(int i = 0; i < tracks; i++) {
      int yMin = i * brojYPoTraci;
      int yMax = (i+1)*brojYPoTraci-1;
      System.out.println("from " + yMin + " to " + yMax);
      if(i==tracks-1) {
        yMax = height-1;
      }

      var job = new NewtonFractalJob(reMin, reMax, imMin, imMax, width, height, yMin, yMax,
                                  cp.order() + 1, data, cancel, crp, convergenceThreshold,
                                      rootThreshold);
      while(true) {
        try {
          q.put(job);
          break;
        } catch (InterruptedException e) {
        }
      }
    }
    for(int i = 0; i < pool.length; i++) {
      while(true) {
        try {
          q.put(NewtonFractalJob.NO_JOB);
          break;
        } catch (InterruptedException e) {
        }
      }
    }

    for(int i = 0; i < pool.length; i++) {
      while(true) {
        try {
          pool[i].join();
          break;
        } catch (InterruptedException e) {
        }
      }
    }

    System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
    obs.acceptResult(data, (short) (cp.order() + 1), requestNo);
  }
}
