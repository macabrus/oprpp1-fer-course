package hr.fer.zemris.java.hw06;

/**
 * Universal interface for CLI subprograms that prepares
 * arguments (parses string of characters from shell) before usage
 */
public interface CommandParser {
  String[] parse(String line);
}
