package hr.fer.oprpp1.demo;

import hr.fer.oprpp1.custom.collections.ObjectStack;

public class StackDemo {

  public static void main(String... args) {
    var stack = new ObjectStack();
    var expr = args[0].split(" ");
    for (String e : expr) {
      if (e.matches("[-+]?[0-9]+")) {
        System.out.println("NUMBER " + e);
        stack.push(Integer.parseInt(e));
      }
      else {
        var b = (int) stack.pop();
        var a = (int) stack.pop();
        System.out.println("A AND B = " + a + " " + b);
        stack.push(switch (e) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> a / b;
            default -> throw new ArithmeticException("Unknown operator " + e);
        });
      }
    }
    if (stack.size() != 1)
      throw new RuntimeException("Stack is not empty!");
    else
      System.out.println(stack.pop());
  }
}
