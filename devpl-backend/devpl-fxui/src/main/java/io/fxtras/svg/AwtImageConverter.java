package io.fxtras.svg;

import io.fxtras.svg.xml.parsers.SVGLibraryException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

/**
 * Converts a JPG image to handle the transparent background.
 *
 * @version 1.0
 */
class AwtImageConverter {
    private static Object colorModel = null;
    private static final int[] RGB_MASKS = {0xFF0000, 0xFF00, 0xFF};

    static boolean snapshot(WritableImage image, SnapshotParameters params, String format, File file) throws SVGLibraryException {
        RenderedImage awtImg = SwingFXUtils.fromFXImage(image, null);
        try {
            if (format.equals("jpg")) {
                // see https://stackoverflow.com/questions/4386446/issue-using-imageio-write-jpg-file-pink-background
                PixelGrabber pg = new PixelGrabber((java.awt.Image) awtImg, 0, 0, -1, -1, true);
                pg.grabPixels();
                int width = pg.getWidth(), height = pg.getHeight();

                DataBuffer buffer = new DataBufferInt((int[]) pg.getPixels(), pg.getWidth() * pg.getHeight());
                WritableRaster raster = Raster.createPackedRaster(buffer, width, height, width, RGB_MASKS, null);
                if (colorModel == null) {
                    colorModel = new DirectColorModel(32, RGB_MASKS[0], RGB_MASKS[1], RGB_MASKS[2]);
                }
                awtImg = new BufferedImage((ColorModel) colorModel, raster, false, null);
            }
            ImageIO.write(awtImg, format, file);
            return true;
        } catch (IOException | InterruptedException ex) {
            throw new SVGLibraryException(ex);
        }
    }
}
