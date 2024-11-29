package com.example.library.controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class QrBookController implements Initializable {
    private String link = "";

    // Thêm ImageView để hiển thị mã QR
    @FXML
    private ImageView qrImageView;

    public void setBookData(String url){
        link = url;
        generateQRCode(link, "qr.png");
    }

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        // Bạn có thể khởi tạo thêm nếu cần
    }

    public void generateQRCode(String link, String outputFilePath) {
        try {
            // Cấu hình mã QR
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.MARGIN, 1); // Khoảng trắng xung quanh mã QR

            // Tạo BitMatrix cho mã QR
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    link, BarcodeFormat.QR_CODE, 300, 300, hints);

            // Chuyển BitMatrix thành hình ảnh
            BufferedImage qrImage = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < 300; x++) {
                for (int y = 0; y < 300; y++) {
                    qrImage.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }

            // Lưu ảnh mã QR vào file
            File outputFile = new File(outputFilePath);
            ImageIO.write(qrImage, "PNG", outputFile);

            System.out.println("QR Code created successfully at: " + outputFile.getAbsolutePath());

            // Hiển thị mã QR lên ImageView
            Image qrImageJavaFX = new Image(outputFile.toURI().toString());
            qrImageView.setImage(qrImageJavaFX);
        } catch (Exception e) {
            System.err.println("Error generating QR Code: " + e.getMessage());
        }
    }
}
