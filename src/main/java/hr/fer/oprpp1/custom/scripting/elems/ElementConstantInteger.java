package hr.fer.oprpp1.custom.scripting.elems;

public class ElementConstantInteger extends Element {

  private final Integer value;

  public ElementConstantInteger(Integer value) {
    this.value = value;
  }


  @Override
  public String asText() {
    return String.valueOf(value);
  }
}
