package hr.fer.zemris.lsystems.impl;

import hr.fer.oprpp1.custom.collections.Dictionary;
import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.commands.*;

import java.awt.*;
import java.util.Scanner;

import static java.lang.StrictMath.PI;
import static java.lang.StrictMath.pow;

class LSystemImpl implements LSystem {

  private Dictionary<Character, String> productions = new Dictionary<>();
  private Dictionary<Character, Command> commands = new Dictionary<>();

  private double unitLength = 0.1;
  private double unitLengthDegreeScaler = 1;
  private Vector2D origin = new Vector2D(0, 0);
  private double angle = 0;
  private String axiom = "";


  public void setUnitLength(double v) {
    unitLength = v;
  }

  public void setOrigin(double v, double v1) {
    origin = new Vector2D(v, v1);
  }

  public void setAngle(double v) {
    this.angle = v * PI / 180;
  }

  public void setAxiom(String s) {
    this.axiom = s;
  }

  public void setUnitLengthDegreeScaler(double v) {
    this.unitLengthDegreeScaler = v;
  }

  public void registerProduction(char c, String s) {
    productions.put(c, s);
  }

  public void registerCommand(char c, String s) {
    var sc = new Scanner(s);
    commands.put(c, switch (sc.next()) {
      case "draw" -> new DrawCommand(sc.nextDouble());
      case "skip" -> new SkipCommand(sc.nextDouble());
      case "scale" -> new ScaleCommand(sc.nextDouble());
      case "rotate" -> new RotateCommand(new Vector2D(1,0).rotated(sc.nextDouble() * PI / 180));
      case "push" -> new PushCommand();
      case "pop" -> new PopCommand();
      case "color" -> new ColorCommand(Color.decode('#' + sc.next()));
      default -> throw new RuntimeException("Invalid command syntax");
    });
  }

  @Override
  public String generate(int i) {
    var axiomCopy = axiom;
    for (int j = 0; j < i; j ++) {
      var sb = new StringBuilder();
      for (char c : axiomCopy.toCharArray()) {
        var production = productions.get(c);
        if (production != null)
          sb.append(production);
        else
          sb.append(c);
      }
      axiomCopy = sb.toString();
    }
    return axiomCopy;
  }

  @Override
  public void draw(int i, Painter painter) {
    var ctx = new Context();
    ctx.pushState(
      new TurtleState(
        origin,
        new Vector2D(1,0).rotated(angle),
        Color.BLACK,
        unitLength * pow(unitLengthDegreeScaler, i)
      )
    );
    var actions = generate(i);
    int j = 0;
    for (char c : actions.toCharArray()) {
      System.out.println("COMMAND IS " + c);
      var cmd = commands.get(c);
      if (cmd != null)
        cmd.execute(ctx, painter);
    }
  }
}
