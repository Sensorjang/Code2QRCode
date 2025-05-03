package com.sensorjang.CodeToQRcode.window;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.sensorjang.CodeToQRcode.Data.DataCenter;
import com.sensorjang.CodeToQRcode.utils.QRcodeUtils;
import com.sensorjang.CodeToQRcode.utils.RandomStringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QRcodeShowWindow {
    private JPanel contentPanel;
    private JButton saveButton;
    private JLabel QRcodeLabel;
    private JButton generateLinkButton;
    private JButton generateOnceLinkButton;
    private JComboBox languageComboBox;
    private JTextField pwTextField;
    private JTextField resultLinkTextField;
    private JTextField passwordTextField;
    private JButton cpoyButton;

    public QRcodeShowWindow(Project project, ToolWindow toolWindow, List<String> language_type_list) {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VirtualFile virtualFile = FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFolderDescriptor(), project, project.getBaseDir());
                if (virtualFile != null) {
                    String path = virtualFile.getPath();
                    String topic = RandomStringUtils.getRandomString(10);
                    String filePath = path + "/" + topic + ".png";
                    DataCenter data = DataCenter.getData();
                    ImageIcon qrCode = data.getQrCode();

                    //二维码持久化
                    BufferedImage image = QRcodeUtils.imageToBufferedImage(qrCode.getImage());
                    File f = new File(filePath);
                    try {
                        ImageIO.write(image, "png", f);

                    } catch (IOException e2) {
                        e2.printStackTrace();

                    }

                }
            }
        });

        generateLinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateLink(false);
            }
        });

        generateOnceLinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateLink(true);
            }
        });

        cpoyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyToClipboard(resultLinkTextField.getText());
            }
        });

        init(language_type_list);
    }

    private void copyToClipboard(String text) {
        if (text == null || text.isEmpty()) {
            // 显示错误通知
            Notification notification = new Notification("com.Sensorjang.plugin.Code2QRcode.id", "No text to copy!", NotificationType.ERROR);
            Notifications.Bus.notify(notification);
            return;
        }
        // 创建一个字符串选择器
        StringSelection stringSelection = new StringSelection(text);
        // 获取系统剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 将内容复制到剪贴板
        clipboard.setContents(stringSelection, null);
        // 显示成功通知
        Notification notification = new Notification("com.Sensorjang.plugin.Code2QRcode.id", "Copied to clipboard!", NotificationType.INFORMATION);
        Notifications.Bus.notify(notification);
    }

    private void init(List<String> language_type_list) {
        DataCenter data = DataCenter.getData();
        ImageIcon qrCode = data.getQrCode();
        QRcodeLabel.setIcon(qrCode);
        //初始化语言选择框
        for (String language : language_type_list) {
            languageComboBox.addItem(language);
        }
        languageComboBox.setSelectedIndex(0);
    }

    public void refreshQRcode() {
        DataCenter data = DataCenter.getData();
        ImageIcon qrCode = data.getQrCode();
        QRcodeLabel.setIcon(qrCode);
        resultLinkTextField.setText("");
    }

    public void generateLink(Boolean onceflag) {
        DataCenter data = DataCenter.getData();
        String text = data.getSelectedText();
        String language = (String) languageComboBox.getSelectedItem();
        String password = passwordTextField.getText();
        if(password.equals(""))password = null;

        //构建json
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("lang", language);
        jsonMap.put("content", text);
        jsonMap.put("password", password);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonMap);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String json = jsonObject.toString();

        String responseStr="", key="ERROR";
        try{
            URL url;
            if (!onceflag){
                url = new URL("https://paste.liumingye.cn/api");
            }else {
                url = new URL("https://paste.liumingye.cn/api/once");
            }

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setDoOutput(true);

            try(OutputStream os = con.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
                responseStr = response.toString();
            }

            //从responseStr中提取key
            JSONObject jsonResponse = new JSONObject(responseStr);
            key = jsonResponse.getString("key");

        }catch (Exception e1){
            e1.printStackTrace();
        }

        String res = "https://paste.liumingye.cn/" + key;
        if(key.equals("ERROR"))res = "ERROR";
        resultLinkTextField.setText(res);
        return;

    }

    public JPanel getContentPanel() {
        return contentPanel;
    }
}
