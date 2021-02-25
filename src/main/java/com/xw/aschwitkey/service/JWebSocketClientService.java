package com.xw.aschwitkey.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;


import com.xw.aschwitkey.http.Http;
import com.xw.aschwitkey.utils.SPUtils;

import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;


public class JWebSocketClientService extends Service {
    private String ws = "";
    private SPUtils spUtils;
    public JWebSocketClient client;
    private JWebSocketClientBinder mBinder = new JWebSocketClientBinder();
    private final static int GRAY_SERVICE_ID = 1001;
    private Handler mHandler_send = new Handler();

    private Runnable sendRunnable = new Runnable() {
        @Override
        public void run() {
            sendMsg("{\"message_type\":4}");
            mHandler_send.postDelayed(this, 5 * 1000);
        }
    };

    public static class GrayInnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(GRAY_SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    public class JWebSocketClientBinder extends Binder {
        public JWebSocketClientService getService() {
            return JWebSocketClientService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        spUtils = new SPUtils(getApplicationContext(), "AschWallet");
        ws = Http.Ws + spUtils.getInt("userId")+"/"+spUtils.getString("loginToken");
        initSocketClient();
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);
        mHandler_send.postDelayed(sendRunnable, 5 * 1000);
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        closeConnect();
        super.onDestroy();
    }

    public JWebSocketClientService() {
    }

    private void initSocketClient() {
        URI uri = URI.create(ws);
        client = new JWebSocketClient(uri) {
            @Override
            public void onMessage(String message) {
                Intent intent = new Intent();
                intent.setAction("com.xw.aschWitkey.serviceCallback.content");
                intent.putExtra("message", message);
                sendBroadcast(intent);
            }

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                super.onOpen(handshakedata);
            }

            @Override
            public void onError(Exception ex) {
                super.onError(ex);
            }
        };
        connect();
    }

    private void connect() {
        new Thread() {
            @Override
            public void run() {
                try {
                    client.connectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    public void sendMsg(String msg) {
        if (msg.equals("-1")) {
            mHandler.removeCallbacks(heartBeatRunnable);
            mHandler_send.removeCallbacks(sendRunnable);
            closeConnect();
        } else {
            if (null != client && client.isOpen()) {
                try {
                    client.send(msg);
                } catch (Exception e) {
                }
            }
        }
    }

    public void closeConnect() {
        try {
            if (null != client) {
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client = null;
        }
    }

    private static final long HEART_BEAT_RATE = 10 * 1000;
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (client != null) {
                if (client.isClosed()) {
                    reconnectWs();
                }
            } else {
                client = null;
                initSocketClient();
            }
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };

    private void reconnectWs() {
        mHandler.removeCallbacks(heartBeatRunnable);
        new Thread() {
            @Override
            public void run() {
                try {
                    if(client!=null){
                        client.reconnectBlocking();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
