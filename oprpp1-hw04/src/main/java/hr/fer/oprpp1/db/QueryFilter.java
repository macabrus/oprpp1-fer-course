package hr.fer.oprpp1.db;

import java.util.List;

public class QueryFilter implements IFilter {

  private List<ConditionalExpression> conditionals;

  public QueryFilter(List<ConditionalExpression> conditionals) {
    this.conditionals = conditionals;
  }

  @Override
  public boolean accepts(StudentRecord record) {
    for (ConditionalExpression cond : conditionals) {
      if (!cond.getComparisonOperator().satisfied(cond.getFieldGetter().get(record),cond.getStringLiteral()))
        return false;
    }
    return true;
  }
}
