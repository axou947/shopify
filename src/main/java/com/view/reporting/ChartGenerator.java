package main.java.com.view.reporting;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Classe utilitaire pour générer des graphiques
 */
public class ChartGenerator {

    /**
     * Crée un graphique camembert
     * @param title Titre du graphique
     * @param data Données à afficher (clé = nom de section, valeur = valeur)
     * @return Un panel contenant le graphique
     */
    public static ChartPanel createPieChart(String title, Map<String, Double> data) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Ajout des données au dataset
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        // Création du graphique
        JFreeChart chart = ChartFactory.createPieChart(
                title,      // Titre
                dataset,    // Données
                true,       // Légende
                true,       // Tooltips
                false       // URLs
        );

        // Personnalisation du graphique
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setOutlinePaint(Color.lightGray);

        // Création du panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 400));
        return chartPanel;
    }

    /**
     * Crée un graphique à barres
     * @param title Titre du graphique
     * @param categoryAxisLabel Libellé de l'axe des catégories
     * @param valueAxisLabel Libellé de l'axe des valeurs
     * @param data Données à afficher (clé = nom de catégorie, valeur = valeur)
     * @return Un panel contenant le graphique
     */
    public static ChartPanel createBarChart(String title, String categoryAxisLabel,
                                            String valueAxisLabel, Map<String, Double> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Ajout des données au dataset
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            dataset.addValue(entry.getValue(), "Valeur", entry.getKey());
        }

        // Création du graphique
        JFreeChart chart = ChartFactory.createBarChart(
                title,               // Titre
                categoryAxisLabel,   // Libellé axe des catégories
                valueAxisLabel,      // Libellé axe des valeurs
                dataset,             // Données
                PlotOrientation.VERTICAL,  // Orientation
                true,                // Légende
                true,                // Tooltips
                false                // URLs
        );

        // Personnalisation du graphique
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setOutlinePaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(79, 129, 189));

        // Création du panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 400));
        return chartPanel;
    }

    /**
     * Crée un graphique à lignes
     * @param title Titre du graphique
     * @param categoryAxisLabel Libellé de l'axe des catégories
     * @param valueAxisLabel Libellé de l'axe des valeurs
     * @param data Données à afficher (clé = nom de série, valeurs = séries de données)
     * @param categories Liste des catégories
     * @return Un panel contenant le graphique
     */
    public static ChartPanel createLineChart(String title, String categoryAxisLabel,
                                             String valueAxisLabel, Map<String, List<Double>> data,
                                             List<String> categories) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Ajout des données au dataset
        for (Map.Entry<String, List<Double>> entry : data.entrySet()) {
            String series = entry.getKey();
            List<Double> values = entry.getValue();

            for (int i = 0; i < values.size() && i < categories.size(); i++) {
                dataset.addValue(values.get(i), series, categories.get(i));
            }
        }

        // Création du graphique
        JFreeChart chart = ChartFactory.createLineChart(
                title,               // Titre
                categoryAxisLabel,   // Libellé axe des catégories
                valueAxisLabel,      // Libellé axe des valeurs
                dataset,             // Données
                PlotOrientation.VERTICAL,  // Orientation
                true,                // Légende
                true,                // Tooltips
                false                // URLs
        );

        // Personnalisation du graphique
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setOutlinePaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.lightGray);

        // Création du panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 400));
        return chartPanel;
    }
}