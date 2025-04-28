package com.sensorjang.CodeToQRcode.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sensorjang.CodeToQRcode.Data.DataCenter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Hashtable;


public class QRcodeUtils {



    public static void generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        BufferedImage bufferedImage = bitmatrixToBufferedImage(bitMatrix);
        Image image = bufferedImage;
        ImageIcon imageIcon = new ImageIcon(image);

        DataCenter data = DataCenter.getData();
        data.setQrCode(imageIcon);
        data.setSelectedText(text);



    }

    private static BufferedImage bitmatrixToBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, 1);

        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                image.setRGB(x, y, matrix.get(x, y) ? -16777216 : -1);
            }
        }

        return image;
    }

    public static BufferedImage imageToBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        image = new ImageIcon(image).getImage();
        boolean hasAlpha = false;
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null),
                    image.getHeight(null), transparency);
        } catch (HeadlessException e) {
        }
        if (bimage == null) {
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null),
                    image.getHeight(null), type);
        }
        Graphics g = bimage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }


    public static void main(String[] args) throws IOException, WriterException {
    }
}
