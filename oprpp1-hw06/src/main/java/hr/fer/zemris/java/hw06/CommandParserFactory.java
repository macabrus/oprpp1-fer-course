package hr.fer.zemris.java.hw06;

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
  public static CommandParserFactory getInstance() {
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
        Pattern.compile("\\s*\"(?<target>[^\"]*)\"\\s*"),
        Pattern.compile("\\s*(?<target>[^\\s]+)\\s*"),
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

  /**
   * Simple default prompt parser which just separates first word from rest of line.
   * @return
   */
  public CommandParser getDefaultPromptParser() {
    return line -> {
      line = line.stripLeading();
      var firstSpace = line.indexOf(" ");
      if (firstSpace == -1)
        return new String [] {line, ""};
      return new String[] {line.substring(0, firstSpace), line.substring(firstSpace)};
    };
  }

  // Not really the best solution for parsing complex numbers in the world.
  public CommandParser complexNumberParser() {
    return line -> {
      var parsed = new String[] {"0", "0"}; // assume zeros in the start
      var usd = "(\\d+([.]\\d*)?([eE][+-]?\\d+)?|[.]\\d+([eE][+-]?\\d+)?)"; // matches unsigned double
      var sdb = "[+-]%s".formatted(usd); // matches any double
      var dbl = "[+-]?%s".formatted(usd);
      var imPart = "((%s[iI])|([iI]%s)|([+-]?[iI]))".formatted(dbl, usd); // matches imaginary part
      line = line.replace(" ", "");
      var pattern = Pattern.compile(imPart).matcher(line);
      if (pattern.find()) {
        var parsedIm = pattern.group(1) == null ? "0" : pattern.group(1);
        line = line.replaceFirst(imPart, "");
        if (parsedIm != null) {
          parsedIm = parsedIm.replaceFirst("[iI]", "");
          if (parsedIm.equals("-"))
            parsed[1] = "-1";
          else if (parsedIm.equals("") || parsedIm.equals("+"))
            parsed[1] = "1";
          else
            parsed[1] = parsedIm;
        }
      }
      pattern = Pattern.compile(dbl).matcher(line);
      if (pattern.find()) {
        parsed[0] = pattern.group(0);
      }
      return parsed;
    };
  }
}
