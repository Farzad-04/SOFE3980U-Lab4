package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;
import java.util.Arrays;

/**
 * Evaluate Multi-Class Classification
 */
public class App {
    public static void main(String[] args) {
        String filePath = "model.csv";
        int numClasses = 5;
        int[][] confusionMatrix = new int[numClasses][numClasses];
        double ce = 0;
        int totalSamples = 0;

        try {
            FileReader filereader = new FileReader(filePath);
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
            List<String[]> allData = csvReader.readAll();
            csvReader.close();

            for (String[] row : allData) {
                int y_true = Integer.parseInt(row[0]) - 1; // Convert 1-based index to 0-based
                double[] y_pred = new double[numClasses];
                for (int i = 0; i < numClasses; i++) {
                    y_pred[i] = Double.parseDouble(row[i + 1]);
                }

                // Compute Cross-Entropy
                ce += -Math.log(y_pred[y_true]);

                // Find predicted class (argmax)
                int y_hat = 0;
                double maxProb = y_pred[0];
                for (int i = 1; i < numClasses; i++) {
                    if (y_pred[i] > maxProb) {
                        maxProb = y_pred[i];
                        y_hat = i;
                    }
                }

                // Update confusion matrix
                confusionMatrix[y_hat][y_true]++;
                totalSamples++;
            }
        } catch (Exception e) {
            System.out.println("Error reading the CSV file.");
            return;
        }

        ce /= totalSamples; // Compute mean Cross-Entropy
        System.out.printf("CE = %.7f\n", ce);
        
        // Print Confusion Matrix
        System.out.println("Confusion Matrix:");
        System.out.printf("%12s%8s%8s%8s%8s%8s\n", "", "y=1", "y=2", "y=3", "y=4", "y=5");
        for (int i = 0; i < numClasses; i++) {
            System.out.printf("y^=%d", i + 1);
            for (int j = 0; j < numClasses; j++) {
                System.out.printf("%8d", confusionMatrix[i][j]);
            }
            System.out.println();
        }
    }
}
