package com.zk.zk_online.weight;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.zk.zk_online.R;

/**
 * 消息通知栏
 * Created by ZYB on 2017/11/23 0023.
 */

public class MsgNotification {
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private Context mcontext;
    public MsgNotification(Context context)
    {
        mcontext=context;
        notificationManager= (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);


    }

    public void showNotificaion(String title,String msg)
    {
        builder=new NotificationCompat.Builder(mcontext)
                .setSmallIcon(R.mipmap.icon_96)
                .setContentTitle(title)
                .setContentText(msg);
        notificationManager.notify(1, builder.build());
    }
}
