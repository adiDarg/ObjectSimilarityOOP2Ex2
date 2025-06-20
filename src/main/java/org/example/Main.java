package org.example;

import Features.FeatureReader;
import Features.FeaturesObject;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        FeatureReader reader = new FeatureReader();
        System.out.println("Please choose a CSV or XLSX File");
        boolean validFile = false;
        while (!validFile) {
            validFile = true;
            reader.chooseFile();
            try {
                reader.read();
                List<FeaturesObject> featuresObjects = reader.convertToFeatures();
                program(featuresObjects);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                validFile = false;
            }
        }
    }
    public static void program(List<FeaturesObject> featuresObjects) {
        Scanner scanner = new Scanner(System.in);
        while (true){
            int index = 1;
            for (FeaturesObject featuresObject : featuresObjects) {
                System.out.println(index + ". " + featuresObject);
                index++;
            }
            System.out.println("Choose objects by number to compare(enter -1 to end program): ");
            try {
                int index1 = scanner.nextInt();
                if (index1 == -1) {
                    break;
                }
                int index2 = scanner.nextInt();
                FeaturesObject featuresObject1 = featuresObjects.get(index1 - 1);
                FeaturesObject featuresObject2 = featuresObjects.get(index2 - 1);
                if (featuresObject1 == null || featuresObject2 == null) {
                    System.out.println("Please choose valid objects");
                    continue;
                }
                System.out.println("Similarity: " +
                        FeaturesObject.compareFeatures(featuresObject1, featuresObject2) +
                        "/7");
            }
            catch (Exception e) {
                System.out.println("Please enter valid indexes");
            }
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //Clear Screen
            for (int i = 1; i <= 30; i++){
                System.out.println();
            }
        }

    }
}