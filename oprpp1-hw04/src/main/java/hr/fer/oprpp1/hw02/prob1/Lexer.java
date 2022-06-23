package hr.fer.oprpp1.hw02.prob1;

/**
 * A simple language parser for numbers and words.
 */
public class Lexer {

  /**
   * raw data to be tokenized
   */
  private final char[] data;

  /**
   * a last parsed token
   */
  private Token token;

  /**
   * an index of first non-consumed character in data array
   */
  private int currentIndex;

  /**
   * a current parsing mode for this lexer
   */
  private LexerState state = LexerState.BASIC;

  /**
   * @param text text to be tokenized
   * @throws NullPointerException if passed null
   */
  public Lexer(String text) {
    if (text == null)
      throw new NullPointerException();
    data = text.toCharArray();
  }

  /**
   * Manually a new parsing mode for this lexer.
   * @param state a new state of lexer
   * @throws NullPointerException if passed null
   */
  public void setState(LexerState state) {
    if (state == null)
      throw new NullPointerException("Lexer's state can't be null.");

    this.state = state;
  }


  /**
   * @return a next parsed token
   * @throws LexerException if invalid character occurs, all tokens have been parsed or
   */
  public Token nextToken() {

    if (token != null && token.getType() == TokenType.EOF)
      throw new LexerException("Reached EOF, no more tokens to parse");

    skipWhitespace();
    if (isEOF()) {
      token = new Token(TokenType.EOF, null);
    }
    else {
      // Ah yes, an opportunity to use new feature...
      token = switch (state) {
        case EXTENDED -> {
          if (data[currentIndex] == '#')
            yield new Token(TokenType.SYMBOL, data[currentIndex++]);
          else
            yield new Token(TokenType.WORD, parseExtendedSequence());
        }
        case BASIC -> {
          if (Character.isDigit(data[currentIndex]))
            yield new Token(TokenType.NUMBER, parseLong());
          else if (isWordChar())
            yield new Token(TokenType.WORD, parseWord());
          yield new Token(TokenType.SYMBOL, data[currentIndex++]);
        }
      };
    }

    return token;
  }


  /**
   * @return the last parsed token (doesn't modify lexer's state or move read pointer)
   */
  public Token getToken() {
    return token;
  }

  /**
   * @return true if character at head is escaped (preceeded by odd number of backslashes)
   */
  private boolean isEscaped() {
    int i = currentIndex - 1;
    int count = 0;
    while (i >= 0 && data[i--] == '\\')
      count++;

    return count % 2 == 1;
  }

  /**
   * @return true if character is a letter or escaped number
   */
  private boolean isWordChar() {
    char c = data[currentIndex];
    if (Character.isLetter(c) && isEscaped())
      throw new LexerException(String.format("Illegal escape on character '%s' at index %s.", c, currentIndex));

    return Character.isLetter(c) || (Character.isDigit(c) && isEscaped()) || c == '\\';
  }

  /**
   * @return a parsed long value from number sequence starting from currentIndex
   */
  private long parseLong() {
    var sb = new StringBuilder();
    while (!isEOF() && Character.isDigit(data[currentIndex]))
      sb.append(data[currentIndex++]);

    try {
      return Long.parseLong(sb.toString());
    } catch (NumberFormatException e) {
      throw new LexerException("Number sequence is longer than maximum long value and cannot be parsed.");
    }
  }

  /**
   * @return a parsed word string
   */
  private String parseWord() {
    var sb = new StringBuilder();
    while (!isEOF() && isWordChar()) {
      if (data[currentIndex] == '\\' && currentIndex + 1 == data.length)
        throw new LexerException("Illegal escape character at the end of data buffer. Nothing is escaped.");
      if (data[currentIndex] == '\\' && !isEscaped()) {
        currentIndex++;
        continue;
      }
      sb.append(data[currentIndex++]);
    }

    return sb.toString();
  }

  /**
   * Parses special #-denoted sequence in this lexers definition.
   * @return parsed string sequence nested inside two hashtags (#[sequence]#)
   */
  private String parseExtendedSequence() {
    var sb = new StringBuilder();
    while ( !isEOF() && !Character.isWhitespace(data[currentIndex]) && data[currentIndex] != '#')
      sb.append(data[currentIndex++]);
    return sb.toString();
  }

  /**
   * Skips all whitespace characters starting from current position of head if there are any.
   */
  private void skipWhitespace() {
    while (!isEOF() && Character.isWhitespace(data[currentIndex]))
      currentIndex++;
  }

  /**
   * @return true if end of buffer is reached
   */
  private boolean isEOF() {
    return currentIndex >= data.length;
  }
}
