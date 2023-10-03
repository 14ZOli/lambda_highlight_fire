package com.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.awt.Color;
import java.io.IOException;

public class FiltersHighlightFireLambda implements RequestHandler<InputType, String> {

    @Override
    public String handleRequest(InputType input, Context context) {
        try {
            // Extract input parameters
            float threshold = input.getThreshold();
            String imageStr = input.getImageStr();

            // deserialize the base64 color array
            System.out.println("This is the threshold: " + threshold);
            System.out.println("This is the imageStr: " + imageStr);
            Color[][] image = Utils.base64StringToColorArray(imageStr);
            
            // Create an instance of FiltersHighlightFire and invoke HighLightFireFilter
            FiltersHighlightFire filter = new FiltersHighlightFire(image);
            Color[][] colorArray = filter.HighLightFireFilter(threshold);

            String result = Utils.colorArrayToBase64String(colorArray);
            // Create and return the output object
            
            return result;
        } catch (IOException e) {
            // Log the error
            context.getLogger().log("Error processing image: " + e.getMessage());
            // Throw a runtime exception to indicate the error
            throw new RuntimeException(e);
        } catch (Exception e) {
            // Throw a runtime exception to indicate the error
            context.getLogger().log("Unexpected error: " + e.getMessage());
            return null;
        }
    }
}
