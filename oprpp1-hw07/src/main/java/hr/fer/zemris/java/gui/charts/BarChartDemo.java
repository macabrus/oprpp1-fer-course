package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

public class BarChartDemo extends JFrame {

  private static String[] args;

  public static void main(String[] args) {
    BarChartDemo.args = args;
    SwingUtilities.invokeLater(()->{
      new BarChartDemo().setVisible(true);
    });
  }
  public BarChartDemo() {
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    initGUI();
    pack();
  }

  private void initGUI() {
    // vrlo jednostavno parsiranje ulazne datoteke
    // ne isplati se raditi poseban parser i lexer
    String[] lines = new String[0];
    try {
      lines = Files.readString(Path.of(args[0])).split(System.lineSeparator());
    } catch (IOException e) {
      e.printStackTrace();
    }

    setPreferredSize(new Dimension(400, 400));
    Container cp = getContentPane();
    cp.add(new BarChartComponent(new BarChart(
      Arrays.stream(lines[2].split(" "))
        .map(e -> {
        var pair = e.split(",");
        return new XYValue(Integer.parseInt(pair[0]),Integer.parseInt(pair[1]));
      }).collect(Collectors.toList()),
      lines[0],
      lines[1],
      Integer.parseInt(lines[3]),
      Integer.parseInt(lines[4]),
      Integer.parseInt(lines[5])
      )));
  }
}
