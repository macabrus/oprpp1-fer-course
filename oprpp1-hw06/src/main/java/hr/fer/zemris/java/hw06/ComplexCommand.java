package hr.fer.zemris.java.hw06;

import hr.fer.oprpp1.Complex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComplexCommand implements ShellCommand{

  private List<Complex> nums = new ArrayList<>();
  private CommandParser parser;

  public ComplexCommand(CommandParser parser) {
    this.parser = parser;
  }

  @Override
  public ShellStatus executeCommand(Environment env, String arguments) {
    var res = parser.parse(arguments);
    nums.add(new Complex(res[0] == null ? 0 : Double.parseDouble(res[0]), res[1] == null ? 0 : Double.parseDouble(res[1])));
    return ShellStatus.CONTINUE;
  }

  public List<Complex> getParsedNumbers() {
    return nums;
  }
}
