package hr.fer.oprpp1.custom.scripting.lexer;


import hr.fer.oprpp1.hw02.prob1.LexerException;

import static hr.fer.oprpp1.custom.scripting.lexer.TokenType.*;

/**
 * A SmartScript 'language' lexer implementation
 */
public class SmartScriptLexer {

  /**
   * raw data to be tokenized
   */
  private final char[] data;

  /**
   * a last parsed token
   */
  private Token token;

  /**
   * a pointer to the first non-consumed character in data array
   */
  private int head;

  /**
   * a current parsing mode for this lexer
   */
  private LexerState state = LexerState.TEXT;

  /**
   * @param text text to be tokenized
   * @throws NullPointerException if passed null
   */
  public SmartScriptLexer(String text) {
    if (text == null)
      throw new SmartScriptLexerException("Lexer data must not be null");

    data = text.toCharArray();
  }

  /**
   * Manually a new parsing mode for this lexer.
   *
   * @param state a new state of lexer
   * @throws NullPointerException if passed null
   */
  public void setState(LexerState state) {
    if (state == null)
      throw new NullPointerException("Lexer's state can't be null.");

    this.state = state;
  }

  /**
   * @return current parsing mode of this lexer
   */
  public LexerState getState() {
    return state;
  }

  /**
   * @return the last parsed token (doesn't modify lexer's state or move read pointer)
   */
  public Token getToken() {
    return token;
  }

  /**
   * @return a next parsed token
   * @throws LexerException if invalid character occurs, all tokens have been parsed or
   */
  public Token nextToken() {
    if (token != null && token.getType() == EOF)
      throw new SmartScriptLexerException("Reached EOF, no more tokens to parse");

    if (isEOF())
      token = new Token(EOF, null);

    else
      // Performing operations depending on current state
      token = switch (state) {
        // Parser has set text parsing state (default)
        case TEXT -> {
          if (isText())
            yield new Token(TEXT, parseText());
          else if (isTagOpenBracket())
            yield new Token(TAG_START, parseTagOpenBracket());
          else
            throw new SmartScriptLexerException("Unable to tokenize sequence in TEXT mode at index " + head);
        }
        // Parser has set tag parsing state
        case TAG -> {
          // If in tag, we can ignore whitespace
          parseWhitespace();
          if (isTagCloseBracket())
            yield new Token(TAG_CLOSE, parseTagCloseBracket());
          else if (isVar(head))
            yield new Token(VAR, parseVar());
          else if (isInt())
            yield new Token(INT, parseInt());
          else if (isDouble())
            yield new Token(DOUBLE, parseDouble());
          else if (isFunction())
            yield new Token(FUNCTION, parseFunction());
          else if (isString())
            yield new Token(STRING, parseString());
          else if (isOperator(head))
            yield new Token(OPERATOR, parseOperator());
          else if (isTagName())
            yield new Token(VAR, parseTagName());
          else
            throw new SmartScriptLexerException("Unable to tokenize sequence in TAG mode at index " + head);
        }
      };

    return token;
  }

  /*---------- UTILITY METHODS ----------*/

  private boolean isFunction() {
    if (data[head] != '@')
      return false;

    return isVar(head + 1);
  }

  private String parseFunction() {
    head++;
    return '@' + parseVar();
  }

  private boolean isOperator(int start) {
    return "+-*/^".indexOf(data[start]) != -1;
  }

  private String parseOperator() {
    return String.valueOf(data[head++]);
  }

  private boolean isString() {
    if (!isEscaped() && isSequence("\"")) {
      int ptr = head + 1;
      while (ptr < data.length) {
        if (data[ptr] == '\"' && !isEscaped())
          return true;
        ptr++;
      }
    }

    return false;
  }

  private Object parseString() {
    var sb = new StringBuilder("\"");

    head++;
    while (!isEOF() && !(data[head] == '\"' && !isEscaped()))
      sb.append(data[head++]);
    head++;
    sb.append('\"');

    return sb.toString();
  }

  private boolean isVar(int start) {
    return Character.isLetter(data[start]);
  }

  private String parseVar() {
    var sb = new StringBuilder();

    while (!isEOF() && (Character.isLetter(data[head]) || Character.isDigit(data[head]) || data[head] == '_')) {
      sb.append(data[head]);
      head++;
    }

    return sb.toString();
  }

  private boolean isDouble() {
    int ptr = head;
    boolean leftDigits = false;
    boolean rightDigits = false;
    boolean fp = false;

    // Skipping sign if there is one
    if (data[ptr] == '-' || data[ptr] == '+')
      ptr++;
    // Skipping whole digits
    while (Character.isDigit(data[ptr])) {
      leftDigits = true;
      ptr++;
    }
    // Skipping floating point if there is one
    if (data[ptr] == '.') {
      fp = true;
      ptr++;
    }
    // Skipping digits after floating point
    while (Character.isDigit(data[ptr])) {
      rightDigits = true;
      ptr++;
    }

    // If at least some digits existed, then it is ok,
    // Otherwise it is not, (e.g. "-." is not a valid double)
    return fp && (leftDigits || rightDigits);
  }

  private Double parseDouble() {
    var sb = new StringBuilder();

    if (data[head] == '-' || data[head] == '+') {
      sb.append(data[head]);
      head++;
    }
    while (Character.isDigit(data[head])) {
      sb.append(data[head]);
      head++;
    }
    if (data[head] == '.') {
      sb.append(data[head]);
      head++;
    }
    while (Character.isDigit(data[head])) {
      sb.append(data[head]);
      head++;
    }

    try {
      return Double.parseDouble(sb.toString());
    } catch (NumberFormatException e) {
      throw new SmartScriptLexerException("Number literal is to large to be parsed as double");
    }
  }

  private boolean isInt() {
    if (data[head] == '-' || data[head] == '+')
      return !isDouble() && Character.isDigit(data[head + 1]);
    return !isDouble() && Character.isDigit(data[head]);
  }

  private int parseInt() {
    var sb = new StringBuilder();
    while (!isEOF() && (Character.isDigit(data[head]) || "+-".indexOf(data[head]) != -1))
      sb.append(data[head++]);
    try {
      return Integer.parseInt(sb.toString());
    } catch (NumberFormatException e) {
      throw new LexerException("Number sequence is longer than maximum int value and cannot be parsed.");
    }
  }

  private boolean isText() {
    return !isTagOpenBracket();
  }

  private String parseText() {
    var sb = new StringBuilder();

    while (!isEOF() && !isTagOpenBracket()) {
      // Skipping start of escape sequence
      if (data[head] == '\\' && !isEscaped()) {
        head++;
        continue;
      }
      // For every escaped character that is not \ or {, lexer throws
      if (isEscaped() && !(data[head] == '\\' || data[head] == '{'))
        throw new SmartScriptLexerException("Illegal escape occured at index " + head);
      sb.append(data[head++]);
    }

    return sb.toString();
  }

  private boolean isTagName() {
    return isVar(head) || data[head] == '=';
  }

  private String parseTagName() {
    var sb = new StringBuilder();

    if (data[head] == '=') {
      head++;
      return sb.append('=').toString();
    }

    while (!isEOF() && (Character.isLetter(data[head]) || Character.isDigit(data[head]) || data[head] == '_')) {
      sb.append(data[head]);
      head++;
    }

    return sb.toString();
  }

  private boolean isSequence(String sequence) {
    for (int i = 0; i < sequence.length(); i++)
      if (isEOF() || data[head + i] != sequence.charAt(i))
        return false;

    return true;
  }

  private boolean isTagOpenBracket() {
    return !isEscaped() && isSequence("{$");
  }

  private String parseTagOpenBracket() {
    head += 2;

    return "{$";
  }

  private boolean isTagCloseBracket() {
    return !isEscaped() && isSequence("$}");
  }

  private String parseTagCloseBracket() {
    head += 2;

    return "$}";
  }

  private boolean isEscaped() {
    int i = head - 1;
    int count = 0;
    while (i >= 0 && data[i--] == '\\')
      count++;

    return count % 2 == 1;
  }

  private void parseWhitespace() {
    while (!isEOF() && Character.isWhitespace(data[head]))
      head++;
  }

  private boolean isEOF() {
    return head >= data.length;
  }
}
