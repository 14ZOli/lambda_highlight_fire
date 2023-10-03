package com.fire;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

/**
 * Creating image filters
 * 
 * @author Jorge Coelho
 * @contact jmn@isep.ipp.pt
 * @version 1.0
 * @since 2022-01-04
 */
public class FiltersCleanImage {

    List<Color[][]> images;
    int IMAGES_SIZE;

    FiltersCleanImage(List<Color[][]> images1, int size) {
        this.images = images1;
        IMAGES_SIZE = size;
    }

    public Color[][] mergeImages(String outputFile, float threshold) throws IOException {
        Color[][] tmp = Utils.copyImage(images.get(0));
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {
                int sum_r = 0;
                int sum_g = 0;
                int sum_b = 0;
                for (Color[][] color : images) {
                    Color pixel = color[i][j];
                    sum_r += pixel.getRed();
                    sum_g += pixel.getGreen();
                    sum_b += pixel.getBlue();
                }
                int avg_red = sum_r / IMAGES_SIZE;
                int avg_green = sum_g / IMAGES_SIZE;
                int avg_blue = sum_b / IMAGES_SIZE;
                double min_dist = 256;
                int final_r = 0;
                int final_g = 0;
                int final_b = 0;
                for (Color[][] color : images) {
                    Color pixel = color[i][j];
                    int r = pixel.getRed();
                    int g = pixel.getGreen();
                    int b = pixel.getBlue();
                    double dist = calcDist(r, g, b, avg_red, avg_green, avg_blue);
                    if (dist < min_dist) {
                        min_dist = dist;
                        final_r = r;
                        final_g = g;
                        final_b = b;
                    }
                }

                tmp[i][j] = new Color(final_r, final_g, final_b);
            }
        }
        Utils.writeImage(tmp, outputFile);
        return tmp;
    }

    private double calcDist(int red, int green, int blue, int avg_red, int avg_green, int avg_blue) {
        double body = Math.pow(red - avg_red, 2) + Math.pow(green - avg_green, 2) + Math.pow(blue - avg_blue, 2);
        return Math.sqrt(body);
    }

}
