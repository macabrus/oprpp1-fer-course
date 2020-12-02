package hr.fer.zemris.java.hw06.shell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Creates argument parser objects for commands.
 * Abstracts object instantiation from consumers (dependency injection)
 */
public class CommandParserFactory {

  private static final CommandParserFactory INSTANCE = new CommandParserFactory();

  private CommandParserFactory() {}

  /**
   * @return singleton instance of this factory
   */
  public CommandParserFactory getInstance() {
    return INSTANCE;
  }

  /**
   * Constructs anonymous default arg parser for this homework.
   *
   * @return a default CLI args parser
   *         that parses words separated by space
   *         but keeping spaces a part of same word
   *         in quotes
   */
  public CommandParser getDefault() {
    return new CommandParser() {
      private final Pattern[] expressions = new Pattern[]{
        Pattern.compile("\\s*(?<target>\\w+)\\s*"),
        Pattern.compile("\\s*\"(?<target>[^\"]*)\"\\s*")
      };
      @Override
      public String[] parse(String line) {
        List<String> parsedArgs = new ArrayList<>();
        while (line.length() > 0) {
          for (Pattern p : expressions) {
            Matcher m = p.matcher(line);
            if (m.find() && m.start() == 0) {
              parsedArgs.add(m.group("target"));
              line = line.replaceFirst(p.pattern(), "");
            }
          }
          if (line.length() == 0)
            break;
        }
        return parsedArgs.toArray(new String[0]);
      }
    };
  }

  public static void main(String[] args) {
    // example usage
    System.out.println(Arrays.toString(INSTANCE.getDefault().parse("this is an \"fdg \\ /d/dfg df g\" command")));
  }
}
