/*
 * Decompiled with CFR 0.151.
 */
package com.multi.server;

import com.multi.config.ConfigClass;
import com.multi.pane.SocketPane;
import com.multi.server.SocketThread;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

public class SocketServer {
    private static Set<String> set = ConfigClass.getWif_sets();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(Integer.valueOf(args[0]));
            System.out.println("\u670d\u52a1\u5668\u5df2\u542f\u52a8~~~");
            Socket socket = new Socket();
            boolean num = false;
            while (true) {
                InetAddress address;
                String addressString;
                if (!set.contains(addressString = (address = (socket = serverSocket.accept()).getInetAddress()).toString())) {
                    set.add(addressString);
                    SocketThread thread = new SocketThread(socket);
                    thread.start();
                    System.out.println("\u5f53\u524d\u5ba2\u6237\u7aef\u7684IP\uff1a" + address.getHostAddress());
                    System.out.println("\u5f53\u524d\u7ebf\u7a0b\u603b\u6570\uff1a" + ConfigClass.getWif_sets().size());
                    continue;
                }
                socket.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public static void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(Integer.valueOf(ConfigClass.SERVER_PORT));
            System.out.println("\u670d\u52a1\u5668\u5df2\u542f\u52a8,4b_5~~~");
            Socket socket = new Socket();
            boolean num = false;
            while (true) {
                InetAddress address;
                String addressString;
                if (!set.contains(addressString = (address = (socket = serverSocket.accept()).getInetAddress()).toString())) {
                    set.add(addressString);
                    SocketThread thread = new SocketThread(socket);
                    thread.start();
                    System.out.println("\u5f53\u524d\u5ba2\u6237\u7aef\u7684IP\uff1a" + address.getHostAddress());
                    System.out.println("\u5f53\u524d\u7ebf\u7a0b\u603b\u6570\uff1a" + ConfigClass.getWif_sets().size());
                    if (!SocketPane.getUart_num_dlm().contains(address.getHostAddress())) {
                        SocketPane.getUart_num_dlm().addElement(address.getHostAddress() + "\uff1a\u5df2\u7ecf\u8fde\u63a5\u4e0a\u4e86");
                        SocketPane.getUARTjLabel().setText("\u8fd9\u6b21\u91c7\u96c6\u7684\u5ba2\u6237\u7aef\u8bbe\u5907\u4fe1\u606f\uff1a" + set.size() + "\u4e2a\u5ba2\u6237\u7aef");
                    }
                    Thread.sleep(100L);
                    continue;
                }
                socket.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}

