package hr.fer.zemris.java.gui.calc;

import java.util.*;
import java.util.function.DoubleBinaryOperator;

public class CalcModelImpl implements CalcModel {

  private boolean negative = false; // true if - else +
  private boolean inv = false; // whether inverse button is pressed
  private double prevVal;
  private double val;
  private String displayVal = "";
  private boolean frozen = false;
  private DoubleBinaryOperator op;

  private List<CalcValueListener> valListeners = new ArrayList<>();
  private Stack<Double> numStack = new Stack<>();

  private Map<String, Runnable> actionStrat = new HashMap<>() {{

    // digit actions
    for (char c : "0123456789".toCharArray()) {
      put(String.valueOf(c), () -> {
        insertDigit(c - '0');
      });
    }
    put(".", CalcModelImpl.this::insertDecimalPoint);

    // Binary ops
    put("+", () -> setPendingBinaryOperation((d1, d2) -> d1 + d2) );
    put("-", () -> setPendingBinaryOperation((d1, d2) -> d1 - d2) );
    put("*", () -> setPendingBinaryOperation((d1, d2) -> d1 * d2) );
    put("/", () -> setPendingBinaryOperation((d1, d2) -> d1 / d2) );
    put("x^n", () -> {
      if (!inv) {
        setPendingBinaryOperation(Math::pow);
      } else {
        setPendingBinaryOperation((d1, d2) -> Math.pow(d1, 1. / d2));
      }
    });

    // Compute command
    put("=", () -> {
      if (getPendingBinaryOperation() != null)
        setValue(getPendingBinaryOperation().applyAsDouble(getPrevVal(), getValue()));
      frozen = true;
    });

    // Stack commands
    put("push", () -> numStack.push(getValue()));
    put("pop", () -> setValue(numStack.pop()));
    // Reset
    put("res", CalcModelImpl.this::clearAll);
    // Clear
    put("clr", CalcModelImpl.this::clear);

    // Bijections
    put("1/x", () -> setValue(!inv ? 1. / getValue() : getValue()));
    put("sin", () -> setValue(!inv ? Math.sin(getValue()) : Math.asin(getValue())));
    put("log", () -> setValue(!inv ? Math.log10(getValue()) : Math.pow(getValue(), 10)));
    put("ln", () -> setValue(!inv ? Math.log(getValue()) : Math.exp(getValue())));
    put("cos", () -> setValue(!inv ? Math.cos(getValue()) : Math.acos(getValue())));
    put("tan", () -> setValue(!inv ? Math.tan(getValue()) : Math.atan(getValue())));
    put("ctg", () -> setValue(!inv ? 1. / Math.tan(getValue()) : Math.atan(1. / getValue())));
    put("+/-", () -> setValue(-getValue()));
  }};


  @Override
  public void addCalcValueListener(CalcValueListener l) {
    valListeners.add(l);
  }

  @Override
  public void removeCalcValueListener(CalcValueListener l) {
    if (l == null)
      throw new NullPointerException("null is not an instance of " + getClass());
    valListeners.remove(l);
  }

  @Override
  public double getValue() {
    return val;
  }

  @Override
  public void setValue(double value) {
    this.val = value;
    this.displayVal = String.valueOf(val);
    valListeners.forEach(l -> l.onValueChange(displayVal, val));
  }

  private String getDisplayValue() {
    return displayVal;
  }

  private void setDisplayValue(String value) {
    this.val = "".equals(value) ? 0 : Double.parseDouble(value);
    this.displayVal = value;
    valListeners.forEach(l -> l.onValueChange(displayVal, val));
  }

  @Override
  public void clear() {
    setDisplayValue("");
  }

  @Override
  public void clearAll() {
    clear();
    op = null;
    numStack.clear();
  }

  @Override
  public void insertDecimalPoint() throws CalculatorInputException {
    if (frozen) {
      setDisplayValue("");
      frozen = false;
    }
    if ("".equals(getDisplayValue()))
      throw new CalculatorInputException("Calculator doesn't have a value yet!");
    setDisplayValue(getDisplayValue() + ".");
  }

  @Override
  public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
    if (digit > 9 || digit < 0)
      throw new IllegalArgumentException("Digit must be 0-9");
    if (frozen) {
      setDisplayValue("");
      frozen = false;
    }
    if (!parseableDouble(getDisplayValue() + digit))
      throw new CalculatorInputException("Not representable as double!");
    setDisplayValue(getDisplayValue() + digit);
  }

  @Override
  public DoubleBinaryOperator getPendingBinaryOperation() {
    return op;
  }

  @Override
  public void setPendingBinaryOperation(DoubleBinaryOperator op) {
    setPrevVal(getValue());
    //clear();
    this.op = op;
    frozen = true;

  }

  @Override
  public void perform(String input) {
    actionStrat.getOrDefault(input, () -> {
      throw new CalculatorInputException("Unsupported input");
    }).run();
  }

  private boolean parseableDouble(String num) {
    try {
      Double.parseDouble(num);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public double getPrevVal() {
    return prevVal;
  }

  public void setPrevVal(double prevVal) {
    this.prevVal = prevVal;
  }

  public void useInverse(boolean val) {
    inv = val;
  }
}
