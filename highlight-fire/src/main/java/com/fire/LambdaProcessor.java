package com.fire;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.Callable;

public class LambdaProcessor implements Callable<Color[][]>{

    Color[][] image;
    List<Color[][]> images;

    String outputFilePath;
    int processType; //1 - Cleanimage   2 - Highlight Fire
    int threadNumber;
    float threshold;

    public LambdaProcessor(List<Color[][]> images, String outputFilePath, int processType, float threshold) {
        this.images= images;
        this.processType=processType;
        this.threshold = threshold;
        this.outputFilePath = outputFilePath;
    }

    @Override
    public Color[][] call() throws Exception {
        Color[][] result = null;
        if(processType==1) { // Clean Image
            FiltersCleanImage filter1 = new FiltersCleanImage(images, images.size());
            
            result = filter1.mergeImages(outputFilePath, threshold);
            
        } else { // Highlight Fire
            FiltersHighlightFire filter2 = new FiltersHighlightFire(images.get(0));
            
            result = filter2.HighLightFireFilter(outputFilePath, threshold);
            Utils.writeImage(result, outputFilePath);
        }
        return result;
    }
    
}
