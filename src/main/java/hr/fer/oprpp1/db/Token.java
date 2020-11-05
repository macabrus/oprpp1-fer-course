package hr.fer.oprpp1.db;

public class Token {

  private TokenType type;
  private String value;

  public Token(TokenType type, String value) {
    this.type = type;
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public TokenType getType() {
    return type;
  }

  @Override
  public String toString() {
    return "(" + type + ", "+ value + ')';
  }
}
