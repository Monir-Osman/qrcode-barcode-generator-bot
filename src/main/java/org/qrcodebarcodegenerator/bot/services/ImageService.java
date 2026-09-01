package org.qrcodebarcodegenerator.bot.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ImageService {

    /**
     * Converts a BufferedImage into a Telegram InputFile.
     */
    public InputFile toInputFile(BufferedImage image, String fileName) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            ImageIO.write(image, "PNG", outputStream);

            byte[] imageBytes = outputStream.toByteArray();

            return new InputFile(
                    new java.io.ByteArrayInputStream(imageBytes),
                    fileName
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to convert image to PNG", e);
        }
    }
}