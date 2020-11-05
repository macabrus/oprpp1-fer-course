package hr.fer.oprpp1.db;

public class ComparisonOperators {
  public static final IComparisonOperator LESS = (o1, o2) -> o1.compareTo(o2) < 0;
  public static final IComparisonOperator LESS_OR_EQUALS = (o1, o2) -> o1.compareTo(o2) <= 0;
  public static final IComparisonOperator GREATER = (o1, o2) -> o1.compareTo(o2) > 0;
  public static final IComparisonOperator GREATER_OR_EQUALS = (o1, o2) -> o1.compareTo(o2) >= 0;
  public static final IComparisonOperator EQUALS = (o1, o2) -> o1.compareTo(o2) == 0;
  public static final IComparisonOperator NOT_EQUALS = (o1, o2) -> o1.compareTo(o2) != 0;
  public static final IComparisonOperator LIKE = (o1, o2) -> {
    if (o2.contains("*")) {
      int total = 0;
      int i = 0;
      // checking from left side til *
      while (o2.charAt(i) == o1.charAt(i)) {
        total++;
        i++;
      }
      // checking from right side til *
      i = 1;
      while (o2.charAt(o2.length() - i) == o1.charAt(o1.length() - i)) {
        total++;
        i++;
      }
      // if it counted right amount of characters, LIKE is true
      return total == o2.length() - 1;
    }
    // if there was no kleene operator, do normal comparison
    else
      return EQUALS.satisfied(o1, o2);
  };
}
