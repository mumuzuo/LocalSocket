package com.zuo.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.zuo.service.databinding.ActivityMainBinding;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IntRange;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

/**
 * @author zuo
 * @date 2020/5/18 11:01
 */
public class MainActivity extends AppCompatActivity {

    private SocketServerImpl socketServer;
    private ActivityMainBinding binding;
    private List<Integer> data;
    @IntRange(from = 0, to = 3)
    private int index = 0;

    //持续接收客户端反馈信息
    private StringBuilder buffer = new StringBuilder();
    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.arg1 == 1) {
                SocketParseBean bean = null;
                try {
                    bean = SendDataUtils.parseSendData((BufferedInputStream) msg.obj);
                    if (null == bean || TextUtils.isEmpty(bean.getInfo())) return false;
                    showImg();
                } catch (Exception e) {
                    return false;
                }
                buffer.append(bean.getInfo());
                buffer.append("\r\n");
                showSocketMsg();
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setPresenter(new Presenter());
        initData();
        startSocketServer();
    }

    private void showSocketMsg() {
        if (null != binding) {
            binding.backMsgShow.setText("客户端消息：" + buffer.toString());
        }
    }

    private void startSocketServer() {
        socketServer = new SocketServerImpl(handler);
        new Thread(socketServer).start();
    }

    private void initData() {
        data = new ArrayList<>();
        data.add(R.drawable.kb890);
        data.add(R.drawable.kb618);
        data.add(R.drawable.kb224);
    }

    private void showImg() {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), data.get(index));
        binding.imgShow.setImageBitmap(bmp);
        binding.indexShow.setText((index + 1) + "/" + data.size());
        String hint = "服务端正在展示第 " + (index + 1) + " 张照片";
        sendData(hint, bmp);
    }

    public void sendData(final String hint, final Bitmap bmp) {
        if (null != socketServer) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] array = null;
            try {
                if (null != bmp) {
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    array = baos.toByteArray();
                }
                byte[] bytes = SendDataUtils.makeSendData(hint, array);
                socketServer.send(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != socketServer) {
            socketServer.close();
        }
    }

    public class Presenter {

        public void last(View view) {
            if (index <= 0) {
                Toast.makeText(MainActivity.this, "没有上一张了！", Toast.LENGTH_SHORT).show();
                return;
            }
            index--;
            showImg();
        }

        public void next(View view) {
            if (index >= 2) {
                Toast.makeText(MainActivity.this, "没有下一张了！", Toast.LENGTH_SHORT).show();
                return;
            }
            index++;
            showImg();
        }
    }

}
