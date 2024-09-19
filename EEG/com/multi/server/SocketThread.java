/*
 * Decompiled with CFR 0.151.
 */
package com.multi.server;

import com.multi.config.ConfigClass;
import com.multi.pane.SocketPane;
import com.multi.util.Utils;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.net.Socket;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SocketThread
extends Thread {
    private Socket socket = null;
    private BufferedWriter fileWriter = null;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Connection conn = null;
    String device_id = "";
    String socketIp = "";

    public SocketThread(Socket socket) {
        this.socket = socket;
        this.socketIp = socket.getInetAddress().getHostAddress();
        try {
            this.socket.setSoTimeout(ConfigClass.SocketTimeout);
            String path = ConfigClass.STORE_PATH + this.socketIp.replaceAll("\\.", "_") + ".txt";
            Utils.checkFile(path);
            this.fileWriter = new BufferedWriter(new FileWriter(path, true));
            this.device_id = Utils.getMACAddress(socket);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run() {
        try {
            BufferedInputStream bis = new BufferedInputStream(this.socket.getInputStream());
            DataInputStream dis = new DataInputStream(bis);
            byte[] bytes = new byte[1];
            String ret = "";
            int out_flag = 0;
            boolean write_flag = true;
            block11: while (this.socket != null && dis != null) {
                if (dis.available() > 0) {
                    out_flag = 0;
                } else {
                    if (++out_flag == 10) {
                        String message = this.socketIp + "\u6709\u4e00\u6bb5\u65f6\u95f4\u6ca1\u6709\u6570\u636e\u4e86";
                        System.out.println(message);
                        SocketPane.getData_monitor_dlm().addElement(ConfigClass.RECORD_NUM + "\uff1a" + message);
                        ++ConfigClass.RECORD_NUM;
                        write_flag = true;
                    }
                    if (out_flag == 100) {
                        out_flag = 11;
                    }
                }
                dis.read(bytes);
                ret = ret + Utils.bytesToHexString(bytes);
                if (ret.length() > 6) {
                    ret = ret.substring(2);
                }
                if (!"aaaa20".equals(ret)) continue;
                byte[] data_bytes = new byte[1];
                while (dis.read(data_bytes) != -1) {
                    if ((ret = ret + Utils.bytesToHexString(data_bytes)).length() < 72) continue;
                    if (write_flag) {
                        String message = this.socketIp + "\uff1a\u5df2\u7ecf\u5f00\u59cb\u5199\u4e86";
                        SocketPane.getData_monitor_dlm().addElement(ConfigClass.RECORD_NUM + "\uff1a" + message);
                        ++ConfigClass.RECORD_NUM;
                        write_flag = false;
                    }
                    this.fileWriter.write(this.formatter.format(new Date(System.currentTimeMillis())) + "," + ret + "\n");
                    this.fileWriter.flush();
                    ret = "";
                    continue block11;
                }
            }
            this.socket.shutdownInput();
        }
        catch (Exception e) {
            System.out.println(e);
            SocketPane.getData_monitor_dlm().addElement(ConfigClass.RECORD_NUM + "\uff1a" + this.socketIp + "\uff1a" + e.toString().split(":")[1]);
            ++ConfigClass.RECORD_NUM;
        }
        finally {
            try {
                if (this.socket != null) {
                    ConfigClass.getWif_sets().remove(this.socketIp);
                    System.out.println("\u65ad\u5f00\u4e86\u5f53\u524d\u8bbe\u5907" + this.socketIp);
                    if (this.fileWriter != null) {
                        this.fileWriter.close();
                    }
                    this.socket.close();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

