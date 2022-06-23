package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;

public class ForLoopNode extends Node {
  private final ElementVariable variable;
  private final Element startExpression;
  private final Element endExpression;
  private final Element stepExpression;

  public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {
    this.variable = variable;
    this.startExpression = startExpression;
    this.endExpression = endExpression;
    this.stepExpression = stepExpression;
  }

  public ElementVariable getVariable() {
    return variable;
  }

  public Element getStartExpression() {
    return startExpression;
  }

  public Element getEndExpression() {
    return endExpression;
  }

  public Element getStepExpression() {
    return stepExpression;
  }

  @Override
  public String toString() {
    return String.format("{$ FOR %s %s %s %s $}",
      variable.asText(),
      startExpression.asText(),
      endExpression.asText(),
      stepExpression == null ? "" : stepExpression.asText())
      // Što god je unutar for loop taga
      + super.toString()
      // kraj for loop taga
      + "{$ END $}";
  }
}
