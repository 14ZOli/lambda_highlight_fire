package com.example;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.image.BufferedImage;


public class Utils {

  public static String colorArrayToBase64String(Color[][] image) {
        try {
            // Create a BufferedImage from the Color[][] array
            int width = image.length;
            int height = image[0].length;
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bufferedImage.setRGB(x, y, image[x][y].getRGB());
                }
            }

            // Convert BufferedImage to byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();
            
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Color[][] base64StringToColorArray(String base64String) {
        try {
            // Decode the base64 string to a byte array
            byte[] imageBytes = Base64.getDecoder().decode(base64String);

            // Create a BufferedImage from the byte array
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);

            // Get the width and height of the image
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            // Create a Color[][] array to store the colors
            Color[][] colorArray = new Color[width][height];

            // Populate the Color[][] array with colors from the BufferedImage
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int rgb = bufferedImage.getRGB(x, y);
                    colorArray[x][y] = new Color(rgb);
                }
            }

            return colorArray;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
