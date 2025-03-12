package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;

/**
 * Evaluate Single Variable Continuous Regression
 */
public class App {
    public static void main(String[] args) {
        // List of model files
        String[] modelFiles = {"model_1.csv", "model_2.csv", "model_3.csv"};
        double bestMSE = Double.MAX_VALUE, bestMAE = Double.MAX_VALUE, bestMARE = Double.MAX_VALUE;
        String bestMSEModel = "", bestMAEModel = "", bestMAREModel = "";

        for (String filePath : modelFiles) {
            try {
                FileReader filereader = new FileReader(filePath);
                CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
                List<String[]> allData = csvReader.readAll();
                csvReader.close();

                double mse = 0, mae = 0, mare = 0;
                int n = allData.size();

                for (String[] row : allData) {
                    double y_true = Double.parseDouble(row[0]);
                    double y_predicted = Double.parseDouble(row[1]);

                    double error = y_true - y_predicted;
                    mse += error * error;
                    mae += Math.abs(error);
                    mare += Math.abs(error) / y_true;
                }

                mse /= n;
                mae /= n;
                mare /= n;

                System.out.println("For " + filePath);
                System.out.printf("\tMSE = %.5f\n", mse);
                System.out.printf("\tMAE = %.5f\n", mae);
                System.out.printf("\tMARE = %.8f\n", mare);

                // Find best model for each metric
                if (mse < bestMSE) {
                    bestMSE = mse;
                    bestMSEModel = filePath;
                }
                if (mae < bestMAE) {
                    bestMAE = mae;
                    bestMAEModel = filePath;
                }
                if (mare < bestMARE) {
                    bestMARE = mare;
                    bestMAREModel = filePath;
                }

            } catch (Exception e) {
                System.out.println("Error reading the CSV file: " + filePath);
            }
        }

        // Output the best model for each metric
        System.out.println("\nAccording to MSE, the best model is " + bestMSEModel);
        System.out.println("According to MAE, the best model is " + bestMAEModel);
        System.out.println("According to MARE, the best model is " + bestMAREModel);
    }
}
