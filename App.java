package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;

/**
 * Evaluate Classification Model Performance
 */
public class App {
    public static void main(String[] args) {
        // List of model files
        String[] modelFiles = {"model_1.csv", "model_2.csv", "model_3.csv"};
        double bestBCE = Double.MAX_VALUE, bestAccuracy = 0, bestPrecision = 0, bestRecall = 0, bestF1 = 0, bestAUC = 0;
        String bestBCEModel = "", bestAccuracyModel = "", bestPrecisionModel = "", bestRecallModel = "", bestF1Model = "", bestAUCModel = "";

        for (String filePath : modelFiles) {
            try {
                FileReader filereader = new FileReader(filePath);
                CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
                List<String[]> allData = csvReader.readAll();
                csvReader.close();

                double bce = 0;
                int tp = 0, tn = 0, fp = 0, fn = 0;
                int n = allData.size();

                for (String[] row : allData) {
                    int y_true = Integer.parseInt(row[0]);
                    double y_predicted = Double.parseDouble(row[1]);
                    int y_hat = (y_predicted >= 0.5) ? 1 : 0;

                    // Compute BCE
                    bce += -(y_true * Math.log(y_predicted) + (1 - y_true) * Math.log(1 - y_predicted));

                    // Compute confusion matrix
                    if (y_true == 1 && y_hat == 1) tp++;
                    if (y_true == 0 && y_hat == 0) tn++;
                    if (y_true == 0 && y_hat == 1) fp++;
                    if (y_true == 1 && y_hat == 0) fn++;
                }

                bce /= n;
                double accuracy = (double) (tp + tn) / n;
                double precision = (tp + fp) > 0 ? (double) tp / (tp + fp) : 0;
                double recall = (tp + fn) > 0 ? (double) tp / (tp + fn) : 0;
                double f1 = (precision + recall) > 0 ? 2 * (precision * recall) / (precision + recall) : 0;
                double auc = (accuracy + recall) / 2;  // Approximate AUC-ROC

                System.out.println("For " + filePath);
                System.out.printf("\tBCE = %.7f\n", bce);
                System.out.println("\tConfusion Matrix:");
                System.out.println("\t\t y=1      y=0");
                System.out.printf("\ty^=1    %d    %d\n", tp, fp);
                System.out.printf("\ty^=0    %d    %d\n", fn, tn);
                System.out.printf("\tAccuracy = %.4f\n", accuracy);
                System.out.printf("\tPrecision = %.8f\n", precision);
                System.out.printf("\tRecall = %.8f\n", recall);
                System.out.printf("\tF1 Score = %.8f\n", f1);
                System.out.printf("\tAUC-ROC = %.8f\n", auc);

                // Find best model for each metric
                if (bce < bestBCE) {
                    bestBCE = bce;
                    bestBCEModel = filePath;
                }
                if (accuracy > bestAccuracy) {
                    bestAccuracy = accuracy;
                    bestAccuracyModel = filePath;
                }
                if (precision > bestPrecision) {
                    bestPrecision = precision;
                    bestPrecisionModel = filePath;
                }
                if (recall > bestRecall) {
                    bestRecall = recall;
                    bestRecallModel = filePath;
                }
                if (f1 > bestF1) {
                    bestF1 = f1;
                    bestF1Model = filePath;
                }
                if (auc > bestAUC) {
                    bestAUC = auc;
                    bestAUCModel = filePath;
                }

            } catch (Exception e) {
                System.out.println("Error reading the CSV file: " + filePath);
            }
        }

        // Output the best model for each metric
        System.out.println("\nAccording to BCE, the best model is " + bestBCEModel);
        System.out.println("According to Accuracy, the best model is " + bestAccuracyModel);
        System.out.println("According to Precision, the best model is " + bestPrecisionModel);
        System.out.println("According to Recall, the best model is " + bestRecallModel);
        System.out.println("According to F1 Score, the best model is " + bestF1Model);
        System.out.println("According to AUC ROC, the best model is " + bestAUCModel);
    }
}
