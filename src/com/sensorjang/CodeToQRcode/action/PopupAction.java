package com.sensorjang.CodeToQRcode.action;

import com.google.zxing.WriterException;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ui.MessageType;
import com.sensorjang.CodeToQRcode.Data.DataCenter;
import com.sensorjang.CodeToQRcode.utils.QRcodeUtils;
import com.sensorjang.CodeToQRcode.window.QRcodeShowWindow;
import com.sensorjang.CodeToQRcode.window.QRcodeShowWindowFactory;

import javax.swing.*;
import java.io.IOException;

public class PopupAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        SelectionModel selectionModel = editor.getSelectionModel();
        String selectedText = selectionModel.getSelectedText();//获取得到selectedText选中文本

        if (selectedText == null) {
            NotificationGroup notificationGroup = new NotificationGroup("Code2QRcode", NotificationDisplayType.BALLOON, false);
            String notificationTextChinese = "请先选中文本！";
            String notificationTextEnglish = "Please select the text first!";
            Notification notification = notificationGroup.createNotification(notificationTextEnglish, MessageType.ERROR);
            Notifications.Bus.notify(notification);
            return;
        }

        try {
            QRcodeUtils.generateQRCodeImage(selectedText, 250, 250);
        } catch (Exception exception1) {
            exception1.printStackTrace();
        }// PP love TT
        try {
            QRcodeShowWindowFactory.qRcodeShowWindow.refreshQRcode();
        }catch (Exception exception2){
            exception2.printStackTrace();
        }

        NotificationGroup notificationGroup = new NotificationGroup("Code2QRcode", NotificationDisplayType.BALLOON, false);
        String notificationTextChinese = "二维码生成成功！请在右侧 [ToolWindow: Code2QRcode] 中查看或保存结果.";
        String notificationTextEnglish = "QR code generation succeeded! Please view or save the results in the [ToolWindow: Code2QRcode] on the right.";
        Notification notification = notificationGroup.createNotification(notificationTextEnglish, MessageType.INFO);
        Notifications.Bus.notify(notification);

        return;
    }

}
