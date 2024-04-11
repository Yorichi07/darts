package com.darts.server.functions;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;


public class CreateQr {

    // Method to generate and save QR code
    public static String generateAndSaveQRCode(String token,String UID) throws WriterException, IOException {
        // Encode hints
        Map<EncodeHintType, Object> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

        int width = 300;
        int height = 300;
        String filePath = "./build/resources/main/static/qrs/"+UID+".png";

        // Create QR code writer
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        String payload = "http://192.168.56.1:8080/api/pages/getPatientDetails?token="+token;

        // Generate BitMatrix
        BitMatrix bitMatrix = qrCodeWriter.encode(payload, BarcodeFormat.QR_CODE, width, height, hintMap);

        // Create image file
        File qrCodeFile = new File(filePath);

        // Write to file
        MatrixToImageWriter.writeToFile(bitMatrix, "PNG", qrCodeFile);

        return "qrs/"+UID+".png";
    }
}

class MatrixToImageWriter {

    // Method to write BitMatrix to file
    public static void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
        Path path = file.toPath();
        MatrixToImageWriter.writeToPath(matrix, format, path);
    }

    // Method to write BitMatrix to file path
    public static void writeToPath(BitMatrix matrix, String format, Path path) throws IOException {
        MatrixToImageWriter.writeToPath(matrix, format, path.toFile());
    }

    // Method to write BitMatrix to file
    public static void writeToPath(BitMatrix matrix, String format, File file) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }

    // Method to convert BitMatrix to BufferedImage
    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }
}