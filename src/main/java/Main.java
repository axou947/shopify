package main.java;

import main.java.com.view.common.MainFrame;

import javax.swing.*;

/**
 * Classe principale de l'application
 */
public class Main {
    /**
     * Point d'entrée de l'application
     * @param args Arguments de ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        // Assure que l'interface graphique s'exécute sur l'EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Utilise le look and feel du système
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Crée et affiche la fenêtre principale
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });
    }
}
