package com.fire;

import java.io.IOException;
import java.awt.Color;

/**
 * Creating image filters
 * 
 * @author Jorge Coelho
 * @contact jmn@isep.ipp.pt
 * @version 1.0
 * @since 2022-01-04
 */
public class FiltersHighlightFire {

    String file;
    Color image[][];

    FiltersHighlightFire(Color[][] image) {
        this.image = image;
    }

    // Highlight Fires.
    public Color[][] HighLightFireFilter(String outputFile, float threshold) throws IOException {
        String encodedString = Utils.colorArrayToBase64String(image);

        LambdaInvoker lambdaInvoker = new LambdaInvoker();

        String response = lambdaInvoker.invokeLambda(encodedString, threshold);

        Color[][] decodedImage = Utils.base64StringToColorArray(response);
        return decodedImage;
    }

}
