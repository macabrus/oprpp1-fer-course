package hr.fer.zemris.java.gui.layouts;

import hr.fer.zemris.java.gui.calc.CalcModelImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class CalcLayoutTest {

  private CalcLayout m;
  private JPanel parent;
  private JPanel child;

  @BeforeEach
  public void setup() {
    m = new CalcLayout();
    parent = new JPanel(m);
    child = new JPanel();
  }

  @Test
  public void testConstraintsForRowsAndColumns() {
    assertThrows(CalcLayoutException.class, () -> {
      parent.add(child, new RCPosition(0,2));
    });
    assertThrows(CalcLayoutException.class, () -> {
      parent.add(child, new RCPosition(6,2));
    });
    assertThrows(CalcLayoutException.class, () -> {
      parent.add(child, new RCPosition(3,0));
    });
    assertThrows(CalcLayoutException.class, () -> {
      parent.add(child, new RCPosition(6,8));
    });
  }

  @Test
  public void testSameConstraintComponent() {
    assertThrows(CalcLayoutException.class, () -> {
      parent.add(child, new RCPosition(1,1));
      parent.add(new JLabel(), new RCPosition(1,1));
    });
  }

  @Test
  public void testThrowsOnDisplayCoordinates() {
    for (int i = 2; i <= 5; i++) {
      int finalI = i;
      assertThrows(CalcLayoutException.class, () -> {
        parent.add(child, new RCPosition(1, finalI));
      });
    }
  }

  @Test
  public void testScenario1() {
    JPanel p = new JPanel(new CalcLayout(2));
    JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(10,30));
    JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(20,15));
    p.add(l1, new RCPosition(2,2));
    p.add(l2, new RCPosition(3,3));
    Dimension dim = p.getPreferredSize();
    assertEquals(152, dim.width);
  }

  @Test
  public void testScenario2() {
    JPanel p = new JPanel(new CalcLayout(2));
    JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(108,15));
    JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(16,30));
    p.add(l1, new RCPosition(1,1));
    p.add(l2, new RCPosition(3,3));
    Dimension dim = p.getPreferredSize();
    assertEquals(152, dim.width);
    assertEquals(158, dim.height);
  }
}