package com.example;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


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
    public Color[][] HighLightFireFilter(float threshold) throws IOException {
        Color[][] tmp = copyImage(image);
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {
                // fetches values of each pixel
                Color pixel = tmp[i][j];
                int r = pixel.getRed();
                int g = pixel.getGreen();
                int b = pixel.getBlue();
                // takes average of color values
                int avg = (r + g + b) / 3;
                if (r > avg * threshold && g < 100 && b < 200)
                    // outputs red pixel
                    tmp[i][j] = new Color(255, 0, 0);
                else
                    tmp[i][j] = new Color(avg, avg, avg);

            }
        }
        return tmp;
    }

    public static Color[][] copyImage(Color[][] image) {
        Color[][] copy = new Color[image.length][image[0].length];
        for (int i = 0; i < image.length; i++) {
          for (int j = 0; j < image[i].length; j++) {
            copy[i][j] = image[i][j];
          }
        }
        return copy;
      }

}
