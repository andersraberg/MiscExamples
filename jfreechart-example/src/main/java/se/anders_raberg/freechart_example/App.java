package se.anders_raberg.freechart_example;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    private static final Random RND = new Random();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            List<Double> data = createData();
            final AtomicInteger dataCounter2 = new AtomicInteger(0);
            final AtomicReference<Double> currentValue = new AtomicReference<>(0.0);

            JFrame frame = new JFrame("Dynamic Network Throughput Chart");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            TimeSeries throughputSeries = new TimeSeries("Throughput");
            TimeSeriesCollection dataset = new TimeSeriesCollection(throughputSeries);
            JFreeChart chart = ChartFactory.createTimeSeriesChart("Network Throughput", "Time", "Throughput (Mbps)",
                    dataset, //
                    false, // legend
                    true, // tooltips
                    false // URLs
            );

            XYPlot plot = (XYPlot) chart.getPlot();

            ValueAxis yAxis = new LogarithmicAxis("Logarithmic Throughput (Mbps)");
            plot.setRangeAxis(yAxis);

            ChartPanel chartPanel = new ChartPanel(chart);
            frame.add(chartPanel, BorderLayout.CENTER);

            Timer timer = new Timer(100, e -> {
                Double accumulateAndGet = currentValue.accumulateAndGet(
                        data.get(dataCounter2.incrementAndGet() % data.size()), (x, y) -> limit(x + y));
                throughputSeries.addOrUpdate(new Second(), accumulateAndGet);
            });

            timer.start();

            frame.setSize(800, 600);
            frame.setVisible(true);
        });
    }

    private static List<Double> createData() {
        List<Double> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.addAll(Collections.nCopies(RND.nextInt(20), RND.nextBoolean() ? 1.0 : -1.0));
        }
        LOGGER.info(data::toString);
        return data;
    }

    private static double limit(double v) {
        return Math.clamp(v, 1, 100);
    }
}
