package hr.fer.zemris.java.gui.layouts;

import java.awt.*;
import java.util.*;
import java.util.function.Function;

public class CalcLayout implements LayoutManager2 {

  private Map<Component, RCPosition> posMap = new HashMap();
  private Set<RCPosition> positions = new HashSet<>();
  private int spacing;

  public CalcLayout() {
    this(0);
  }

  public CalcLayout(int spacing) {
    this.spacing = spacing;
  }

  @Override
  public void addLayoutComponent(Component comp, Object constraints) {
    if (! (constraints instanceof RCPosition))
      throw new CalcLayoutException("Provided constraints must be of type RCPosition");
    if ( comp.getParent().getComponentCount() > 31)
      throw new CalcLayoutException("CalcLayout supports up to 31 child elements.");
    if ( positions.contains(constraints))
      throw new CalcLayoutException("Component already exists at this index");
    RCPosition pos = (RCPosition) constraints;
    int r = pos.getRow();
    int c = pos.getCol();
    System.out.println(r + " " + c);
    if (r < 1 || r > 5 || c < 1 || c > 7 || r == 1 && c > 1 && c < 6)
      throw new CalcLayoutException("Illegal position for first row component. It must be (1,1), (1,6) or (1,7)");
    posMap.put(comp, pos);
    positions.add(pos);
  }

  @Override
  public float getLayoutAlignmentX(Container target) {
    return 0;
  }

  @Override
  public float getLayoutAlignmentY(Container target) {
    return 0;
  }

  @Override
  public void invalidateLayout(Container target) {
    // nothing is cached so...
  }

  @Override
  public void addLayoutComponent(String name, Component comp) {
    // pass
  }

  @Override
  public void removeLayoutComponent(Component comp) {
    positions.remove(posMap.get(comp));
    posMap.remove(comp);
  }

  @Override
  public Dimension maximumLayoutSize(Container target) {
    return strategicLayoutSize(target, Component::getMaximumSize);
  }

  @Override
  public Dimension preferredLayoutSize(Container parent) {
    return strategicLayoutSize(parent, Component::getPreferredSize);
  }

  @Override
  public Dimension minimumLayoutSize(Container parent) {
    return strategicLayoutSize(parent, Component::getMinimumSize);
  }

  // Strategy method
  private Dimension strategicLayoutSize(Container parent, Function<Component, Dimension> strategy) {
    int prefHeight = Arrays.stream(parent.getComponents())
      .map(c -> strategy.apply(c).height)
      .max(Integer::compareTo)
      .orElse(0);
    int prefWidh = Arrays.stream(parent.getComponents())
//      .filter(c -> {
//        RCPosition pos = posMap.get(c);
//        return !(pos.getCol() == 1 && pos.getRow() == 1);
//      })
      .map(c -> {
        var rc = posMap.get(c);
        var x = rc.row;
        var y = rc.col;
        if (x == 1 && y == 1)
          return (strategy.apply(c).width - 4 * spacing) / 5 ;
        return strategy.apply(c).width;
      })
      .max(Integer::compareTo)
      .orElse(0);
    return new Dimension(prefWidh * 7 + spacing * 6, prefHeight * 5 + spacing * 4);
  }

  @Override
  public void layoutContainer(Container parent) {
    Insets insets = parent.getInsets();
    // koliko prostora imamo u parent containeru
    int maxWidth = parent.getWidth() - (insets.left + insets.right);
    int maxHeight = parent.getHeight() - (insets.top + insets.bottom);

    // sirina jedne celije
    double cellWidth = (maxWidth - spacing * 6) / 7.;
    double cellHeight = (maxHeight - spacing * 4) / 5.;

    // sada moramo proiterirati po djeci kontejnera
    for (Component comp : parent.getComponents()) {
      RCPosition pos = posMap.get(comp);
      int r = pos.getRow();
      int c = pos.getCol();
      int x = (int) (Math.round((pos.getCol() - 1) * cellWidth) + spacing * (pos.getCol() - 1));
      int y = (int) (Math.round((pos.getRow() - 1) * cellHeight) + spacing * (pos.getRow() - 1));
      int w = (int) cellWidth;
      int h = (int) cellHeight;
      if (r == 1 && c == 1)
        w += cellWidth * 4 + spacing * 4;
      comp.setBounds(x, y, w, h); // fix it
    }
  }
}
