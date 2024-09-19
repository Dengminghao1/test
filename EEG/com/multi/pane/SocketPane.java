/*
 * Decompiled with CFR 0.151.
 */
package com.multi.pane;

import com.multi.config.ConfigClass;
import com.multi.config.ConfigPane;
import com.multi.server.SocketServer;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class SocketPane {
    private static int HEIGHT = 600;
    private static int WIDTH = 800;
    private JButton startButton;
    private JButton peizhiButton;
    private JList<String> uart_num_list;
    private Thread startThread = null;
    public static DefaultListModel<String> uart_num_dlm;
    private static JLabel UARTjLabel;
    private JList<String> data_monitor_list;
    private static DefaultListModel<String> data_monitor_dlm;
    private JLabel DatajLabel;
    private JTextField file_jTextField;
    private JLabel file_jLabel;
    private JFrame jFrame;
    private JPanel panel;
    private JScrollPane uart_num_pane;
    private JScrollPane data_monitor_pane;
    private JLabel port_jLabel;
    private JTextField port_jTextField;

    public void makeJFrame() {
        this.jFrame = new JFrame("\u591a\u6a21\u6001\u5b66\u4e60\u6570\u636e\u91c7\u96c6\u5de5\u5177");
        this.jFrame.setLocation(500, 300);
        this.jFrame.setSize(WIDTH, HEIGHT);
        this.jFrame.setDefaultCloseOperation(3);
        this.jFrame.setContentPane(this.panel);
        this.jFrame.setVisible(true);
    }

    public void makePanel() {
        this.panel = new JPanel(null);
        this.panel.add(this.startButton);
        this.panel.add(this.port_jLabel);
        this.panel.add(this.port_jTextField);
        this.panel.add(this.uart_num_pane);
        this.panel.add(UARTjLabel);
        this.panel.add(this.data_monitor_pane);
        this.panel.add(this.DatajLabel);
        this.panel.add(this.file_jLabel);
        this.panel.add(this.file_jTextField);
    }

    public static void main(String[] args) {
        SocketPane socketPane = new SocketPane();
        socketPane.initComponent();
        socketPane.addAction();
        socketPane.makePanel();
        socketPane.makeJFrame();
    }

    public void initComponent() {
        Font f = new Font("\u5b8b\u4f53", 0, 16);
        this.file_jLabel = new JLabel("\u8bf7\u8f93\u5165\u5b58\u50a8\u4f4d\u7f6e\uff1a");
        this.file_jLabel.setFont(f);
        this.file_jLabel.setLocation(0, 0);
        this.file_jLabel.setSize(WIDTH / 2, 30);
        this.file_jTextField = new JTextField(ConfigClass.STORE_PATH);
        this.file_jTextField.setLocation(WIDTH / 6, 0);
        this.file_jTextField.setSize(WIDTH / 2 - 50, 30);
        this.port_jLabel = new JLabel("\u8bf7\u8f93\u5165\u7aef\u53e3\u53f7\uff1a");
        this.port_jLabel.setFont(f);
        this.port_jLabel.setLocation(WIDTH - 300, 0);
        this.port_jLabel.setSize(WIDTH / 5, 30);
        this.port_jTextField = new JTextField(ConfigClass.SERVER_PORT);
        this.port_jTextField.setFont(f);
        this.port_jTextField.setLocation(WIDTH - 180, 0);
        this.port_jTextField.setSize(WIDTH / 5, 30);
        this.startButton = new JButton(ConfigPane.startButtonText);
        this.startButton.setFont(f);
        this.startButton.setLocation(0, HEIGHT - 80);
        this.startButton.setSize(WIDTH - 10, 30);
        UARTjLabel = new JLabel("\u8fd9\u6b21\u91c7\u96c6\u7684\u5ba2\u6237\u7aef\u8bbe\u5907\u4fe1\u606f\uff1a");
        UARTjLabel.setFont(f);
        UARTjLabel.setLocation(0, 30);
        UARTjLabel.setSize(WIDTH / 2, 30);
        this.DatajLabel = new JLabel("\u5ba2\u6237\u7aef\u5f02\u5e38\u4fe1\u606f\u6253\u5370\uff1a");
        this.DatajLabel.setFont(f);
        this.DatajLabel.setLocation(WIDTH / 2, 30);
        this.DatajLabel.setSize(WIDTH / 2, 30);
        this.uart_num_list = new JList();
        this.uart_num_list.setFont(f);
        uart_num_dlm = new DefaultListModel();
        this.uart_num_list.setModel(uart_num_dlm);
        this.uart_num_pane = new JScrollPane(this.uart_num_list);
        this.uart_num_pane.setLocation(0, 60);
        this.uart_num_pane.setSize(WIDTH / 2 - 10, HEIGHT - 140);
        this.data_monitor_list = new JList();
        this.data_monitor_list.setFont(f);
        data_monitor_dlm = new DefaultListModel();
        this.data_monitor_list.setModel(data_monitor_dlm);
        this.data_monitor_pane = new JScrollPane(this.data_monitor_list);
        this.data_monitor_pane.setLocation(WIDTH / 2, 60);
        this.data_monitor_pane.setSize(WIDTH / 2 - 10, HEIGHT - 140);
    }

    public void addAction() {
        this.startButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if (ConfigPane.startButtonText.equals(SocketPane.this.startButton.getText())) {
                    SocketPane.this.startThread = new Thread(new Runnable(){

                        @Override
                        public void run() {
                            SocketServer.start();
                        }
                    });
                    ConfigClass.STORE_PATH = SocketPane.this.file_jTextField.getText();
                    File file = new File(ConfigClass.STORE_PATH);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    ConfigClass.SERVER_PORT = SocketPane.this.port_jTextField.getText();
                    SocketPane.this.startThread.start();
                    SocketPane.this.startButton.setText("stop");
                } else if ("stop".equals(SocketPane.this.startButton.getText())) {
                    System.out.println("\u6211\u5df2\u7ecf\u4e2d\u65ad\u4e86before" + Thread.activeCount());
                    System.out.println(ConfigClass.DATA_THREAD);
                    SocketPane.this.startThread.interrupt();
                    ConfigClass.DATA_THREAD.forEach((threadName, thread) -> thread.interrupt());
                    System.out.println("\u6211\u5df2\u7ecf\u4e2d\u65ad\u4e86after" + Thread.activeCount());
                    SocketPane.this.startButton.setText("start");
                }
            }
        });
    }

    public static DefaultListModel<String> getUart_num_dlm() {
        return uart_num_dlm;
    }

    public static void setUart_num_dlm(DefaultListModel<String> uart_num_dlm) {
        SocketPane.uart_num_dlm = uart_num_dlm;
    }

    public static JLabel getUARTjLabel() {
        return UARTjLabel;
    }

    public static void setUARTjLabel(JLabel UARTjLabel) {
        SocketPane.UARTjLabel = UARTjLabel;
    }

    public static DefaultListModel<String> getData_monitor_dlm() {
        return data_monitor_dlm;
    }

    public static void setData_monitor_dlm(DefaultListModel<String> data_monitor_dlm) {
        SocketPane.data_monitor_dlm = data_monitor_dlm;
    }
}

