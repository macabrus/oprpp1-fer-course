package hr.fer.oprpp1.custom.scripting.elems;

public class ElementString extends Element {

  private final String value;

  public ElementString(String value) {
    this.value = value;
  }

  @Override
  public String asText() {
    return value;
  }

}
