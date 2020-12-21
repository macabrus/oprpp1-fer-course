package hr.fer.zemris.java.gui.charts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BarChart {

  private final List<XYValue> vals;
  private final String xAxisLabel;
  private final String yAxisLabel;
  private final int yStart;
  private final int yEnd;
  private final int tickLength;

  public BarChart(List<XYValue> vals, String xAxisLabel, String yAxisLabel, int yStart, int yEnd, int tickLength) {
    vals.forEach(v -> {
      if (v.y < yStart)
        throw new IllegalArgumentException("Y values must be greater or equal to starting y value (%s)".formatted(yStart));
    });
    this.vals = new ArrayList<>(vals);
    this.vals.sort(Comparator.comparingInt(v -> v.x));
    this.xAxisLabel = xAxisLabel;
    this.yAxisLabel = yAxisLabel;
    this.yStart = yStart;
    this.yEnd = yEnd;
    this.tickLength = tickLength;
  }

  public List<XYValue> getVals() {
    return vals;
  }

  public String getxAxisLabel() {
    return xAxisLabel;
  }

  public String getyAxisLabel() {
    return yAxisLabel;
  }

  public int getyStart() {
    return yStart;
  }

  public int getyEnd() {
    return yEnd;
  }

  public int getTickLength() {
    return tickLength;
  }
}
