package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

public class ScaleCommand implements Command {

  private double factor;

  public ScaleCommand(double factor) {
    this.factor = factor;
  }

  @Override
  public void execute(Context ctx, Painter painter) {
    ctx.getCurrentState().setScaleFactor(factor);
  }

}
