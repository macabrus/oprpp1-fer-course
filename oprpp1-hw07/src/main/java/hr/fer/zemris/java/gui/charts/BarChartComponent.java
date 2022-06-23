package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class BarChartComponent extends JComponent {
  private BarChart chart;

  public BarChartComponent(BarChart chart) {
    this.chart = chart;
    setOpaque(true);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    drawText((Graphics2D) g, 10, getHeight() / 2, -90, chart.getyAxisLabel());
    drawText((Graphics2D) g, getWidth() / 2, getHeight() - 10, 0, chart.getxAxisLabel());
    drawYTicks((Graphics2D)g, 20, 20, 20 + getMaxYMarkWidth((Graphics2D) g), getHeight() - 40);
    drawXTicks((Graphics2D)g, 20 + getMaxYMarkWidth((Graphics2D) g), getHeight() - 40, getWidth() - 20, getHeight() - 20);
    drawAxes((Graphics2D) g, 20 + getMaxYMarkWidth((Graphics2D) g), 20, getWidth() - 20, getHeight() - 40);
  }

  private void drawYTicks(Graphics2D g2, int tlx, int tly, int brx, int bry) {
    double w = brx - tlx;
    double h = bry - tly;
    int count = (chart.getyEnd() - chart.getyStart()) / chart.getTickLength();
    double ch = (h / count); // cell height
    for (int i = count; i >= 0; i --) {
      drawText(g2, tlx + w / 2, tly + h - ch * i, 0, String.valueOf(chart.getyStart() + i * chart.getTickLength()));
    }
  }

  private void drawXTicks(Graphics2D g2, int tlx, int tly, int brx, int bry) {
    double w = brx - tlx;
    double h = bry - tly;
    int count = chart.getVals().size();
    double cw = (w / count); // cell width
    for (int i = 0; i < count; i ++) {
      drawText(g2, tlx + cw * i + cw/2 , tly + h / 2, 0, String.valueOf(chart.getVals().get(i).x));
    }
  }

  private int getMaxYMarkWidth(Graphics2D g2) {
    var fm = g2.getFontMetrics();
    int max = 0;
    for (int i = chart.getyStart(); i <= chart.getyEnd(); i+=chart.getTickLength()) {
      int w = fm.getStringBounds(String.valueOf(i), g2).getBounds().width;
      if (w > max) {
        max = w;
      }
    }
    return max;
  }

  private void drawAxes(Graphics2D g2, int tlx, int tly, int brx, int bry) {
    // Grid
    g2.setColor(Color.orange);
    double w = brx - tlx;
    double h = bry - tly;
    int nh = (int) (((double)chart.getyEnd() - chart.getyStart()) / chart.getTickLength());
    double ch = (h / nh);
    int nv = chart.getVals().size();
    int cw = (int) (w / nv);
    // događa se problem zbog zaokruživanja, kao mali stutter kod vertikalnog resizeanja
    for (int i = 0; i < nh; i ++) {
      g2.drawLine(tlx, (int) (tly + i * ch), brx, (int) (tly + i * ch));
    }
    for (int i = cw; i < nv * cw; i += cw) {
      g2.drawLine(tlx + i, tly, tlx + i, bry);
    }

    g2.setColor(Color.ORANGE);
    // Draw values
    int col = 0;
    for (XYValue val : chart.getVals()) {
      double barPercent = ((val.getY() - chart.getyStart()) / ((double)chart.getyEnd() - chart.getyStart()));
      int barHeight = (int) (barPercent * h);
      int fillYStart = (int) (tly + (h - barHeight));
      int fillXStart = tlx + 2 + col++ * cw;
      g2.fillRect(fillXStart, fillYStart, cw - 4, barHeight);
    }

    // X and Y axes
    g2.setColor(Color.GRAY);
    g2.drawLine(tlx,tly, tlx, bry);
    g2.fillPolygon(new int[] {tlx - 5, tlx, tlx + 5}, new int[] {tly + 5, tly - 5, tly + 5}, 3);
    g2.drawLine(tlx,bry, brx, bry);
    g2.fillPolygon(new int[] {brx - 5, brx + 5, brx - 5}, new int[] {bry - 5, bry, bry + 5}, 3);
  }

  public void drawText(Graphics2D g2d, double x, double y, int angle, String text) {
    FontMetrics fm = g2d.getFontMetrics();
    Rectangle2D r = fm.getStringBounds(text, g2d);
    int xCenter = (int) (r.getWidth() / 2);
    int yCenter = (int) (r.getHeight() / 2 - fm.getAscent());

    g2d.translate((float)x,(float)y);
    g2d.rotate(Math.toRadians(angle));
    g2d.drawString(text,-xCenter,-yCenter);
    g2d.rotate(-Math.toRadians(angle));
    g2d.translate(-(float)x,-(float)y);
  }
}
