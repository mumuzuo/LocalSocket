package com.zuo.localsocket;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 采用数据包的方式发送文本类型的数据
 * 本实例区别于 SocketClientImpl ，仅用作于文本信息的传递，采用 UDP 协议
 *
 * @author zuo
 * @date 2020/5/14 15:08
 */
public class SocketTextImpl implements Runnable {
    private static final String TAG = "SocketClientTextImpl";
    public static final int bufferSize = 1024 * 1024;
    private DatagramSocket socket;
    private final int SERVER_PORT = 8090;
    private final int CLIENT_PORT = 8091;

    public SocketTextImpl() {
    }

    @Override
    public void run() {
        Log.i(TAG, "Client isOpen");
        try {
            if (null == socket) {
                //监听对应端口
                socket = new DatagramSocket(CLIENT_PORT, InetAddress.getLocalHost());
            }
        } catch (IOException e1) {
            Log.i(TAG, e1.getMessage());
            e1.printStackTrace();
        }
        //接收信息
        while (true) {
            byte[] buffer = new byte[bufferSize];
            DatagramPacket recDp = new DatagramPacket(buffer, buffer.length);
            try {
                //定义1M的文本消息缓存，如果消息大于1M，会被截断
                socket.receive(recDp);
                String recMsg = new String(buffer, 0, recDp.getLength());
                LiveStreamRepository.getInstance().addData(recMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送给服务端的数据，使用服务端监听的端口
     *
     * @param data
     */
    public void send(String data) throws Exception {
        if (null != socket) {
            byte[] bytes = data.getBytes();
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, InetAddress.getLocalHost(), SERVER_PORT);
            socket.send(packet);
        }
    }

    /**
     * 关闭监听
     */
    public void close() {
        try {
            if (null != socket) {
                socket.close();
                socket = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
