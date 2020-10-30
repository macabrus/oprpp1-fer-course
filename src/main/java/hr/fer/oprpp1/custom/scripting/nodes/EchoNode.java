package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;

import java.util.Arrays;

public class EchoNode extends Node {

  private final Element[] elements;

  public EchoNode(Element... elements) {
    this.elements = elements;
  }

  public Element[] getElements() {
    return Arrays.copyOf(elements, elements.length);
  }

  @Override
  public String toString() {
    var sb = new StringBuilder("{$= ");
    for (Element e : elements)
      sb.append(e.asText()).append(" ");
    sb.append("$}");
    return sb.toString();
  }
}
