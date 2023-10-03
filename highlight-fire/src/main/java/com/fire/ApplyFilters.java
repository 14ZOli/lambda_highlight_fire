package com.fire;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
public class ApplyFilters {

    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner(System.in);
        List<String> filePaths = new ArrayList<>();
        System.out.println("1\t Local execution");
        System.out.println("2\t Lambda");
        System.out.println("Please enter your choice:");
        int choice = in.nextInt();
        String filePath = "";
        String outputPath = "";
        float thresholdFire;
        int nThreads = 10;
        long startTime = 0, endTime = 0;
        System.out.println("Insert the name of the file path you would like to use:");
        filePath = "/Users/joseoliveira/workspace/isep/highlight-fire/HighlightFires/russia1.jpg";
        System.out.println("Insert the folder path you would like to save your image on:");
        outputPath = "/Users/joseoliveira/workspace/isep/highlight-fire/output";
        System.out.println("Insert the red value threshold:");
        thresholdFire = in.nextFloat();
        in.close();
        switch (choice) {
                case 1:
                        startTime = System.currentTimeMillis();
                        processHighlighFireLocal(filePath,nThreads,thresholdFire,outputPath);
                        endTime = System.currentTimeMillis();
                        break;
                case 2:
                        startTime = System.currentTimeMillis();
                        processHighlighFireLambda(filePath,nThreads,thresholdFire,outputPath);
                        endTime = System.currentTimeMillis();
                        break;
                default:
                        System.out.println("Invalid choice");

                }
        long elapsedTime = endTime - startTime;
        System.out.println("Elapsed Time (Milliseconds): " + elapsedTime);
        
    }

    public static void processCleanImage(List<String> filePaths, int nThreads, float threshold, String outputPath) {
        List<Color[][]> image1Parts = new ArrayList<>();
        List<Color[][]> image2Parts = new ArrayList<>();
        List<Color[][]> image3Parts = new ArrayList<>();
        try {
                image1Parts = Utils.splitImage(Utils.loadImage(filePaths.get(0)),nThreads);
                image2Parts = Utils.splitImage(Utils.loadImage(filePaths.get(1)),nThreads);
                image3Parts = Utils.splitImage(Utils.loadImage(filePaths.get(2)),nThreads);
        } catch(Exception e){

        }
        List<Color[][]> processedImages = new ArrayList<>();
        List<FutureTask<Color[][]>> processors = new ArrayList<>();
        for(int i=0; i<nThreads; i++) {
                List<Color[][]> tmp = new ArrayList<>();
                tmp.add(image1Parts.get(i));tmp.add(image2Parts.get(i));tmp.add(image3Parts.get(i));
                Callable<Color[][]> processor = new LocalProcessor(tmp, outputPath + "/cleanImage-Splited-Processed" + i +".jpg", 1, threshold);
                FutureTask<Color[][]> futureTask = new FutureTask<>(processor);
                new Thread(futureTask).start();
                processors.add(futureTask);
        }
        for (FutureTask<Color[][]> p : processors) {
                try {
                        processedImages.add(p.get());
                } catch(Exception e) {
                        System.out.println("Exception:" + e);
                } 
        }
        
        Color[][] image = Utils.Join(processedImages);
        Utils.writeImage(image, "test.jpg" /*outputPath+ "/highlight-Processed.jpg"*/);
    }

    public static void processHighlighFireLambda(String filePath, int nThreads, float thresholdFire, String outputPath) {
        List<Color[][]> processedImages = new ArrayList<>();
        List<Color[][]> imageParts = new ArrayList<>();
        
        try {
                imageParts = Utils.splitImage(Utils.loadImage(filePath),nThreads);
        } catch(Exception e) {

        }
        
        List<FutureTask<Color[][]>> processors = new ArrayList<>();
        for(int i=0; i<nThreads; i++) {
                List<Color[][]> tmp = new ArrayList<>();
                tmp.add(imageParts.get(i));
                
                Callable<Color[][]> processor = new LambdaProcessor(tmp, outputPath + "/highlight-Splited-Processed" + i +".jpg", 2, thresholdFire);
                FutureTask<Color[][]> futureTask = new FutureTask<>(processor);
                new Thread(futureTask).start();
                processors.add(futureTask);
        }

        for (FutureTask<Color[][]> p : processors) {
                try {
                        processedImages.add(p.get());
                } catch(Exception e) {
                        System.out.println("Exception:" + e);
                } 
        }

        
        Color[][] image = Utils.Join(processedImages);
        Utils.writeImage(image, "transformed-image.jpg" /*outputPath+ "/highlight-Processed.jpg"*/);
    }

    public static void processHighlighFireLocal(String filePath, int nThreads, float thresholdFire, String outputPath) {
        List<Color[][]> processedImages = new ArrayList<>();
        List<Color[][]> imageParts = new ArrayList<>();
        
        try {
                imageParts = Utils.splitImage(Utils.loadImage(filePath),nThreads);
        } catch(Exception e) {

        }
        
        List<FutureTask<Color[][]>> processors = new ArrayList<>();
        for(int i=0; i<nThreads; i++) {
                List<Color[][]> tmp = new ArrayList<>();
                tmp.add(imageParts.get(i));
                
                Callable<Color[][]> processor = new LocalProcessor(tmp, outputPath + "/highlight-Splited-Processed" + i +".jpg", 1, thresholdFire);
                FutureTask<Color[][]> futureTask = new FutureTask<>(processor);
                new Thread(futureTask).start();
                processors.add(futureTask);
        }

        for (FutureTask<Color[][]> p : processors) {
                try {
                        processedImages.add(p.get());
                } catch(Exception e) {
                        System.out.println("Exception:" + e);
                } 
        }

        
        Color[][] image = Utils.Join(processedImages);
        Utils.writeImage(image, "transformed-image.jpg" /*outputPath+ "/highlight-Processed.jpg"*/);
    }
}
