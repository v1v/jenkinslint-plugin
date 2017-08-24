package org.jenkins.ci.plugins.jenkinslint.graph;

import hudson.util.ChartUtil;
import org.jenkins.ci.plugins.jenkinslint.model.InterfaceCheck;
import org.jenkins.ci.plugins.jenkinslint.model.Job;
import org.jenkins.ci.plugins.jenkinslint.model.Lint;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleInsets;

import java.awt.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.TreeMap;


/**
 * JenkinsLintGraph Helper Class.
 * @author Victor Martinez
 **/
public class JenkinsLintGraph {

    public static JFreeChart createChart(Enumeration<Job> jobSet) {
        return createChart(createDataset(jobSet));
    }

    public static JFreeChart createPieChart(Enumeration<Job> jobSet) {
        return createPieChart(createPieDataset(jobSet));
    }


    public static JFreeChart createSeverityPieChart(Enumeration<Job> jobSet, Hashtable<String, InterfaceCheck> checkSet) {
        return createSeverityPieChart(createSeverityPieDataset(jobSet, checkSet));
    }

    /**
     * Generates a dataset based on the overall lint defects
     * @param jobSet
     * @return
     */
    private static CategoryDataset createDataset(Enumeration<Job> jobSet) {
        TreeMap<String, LintGraphData> graphData = new TreeMap<>();
        for (Job key : Collections.list(jobSet)) {
            for (Lint lint : key.getLintList()) {
                LintGraphData temp;
                if (graphData.containsKey(lint.getName())) {
                    temp = graphData.get(lint.getName());
                } else {
                    temp = new LintGraphData(lint.getName());
                }
                if (lint.isEnabled()) {
                    if (lint.isIgnored()) {
                        temp.incTotalIgnored();
                    } else {
                        if (lint.isFound()) {
                            temp.incTotalFound();
                        } else {
                            temp.incTotalNotFound();
                        }
                    }
                } else {
                    temp.incTotalDisabled();
                }
                graphData.put(lint.getName(), temp);
            }
        }
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (LintGraphData data : graphData.values()) {
            dataset.setValue(data.getTotalNotFound(), "Passed", data.getName());
            dataset.setValue(data.getTotalFound(), "Defect", data.getName());
            dataset.setValue(data.getTotalIgnored(), "Ignored", data.getName());
            dataset.setValue(data.getTotalDisabled(), "Disabled", data.getName());
        }

        return dataset;
    }

    /**
     * Generates a StackedBarChart based on a specific dataset
     * @param dataset
     * @return
     */
    private static JFreeChart createChart(final CategoryDataset dataset) {

        final JFreeChart chart = ChartFactory.createStackedBarChart(
                "", "", "Number of Defects",
                dataset, PlotOrientation.HORIZONTAL, true, true, false);

        chart.setBackgroundPaint(Color.WHITE);

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlinePaint(null);
        plot.setForegroundAlpha(0.8f);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.getDomainAxis().setLowerMargin(0.0);
        plot.getDomainAxis().setUpperMargin(0.0);
        plot.getDomainAxis().setCategoryMargin(0.05);

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        ChartUtil.adjustChebyshev(dataset, rangeAxis);
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRange(true);

        plot.getRenderer().setSeriesPaint(0, passed());
        plot.getRenderer().setSeriesPaint(1, defect());
        plot.getRenderer().setSeriesPaint(2, ignored());
        plot.getRenderer().setSeriesPaint(3, disabled());
        plot.getRenderer().setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        plot.getRenderer().setBaseItemLabelsVisible(true);

        // crop extra space around the graph
        plot.setInsets(new RectangleInsets(0, 0, 0, 5.0));
        return chart;
    }


    /**
     * Generates a dataset based on the overall lint defects
     * @param jobSet
     * @return
     */
    private static PieDataset createPieDataset(Enumeration<Job> jobSet) {
        int totalFound = 0;
        int totalNotFound = 0;
        int totalIgnored = 0;
        int totalDisabled = 0;

        for (Job key : Collections.list(jobSet)) {
            for (Lint lint : key.getLintList()) {
                if (lint.isEnabled()) {
                    if (lint.isIgnored()) {
                        totalIgnored++;
                    } else {
                        if (lint.isFound()) {
                            totalFound++;
                        } else {
                            totalNotFound++;
                        }
                    }
                } else {
                    totalDisabled++;
                }
            }
        }
        DefaultPieDataset dataset = new DefaultPieDataset();

        dataset.setValue( "Passed" , new Integer( totalNotFound ) );
        dataset.setValue( "Defect" , new Integer( totalFound ) );
        dataset.setValue( "Ignored" , new Double( totalIgnored ) );
        dataset.setValue( "Disabled" , new Double( totalDisabled ) );

        return dataset;
    }

    /**
     * Generates a PieChart based on a specific dataset
     * @param dataset
     * @return
     */
    private static JFreeChart createPieChart(final PieDataset dataset) {
        final JFreeChart chart = ChartFactory.createPieChart("Lint Pie Chart", dataset, true, true, false);
        chart.setBackgroundPaint(Color.WHITE);
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        chart.getPlot().setOutlinePaint(null);
        PiePlot plot = (PiePlot) chart.getPlot();

        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} - {1} ({2})"));

        plot.setSectionPaint(0, passed());
        plot.setSectionPaint(1, defect());
        plot.setSectionPaint(2, ignored());
        plot.setSectionPaint(3, disabled());
        return chart;
    }

    /**
     * Generates a dataset based on the overall lint defects
     * @param jobSet
     * @return
     */
    private static PieDataset createSeverityPieDataset(Enumeration<Job> jobSet, Hashtable<String, InterfaceCheck> checkSet) {
        int totalHigh = 0;
        int totalMedium = 0;
        int totalLow = 0;

        for (Job key : Collections.list(jobSet)) {
            for (Lint lint : key.getLintList()) {
                if (lint.isEnabled() && !lint.isIgnored() && lint.isFound()) {
                    switch (checkSet.get(lint.getName()).getSeverity()) {
                        case "High":
                            totalHigh++;
                            break;
                        case "Medium":
                            totalMedium++;
                            break;
                        case "Low":
                            totalLow++;
                            break;
                    }
                }
            }
        }
        DefaultPieDataset dataset = new DefaultPieDataset();

        dataset.setValue( "High" , new Integer( totalHigh ) );
        dataset.setValue( "Medium" , new Integer( totalMedium ) );
        dataset.setValue( "Low" , new Double( totalLow ) );

        return dataset;
    }

    /**
     * Generates a PieChart based on a specific dataset
     * @param dataset
     * @return
     */
    private static JFreeChart createSeverityPieChart(final PieDataset dataset) {
        final JFreeChart chart = ChartFactory.createPieChart("Severity Pie Chart", dataset, true, true, false);
        chart.setBackgroundPaint(Color.WHITE);
        chart.getPlot().setBackgroundPaint(Color.WHITE);
        chart.getPlot().setOutlinePaint(null);
        PiePlot plot = (PiePlot) chart.getPlot();

        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} - {1} ({2})"));

        plot.setSectionPaint(0, high());
        plot.setSectionPaint(1, medium());
        plot.setSectionPaint(2, low());
        return chart;
    }

    private static Paint disabled() {
        Color color = new Color(0xF0, 0xF0, 0xF0);
        return new GradientPaint(0.0f, 0.0f, color, 0.0f, 0.0f, color);
    }

    private static Paint ignored() {
        Color color = new Color(0xF9, 0xEF, 0x9E);
        return new GradientPaint(0.0f, 0.0f, color, 0.0f, 0.0f, color);
    }

    private static Paint defect() {
        Color color = new Color(0xFF, 0x85, 0x66);
        return new GradientPaint(0.0f, 0.0f, color, 0.0f, 0.0f, color);
    }

    private static Paint passed() {
        Color color = new Color(0xB8, 0xEE, 0xB8);
        return new GradientPaint(0.0f, 0.0f, color, 0.0f, 0.0f, color);
    }


    private static Paint high() {
        Color color = new Color(0xFF, 0x85, 0x66);
        return new GradientPaint(0.0f, 0.0f, color, 0.0f, 0.0f, color);
    }

    private static Paint medium() {
        Color color = new Color(0xF9, 0xEF, 0x9E);
        return new GradientPaint(0.0f, 0.0f, color, 0.0f, 0.0f, color);
    }

    private static Paint low() {
        Color color = new Color(0xB8, 0xEE, 0xB8);
        return new GradientPaint(0.0f, 0.0f, color, 0.0f, 0.0f, color);
    }

    private static class LintGraphData {
        private String name;
        private int totalFound = 0;
        private int totalNotFound = 0;
        private int totalIgnored = 0;
        private int totalDisabled = 0;

        public LintGraphData(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public int getTotalFound() {
            return totalFound;
        }

        public void incTotalFound() {
            this.totalFound++;
        }

        public int getTotalNotFound() {
            return totalNotFound;
        }

        public void incTotalNotFound() {
            this.totalNotFound++;
        }

        public int getTotalIgnored() {
            return totalIgnored;
        }

        public void incTotalIgnored() {
            this.totalIgnored++;
        }

        public int getTotalDisabled() {
            return totalDisabled;
        }

        public void incTotalDisabled() {
            this.totalDisabled++;
        }
    }
}