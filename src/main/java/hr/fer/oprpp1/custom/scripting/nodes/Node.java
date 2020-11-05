package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

public class Node {

  private final ArrayIndexedCollection children = new ArrayIndexedCollection();

  public void addChildNode(Node child) {
    children.add(child);
  }

  public int numberOfChildren() {
    return children.size();
  }

  public Node getChild(int index) {
    return (Node) children.get(index);
  }

  @Override
  public String toString() {
    var sb = new StringBuilder();
    for (int i = 0; i < numberOfChildren(); i++) {
      sb.append(getChild(i));
    }
    return sb.toString();
  }
}
