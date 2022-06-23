package hr.fer.oprpp1.db;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static hr.fer.oprpp1.db.TokenType.*;

/**
 * Tokenizes query into lexical units.
 */
public class QueryLexer {

  // token types map
  private Map<String, TokenType> tokenMap = new HashMap<>() {{
    put("<", LT);
    put(">", GT);
    put("<=", LTE);
    put(">=", GTE);
    put("=", EQ);
    put("\\s+LIKE\\s+", LIKE);
    put("(?i)and", CONJUNCTION);
    put("\"[^\"]*\"", STRING);
    put("\\w+", VAR);
  }};

  // expressions for tokens, order matters
  private static final String[] expressions = {
    "<=",
    ">=",
    "<",
    ">",
    "=",
    "\\s+LIKE\\s+", // requires spaces around, otherwise could be grouped as word
    "(?i)and",
    "\"[^\"]*\"",
    "\\w+"
  };
  private Scanner sc;
  private String query;

  public QueryLexer(String query) {
    sc = new Scanner(query);
    this.query = query;
  }

  public boolean hasNextToken() {
    return query.length() > 0;
  }

  public Token nextToken() {
    TokenType type = null;
    String value = null;
    while (query.length() > 0) {
      boolean match = false;
      for (String expr : expressions) {
        Pattern p = Pattern.compile(expr);
        Matcher m = p.matcher(query);
        if (m.find() && m.start() == 0) {
          type = tokenMap.get(expr);
          value = m.group(0);
          query = query.replaceFirst(expr, "");
          match = true;
          break;
        }
      }
      if (match)
        break;
      else
        query = query.substring(1);
    }
    return new Token(type, value);
  }

}
