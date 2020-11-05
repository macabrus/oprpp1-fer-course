package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

public class DrawCommand implements Command {

  private double step;

  public DrawCommand(double step) {
    this.step = step;
  }

  @Override
  public void execute(Context ctx, Painter painter) {
    var turtle = ctx.getCurrentState();
    double x0 = turtle.getPosition().getX();
    double y0 = turtle.getPosition().getY();
    var delta = turtle.getOrientation().scaled(step * turtle.getScaleFactor());
    turtle.setPosition(turtle.getPosition().added(delta));
    double x1 = turtle.getPosition().getX();
    double y1 = turtle.getPosition().getY();
    System.out.println("TURTLE COLOR IS " + turtle.getColor());
    painter.drawLine(x0, y0, x1, y1, turtle.getColor(), 1);
  }

}
