package com.sensorjang.CodeToQRcode.Data;

import javax.swing.*;

public class DataCenter {
    //单例模式

    private static DataCenter data = new DataCenter();

    private DataCenter(){}

    public static DataCenter getData(){
        return data;
    }



    //Data:
    private static ImageIcon qrCode;
    private static String selectedText;
    private static ImageIcon webQrCode;





    public ImageIcon getQrCode() {
        return qrCode;
    }

    public void setQrCode(ImageIcon qrCode) {
        DataCenter.qrCode = qrCode;
    }

    public String getSelectedText() {
        return selectedText;
    }

    public void setSelectedText(String selectedText) {
        DataCenter.selectedText = selectedText;
    }

    public ImageIcon getWebQrCode() {
        return webQrCode;
    }

    public void setWebQrCode(ImageIcon webQrCode) {
        DataCenter.webQrCode = webQrCode;
    }
}
