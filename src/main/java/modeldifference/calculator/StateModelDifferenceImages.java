package modeldifference.calculator;

import modeldifference.models.AbstractStateId;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.swing.text.html.Option;

public class StateModelDifferenceImages {
    private StateModelDifferenceImages() {}


    private static BufferedImage createImageFromBytes(byte[] imageData) {
        var bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Compare two images to create a third one that highlights the differences between them.
     *
     * https://stackoverflow.com/questions/25022578/highlight-differences-between-images
     */
    public static Optional<Path> getDifferenceImage(byte[] image1, AbstractStateId idImg1, byte[] image2, AbstractStateId idImg2, Path modelDifferenceReportDirectory) {
        try {

            var img1 = createImageFromBytes(image1);
            var img2 = createImageFromBytes(image2);

            int width1 = img1.getWidth(); // Change - getWidth() and getHeight() for BufferedImage
            int width2 = img2.getWidth(); // take no arguments
            int height1 = img1.getHeight();
            int height2 = img2.getHeight();
            if ((width1 != width2) || (height1 != height2)) {
                System.err.println("Error: Images dimensions mismatch");
                System.exit(1);
            }

            // NEW - Create output Buffered image of type RGB
            BufferedImage outImg = new BufferedImage(width1, height1, BufferedImage.TYPE_INT_RGB);

            // Modified - Changed to int as pixels are ints
            int diff;
            int result; // Stores output pixel
            for (int i = 0; i < height1; i++) {
                for (int j = 0; j < width1; j++) {
                    int rgb1 = img1.getRGB(j, i);
                    int rgb2 = img2.getRGB(j, i);
                    int r1 = (rgb1 >> 16) & 0xff;
                    int g1 = (rgb1 >> 8) & 0xff;
                    int b1 = (rgb1) & 0xff;
                    int r2 = (rgb2 >> 16) & 0xff;
                    int g2 = (rgb2 >> 8) & 0xff;
                    int b2 = (rgb2) & 0xff;
                    diff = Math.abs(r1 - r2); // Change
                    diff += Math.abs(g1 - g2);
                    diff += Math.abs(b1 - b2);
                    diff /= 3; // Change - Ensure result is between 0 - 255
                    // Make the difference image gray scale
                    // The RGB components are all the same
                    result = (diff << 16) | (diff << 8) | diff;
                    outImg.setRGB(j, i, result); // Set result
                }
            }

            // Now save the image on disk

            // see if we have a directory for the screenshots yet
            File screenshotDir = new File(modelDifferenceReportDirectory + File.separator);

            // save the file to disk
            String imageName = "diff_"+ idImg1.getValue() + "_" + idImg2.getValue() + ".png";
            File screenshotFile = new File(screenshotDir, imageName);
            if (screenshotFile.exists()) {
                return Optional.of(screenshotFile.toPath());
            }
            FileOutputStream outputStream = new FileOutputStream(screenshotFile.getCanonicalPath());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(outImg, "png", baos);
            byte[] bytes = baos.toByteArray();

            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();

            return Optional.of(screenshotFile.toPath());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
