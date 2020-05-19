package com.zuo.localsocket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 和服务端进行数据收发
 *
 * @author zuo
 * @date 2020/5/14 15:08
 */
public class SocketClientImpl implements Runnable {
    private static final String TAG = "SocketClientImpl";
    private String localSocketAddress = "kingoituavlink";
    private BufferedOutputStream os;
    private BufferedInputStream is;
    private int timeout = 30000;
    public static final int bufferSizeOutput = 1024 * 1024;
    private Socket client;
    private Handler handler;

    public SocketClientImpl(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        Log.i(TAG, "Client isOpen");
        try {
            if (null == client) {
//                client = new LocalSocket();
                client = new Socket("localhost", 8080);
//                client.connect(new LocalSocketAddress(localSocketAddress));
                client.setSoTimeout(timeout);
                Log.i(TAG, "Server Connected");
            }
            os = new BufferedOutputStream(client.getOutputStream(), bufferSizeOutput);
            is = new BufferedInputStream(client.getInputStream(), bufferSizeOutput);
        } catch (IOException e1) {
            Log.i(TAG, e1.getMessage());
            e1.printStackTrace();
        }
        //将接收到的数据发送出去
        while (null != is) {
            try {
                if (is.available() <= 0) continue;
                Message msg = handler.obtainMessage();
                msg.obj = is;
                msg.arg1 = 1;
                handler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送数据
     *
     * @param data
     */
    public synchronized void send(byte[] data) throws Exception {
        if (null != os) {
            os.write(data);
            os.flush();
        }
    }

    /**
     * 关闭监听
     */
    public void close() {
        try {
            if (null != os) {
                os.close();
                os = null;
            }
            if (null != is) {
                is.close();
                is = null;
            }
            if (null != client) {
                client.close();
                client = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
