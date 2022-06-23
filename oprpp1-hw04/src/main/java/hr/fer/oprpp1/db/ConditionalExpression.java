package hr.fer.oprpp1.db;

public class ConditionalExpression {

  private final IFieldValueGetter fieldGetter;
  private final String stringLiteral;
  private final IComparisonOperator comparisonOperator;

  public ConditionalExpression(IFieldValueGetter getter, String expr, IComparisonOperator operator) {

    this.fieldGetter = getter;
    this.stringLiteral = expr;
    this.comparisonOperator = operator;
  }

  public IFieldValueGetter getFieldGetter() {
    return fieldGetter;
  }

  public String getStringLiteral() {
    return stringLiteral;
  }

  public IComparisonOperator getComparisonOperator() {
    return comparisonOperator;
  }
}
