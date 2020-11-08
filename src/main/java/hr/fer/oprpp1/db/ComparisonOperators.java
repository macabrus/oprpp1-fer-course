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
      var parts = o2.split("\\*");
      if (parts.length == 1)
        return o1.startsWith(parts[0]) || o1.endsWith(parts[0]);
      // apart from starting and ending with parts,
      // being shorter or of equal length to target string is also a requirement
      // because following is not a match: AAA doesn't match AA*AA
      return o1.startsWith(parts[0]) && o1.endsWith(parts[1]) && o2.length() - 1 <= o1.length();
    }
    else
      return EQUALS.satisfied(o1, o2);
  };
}
