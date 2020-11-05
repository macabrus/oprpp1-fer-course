package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

public class SkipCommand implements Command {

  private double step;

  public SkipCommand(double step) {
    this.step = step;
  }

  @Override
  public void execute(Context ctx, Painter painter) {
    TurtleState turtle = ctx.getCurrentState();
    // current turtle position and rotation
    var currentPosition = turtle.getPosition();
    double x0 = currentPosition.getX();
    double y0 = currentPosition.getY();
    // scaling vector with step size
    var delta = currentPosition.getUnitVector().scaled(step);
    // rotating difference vector to correct orientation
    delta.rotate(turtle.getOrientation().getAngle());
    // new vector = old position + new delta vector
    var newPosition = currentPosition.added(delta);
    double x1 = newPosition.getX();
    double y1 = newPosition.getY();
    // Modifying turtle's state to new value
    turtle.setPosition(newPosition);
    // new rotation is delta vector's rotation
    turtle.setOrientation(delta.getUnitVector());
  }
}
