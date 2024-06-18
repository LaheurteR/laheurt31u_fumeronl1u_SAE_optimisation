import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class Flou
{
    public static double ecartType;

    public static void flouMoyenne(BufferedImage image)
    {
        int windowSize = 5; // Taille de la fenêtre de pixels
        int offset = windowSize / 2;
        BufferedImage imageFlouMoyenne = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < image.getWidth(); i++)
        {
            for (int j = 0; j < image.getHeight(); j++)
            {
                int sumRed = 0;
                int sumGreen = 0;
                int sumBlue = 0;
                int count = 0;

                for (int k = Math.max(0, i - offset); k <= Math.min(i + offset, image.getWidth() - 1); k++)
                {
                    for (int l = Math.max(0, j - offset); l <= Math.min(j + offset, image.getHeight() - 1); l++)
                    {
                        int pixel = image.getRGB(k, l);
                        sumRed += (pixel >> 16) & 0xff;
                        sumGreen += (pixel >> 8) & 0xff;
                        sumBlue += pixel & 0xff;
                        count++;
                    }
                }

                int avgRed = sumRed / count;
                int avgGreen = sumGreen / count;
                int avgBlue = sumBlue / count;
                int avgColor = (avgRed << 16) | (avgGreen << 8) | avgBlue;

                imageFlouMoyenne.setRGB(i, j, avgColor);
            }
        }
        try
        {
            File outputfile = new File("images/floumoy.jpg");
            ImageIO.write(imageFlouMoyenne, "jpg", outputfile);
        } catch (IOException e)
        {
            System.out.println("Erreur lors de l'écriture de l'image");
        }
    }


    public static void flouGaussien(BufferedImage image)
    {
        int radius = 3;
        float[] matrix = {
                1f / 16, 2f / 16, 1f / 16,
                2f / 16, 4f / 16, 2f / 16,
                1f / 16, 2f / 16, 1f / 16
        };
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage blurredImage = new BufferedImage(width, height, image.getType());

        for (int y = radius; y < height - radius; y++)
        {
            for (int x = radius; x < width - radius; x++)
            {
                float r = 0, g = 0, b = 0;
                for (int dy = -radius; dy <= radius; dy++)
                {
                    for (int dx = -radius; dx <= radius; dx++)
                    {
                        int pixel = image.getRGB(x + dx, y + dy);
                        float coefficient = matrix[(dy + radius) * (2 * radius + 1) + (dx + radius)-1];

                        r += ((pixel >> 16) & 0xff) * coefficient;
                        g += ((pixel >> 8) & 0xff) * coefficient;
                        b += (pixel & 0xff) * coefficient;
                    }
                }

                int newPixel = (0xff << 24) | (clamp((int) r) << 16) | (clamp((int) g) << 8) | clamp((int) b);
                blurredImage.setRGB(x, y, newPixel);
            }
        }
        try
        {
            File outputfile = new File("images/flougauss.jpg");
            ImageIO.write(blurredImage, "jpg", outputfile);
        } catch (IOException e)
        {
            System.out.println("Erreur lors de l'écriture de l'image");
        }
    }
    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

        public static double formuleGauss ( int x, int y)
        {
            return ((double) 1 / 2 * Math.PI * Math.pow(ecartType, 2) * Math.exp(-(Math.pow(x, 2) * Math.pow(y, 2)) / 2 * Math.pow(ecartType, 2)));

        }
    }

