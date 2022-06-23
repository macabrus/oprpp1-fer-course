package hr.fer.oprpp1.custom.scripting.lexer;

public enum TokenType {

  /**
   * Everything outside of tags (separated by tags only)
   */
  TEXT,

  /**
   * A string literal sequence inside of a tag
   */
  STRING,

  /**
   * A whole number constant inside
   */
  INT,

  /**
   * While parsing a double constant (optionally starts with minus)
   */
  DOUBLE,

  /**
   * Variable name inside of tag constant
   */
  VAR,

  /**
   * Beginning of a tag '{$'
   */
  TAG_START,

  /**
   * Closing sequence for tag
   */
  TAG_CLOSE,

  /**
   * End of document (last token)
   */
  EOF,

  /**
   * Function call inside tag element
   */
  FUNCTION,

  /**
   * { +, -, *, /, ^}
   */
  OPERATOR
}
