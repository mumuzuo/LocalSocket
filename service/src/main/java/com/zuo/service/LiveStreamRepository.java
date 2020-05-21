package com.zuo.service;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 无人机互联，数据存储队列
 *
 * @author zuo
 * @date 2020/5/19 14:13
 */
public class LiveStreamRepository {
    //队列，可存储20帧数据
    private int mQueueSize = 10;
    private int mBufferSize = 5;

    private ArrayBlockingQueue<String> mQueue = new ArrayBlockingQueue<>(mQueueSize);


    private LiveStreamRepository() {
    }

    private final static class UavVideoInfoInstanceHolder {
        private static final LiveStreamRepository ins = new LiveStreamRepository();
    }

    public static LiveStreamRepository getInstance() {
        return UavVideoInfoInstanceHolder.ins;
    }

    public String getData() {
        return mQueue.poll();
    }

    public boolean addData(String data) {
        //如果插入失败（），移除前5帧
        if (mQueue.size() == mQueueSize) {
            for (int i = 0; i < mBufferSize; i++) {
                mQueue.remove(i);
            }
        }
        return mQueue.offer(data);
    }
}
