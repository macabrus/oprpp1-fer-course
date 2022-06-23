package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;

import java.util.Scanner;

public class LSystemBuilderImpl implements LSystemBuilder {

  private LSystemImpl ls = new LSystemImpl();

  @Override
  public LSystemBuilder setUnitLength(double v) {
    ls.setUnitLength(v);
    return this;
  }

  @Override
  public LSystemBuilder setOrigin(double v, double v1) {
    ls.setOrigin(v, v1);
    return this;
  }

  @Override
  public LSystemBuilder setAngle(double v) {
    ls.setAngle(v);
    return this;
  }

  @Override
  public LSystemBuilder setAxiom(String s) {
    ls.setAxiom(s);
    return this;
  }

  @Override
  public LSystemBuilder setUnitLengthDegreeScaler(double v) {
    ls.setUnitLengthDegreeScaler(v);
    return this;
  }

  @Override
  public LSystemBuilder registerProduction(char c, String s) {
    ls.registerProduction(c, s);
    return this;
  }

  @Override
  public LSystemBuilder registerCommand(char c, String s) {
    ls.registerCommand(c, s);
    return this;
  }

  /**
   * In case builder is used multiple times,
   * it has to return new instance each time.
   * @return an instance of LSystem with parameters specified in builder.
   */
  @Override
  public LSystem build() {
    var tmp = ls;
    ls = new LSystemImpl();
    return tmp;
  }

  @Override
  public LSystemBuilder configureFromText(String[] strings) {
    var lsb = new LSystemBuilderImpl();
    for (var s : strings) {
      var sc = new Scanner(s);
      if (sc.hasNext())
        switch (sc.next()) {
          case "axiom" -> lsb.setAxiom(sc.next());
          case "origin" -> lsb.setOrigin(sc.nextDouble(), sc.nextDouble());
          case "angle" -> lsb.setAngle(sc.nextDouble());
          case "unitLength" -> lsb.setUnitLength(sc.nextDouble());
          case "unitLengthDegreeScaler" -> lsb.setUnitLengthDegreeScaler(parseDivision(sc.nextLine()));
          case "command" -> lsb.registerCommand(sc.next().charAt(0), sc.nextLine().strip());
          case "production" -> lsb.registerProduction(sc.next().charAt(0), sc.nextLine().strip());
          default -> throw new RuntimeException("Invalid text configuration syntax");
        }
    }
    return lsb;
  }

  /*---------- UTILITIES ----------*/

  private double parseDivision(String next) {
    var sc = new Scanner(next.replace('/', ' '));
    var first = sc.nextDouble();
    if (sc.hasNextDouble())
      return first / sc.nextDouble();
    return first;
  }

}
