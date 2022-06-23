package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.oprpp1.math.Vector2D;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

public class RotateCommand implements Command {

  private Vector2D angle;

  public RotateCommand(Vector2D angle) {
    this.angle = angle;
  }

  @Override
  public void execute(Context ctx, Painter painter) {
    TurtleState turtle = ctx.getCurrentState();
    turtle.setOrientation(turtle.getOrientation().rotated(angle.getAngle()));
  }

}
