package hr.fer.oprpp1.custom.scripting.lexer;

public enum LexerState {

  /**
   * While parsing outside of tags.
   */
  TEXT,

  /**
   * While parsing inside a tag.
   */
  TAG,

}
