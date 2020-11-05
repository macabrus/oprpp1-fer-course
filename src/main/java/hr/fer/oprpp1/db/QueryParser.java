package hr.fer.oprpp1.db;

import java.util.ArrayList;
import java.util.List;

public class QueryParser {

  List<ConditionalExpression> conditionals = new ArrayList<>();
  String query;

  public QueryParser(String query) {
    this.query = query;

    var sc = new QueryLexer(query);
    IFieldValueGetter columnGetter = null;
    IComparisonOperator operator = null;
    String value = null;
    while(sc.hasNextToken()) {
      var token = sc.nextToken();
      switch (token.getType()) {
        case VAR -> columnGetter = switch (token.getValue()) {
          case "jmbag" -> FieldValueGetters.JMBAG;
          case "lastName" -> FieldValueGetters.LAST_NAME;
          case "firstName" -> FieldValueGetters.FIRST_NAME;
          default -> throw new QueryException("Unknown column name in query: " + token.getValue());
        };
        case EQ -> operator = ComparisonOperators.EQUALS;
        case LT -> operator = ComparisonOperators.LESS;
        case GT -> operator = ComparisonOperators.GREATER;
        case LTE -> operator = ComparisonOperators.LESS_OR_EQUALS;
        case GTE -> operator = ComparisonOperators.GREATER_OR_EQUALS;
        case NEQ -> operator = ComparisonOperators.NOT_EQUALS;
        case LIKE -> operator = ComparisonOperators.LIKE;
        case STRING, NUM -> value = token.getValue();
        case CONJUNCTION -> {
          if (columnGetter == null || operator == null || value == null)
            throw new QueryException("Malformed query: " + query);
          columnGetter = null;
          operator = null;
          value = null;
        }
      }
      if (columnGetter != null && operator != null && value != null)
        conditionals.add(new ConditionalExpression(columnGetter, value, operator));
    }
  }

  public boolean isDirectQuery() {
    if (conditionals.size() == 1) {
      var cmp = conditionals.get(0);
      return cmp.getComparisonOperator().equals(ComparisonOperators.EQUALS) &&
        cmp.getFieldGetter().equals(FieldValueGetters.JMBAG);
    }
    return false;
  }

  public String getQueriedJMBAG() {
    if (!isDirectQuery())
      throw new IllegalStateException("Can't get queried JMBAG because this is not a direct query");
    return conditionals.stream().findFirst().orElseThrow().getStringLiteral();
  }

  List<ConditionalExpression> getQuery() {
    return conditionals;
  }
}
