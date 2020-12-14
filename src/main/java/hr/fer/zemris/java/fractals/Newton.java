package hr.fer.zemris.java.fractals;


/**
 * Same as newton parallel but with pre-set fixed
 * single thread and single track
 */
public class Newton {
  public static void main(String[] args) {
    NewtonParallel.main(new String[]{"-W=1", "-T=1"});
  }
}
