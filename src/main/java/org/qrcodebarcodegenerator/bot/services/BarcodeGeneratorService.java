package org.qrcodebarcodegenerator.bot.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

@Service
public class BarcodeGeneratorService {

    private static final int QR_WIDTH = 500;
    private static final int QR_HEIGHT = 500;

    private static final int BARCODE_WIDTH = 800;
    private static final int BARCODE_HEIGHT = 300;

    /**
     * Generates a QR code.
     *
     * @param content text or URL to encode
     * @return generated QR code as BufferedImage
     */
    public BufferedImage generateQrCode(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("QR code content cannot be empty");
        }

        return generate(
                content,
                BarcodeFormat.QR_CODE,
                QR_WIDTH,
                QR_HEIGHT
        );
    }

    /**
     * Generates a Code 128 barcode.
     *
     * Code 128 supports numbers, letters and many ASCII characters.
     *
     * @param content text to encode
     * @return generated barcode as BufferedImage
     */
    public BufferedImage generateBarcode(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Barcode content cannot be empty");
        }

        return generate(
                content,
                BarcodeFormat.CODE_128,
                BARCODE_WIDTH,
                BARCODE_HEIGHT
        );
    }

    private BufferedImage generate(
            String content,
            BarcodeFormat format,
            int width,
            int height
    ) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();

            // UTF-8 allows QR content to contain non-ASCII characters.
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // Adds a white margin around the generated code.
            hints.put(EncodeHintType.MARGIN, 2);

            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    content,
                    format,
                    width,
                    height,
                    hints
            );

            return MatrixToImageWriter.toBufferedImage(bitMatrix);

        } catch (WriterException e) {
            throw new RuntimeException(
                    "Failed to generate " + format + " for content",
                    e
            );
        }
    }
}