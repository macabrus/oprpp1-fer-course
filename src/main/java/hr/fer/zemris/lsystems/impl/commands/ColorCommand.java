package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

import java.awt.*;

public class ColorCommand implements Command {

  private Color color;

  public ColorCommand(Color color) {
    this.color = color;
  }

  @Override
  public void execute(Context ctx, Painter painter) {
    ctx.getCurrentState().setColor(color);
  }

}
