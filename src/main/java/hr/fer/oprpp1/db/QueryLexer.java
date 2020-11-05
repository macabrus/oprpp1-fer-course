package hr.fer.oprpp1.db;

import java.util.Scanner;

import static hr.fer.oprpp1.db.TokenType.*;

/**
 * Tokenizes query into lexical units.
 */
public class QueryLexer {

  private Scanner sc;

  public QueryLexer(String query) {
    sc = new Scanner(query);
  }

  public boolean hasNextToken() {
    return sc.hasNext();
  }

  public Token nextToken() {
    if (sc.hasNext("="))
      return new Token(EQ, sc.next("="));
    if (sc.hasNext("<"))
      return new Token(LT, sc.next("<"));
    if (sc.hasNext(">"))
      return new Token(GT, sc.next(">"));
    if (sc.hasNext("<="))
      return new Token(LTE, sc.next("<="));
    if (sc.hasNext(">="))
      return new Token(GTE, sc.next(">="));
    if (sc.hasNext("!="))
      return new Token(NEQ, sc.next("!="));
    if (sc.hasNext("\\s*LIKE\\s*"))
      return new Token(LIKE, sc.next("LIKE"));
    if (sc.hasNext("(?i)\\s*and\\s*"))
      return new Token(CONJUNCTION, sc.next());
    if (sc.hasNext("\\s*\"[^\"\\s]*\"")) {
      var str = sc.next();
      return new Token(STRING, str.substring(1, str.length() - 1));
    }
    if (sc.hasNextInt())
      return new Token(NUM, sc.next());
    return new Token(VAR, sc.next());
  }

}
