package com.fire;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;

public class Utils {

  Utils() {
  }

  /**
   * Loads image from filename into a Color (pixels decribed with rgb values)
   * matrix.
   * 
   * @param filename the name of the imge in the filesystem.
   * @return Color matrix.
   */
  public static Color[][] loadImage(String filename) {
    BufferedImage buffImg = loadImageFile(filename);
    Color[][] colorImg = convertTo2DFromBuffered(buffImg);
    return colorImg;
  }

  /**
   * Converts image from a Color matrix to a .jpg file.
   * 
   * @param image    the matrix of Color objects.
   * @param filename to the image.
   */
  public static void writeImage(Color[][] image, String filename) {
    File outputfile = new File(filename);
    BufferedImage bufferedImage = Utils.matrixToBuffered(image);
    try {
      ImageIO.write(bufferedImage, "jpg", outputfile);
    } catch (IOException e) {
      System.out.println("Could not write image " + filename + " !");
      e.printStackTrace();
      System.exit(1);
    }
  }

  public static List<Color[][]> splitImage(Color[][] image, int parts) throws IOException {
    List<Color[][]> splitImages = new ArrayList<>();
    int rows = image.length;
    int columns = image[0].length;
    int partSize = rows / parts;
    Color[][] newImagePart = new Color[partSize][columns];
    int cont = 0;
    for (int i = 0; i < image.length; i++) {
      for (int j = 0; j < image[i].length; j++) {
        Color pixel = image[i][j];
        newImagePart[cont][j] = pixel;
      }

      if (i != 0 && (i + 1) % partSize == 0) {
        splitImages.add(newImagePart);
        newImagePart = new Color[partSize][columns];
        cont = 0;
      } else {
        cont++;
      }
    }
    return splitImages;
  }

  public static Color[][] Join(List<Color[][]> images) {
    int nImages = images.size();
    int nRows = images.get(0).length;
    int nColumns = nImages * images.get(0)[0].length;
    Color[][] finalImage = new Color[nRows][nColumns];
    finalImage = appendArrays(images.get(0), images.get(1));
    if (images.size() > 2) {
      for (int i = 2; i < nImages; i++) {
        finalImage = appendArrays(finalImage, images.get(i));
      }
    }

    return finalImage;
  }

  private static Color[][] appendArrays(Color[][] array1, Color[][] array2) {
    Color[][] ret = new Color[array1.length + array2.length][];
    int i = 0;
    for (; i < array1.length; i++) {
      ret[i] = array1[i];
    }
    for (int j = 0; j < array2.length; j++) {
      ret[i++] = array2[j];
    }
    return ret;
  }

  /**
   * Loads in a BufferedImage from the specified path to be processed.
   * 
   * @param filename The path to the file to read.
   * @return a BufferedImage if able to be read, NULL otherwise.
   */
  private static BufferedImage loadImageFile(String filename) {
    BufferedImage img = null;
    try {
      img = ImageIO.read(new File(filename));
    } catch (IOException e) {
      System.out.println("Could not load image " + filename + " !");
      e.printStackTrace();
      System.exit(1);
    }
    return img;
  }

  /**
   * Copy a Color matrix to another Color matrix.
   * Useful if one does not want to modify the original image.
   * 
   * @param image the source matrix
   * @return a copy of the image
   */

  public static Color[][] copyImage(Color[][] image) {
    Color[][] copy = new Color[image.length][image[0].length];
    for (int i = 0; i < image.length; i++) {
      for (int j = 0; j < image[i].length; j++) {
        copy[i][j] = image[i][j];
      }
    }
    return copy;
  }

  /**
   * Converts a matrix of Colors into a BufferedImage to
   * write on the filesystem.
   * 
   * @param image the matrix of Colors
   * @return the image ready for writing to filesystem
   */
  private static BufferedImage matrixToBuffered(Color[][] image) {
    int width = image.length;
    int height = image[0].length;
    BufferedImage bImg = new BufferedImage(width, height, 1);

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        bImg.setRGB(x, y, image[x][y].getRGB());
      }
    }
    return bImg;
  }

  /**
   * Converts a file loaded into a BufferedImage to a
   * matrix of Colors
   * 
   * @param image the BufferedImage to convert
   * @return the matrix of Colors
   */

  private static Color[][] convertTo2DFromBuffered(BufferedImage image) {
    int width = image.getWidth();
    int height = image.getHeight();
    Color[][] result = new Color[width][height];

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        // Get the integer RGB, and separate it into individual components.
        // (BufferedImage saves RGB as a single integer value).
        int pixel = image.getRGB(x, y);
        // int alpha = (pixel >> 24) & 0xFF;
        int red = (pixel >> 16) & 0xFF;
        int green = (pixel >> 8) & 0xFF;
        int blue = pixel & 0xFF;
        result[x][y] = new Color(red, green, blue);
      }
    }
    return result;
  }

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

    // Helper function to compress a string using gzip
    public static byte[] compressString(String input) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
            gzipOutputStream.write(input.getBytes("UTF-8"));
        }
        return outputStream.toByteArray();
    }
}