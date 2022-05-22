package Neu.Network.charts;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.util.ArrayList;

public class ChartGenerator extends ApplicationFrame {

    public ChartGenerator(String neuronCount, ArrayList<Cord> error) {

        super("Network : ");

        final XYSeries originSeries = new XYSeries("f(x)");

        for (Cord cord : error) {
            originSeries.add(cord.getX(), cord.getY()/3);
        }


        final XYSeriesCollection data = new XYSeriesCollection(originSeries);

        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Neurons:" + neuronCount,
                "Epochs",
                "Error",
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = (XYPlot) chart.getPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, false);
        plot.setRenderer(renderer);

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1000, 540));
        setContentPane(chartPanel);

    }
}