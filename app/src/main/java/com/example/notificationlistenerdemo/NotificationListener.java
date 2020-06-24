package com.example.notificationlistenerdemo;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class NotificationListener extends NotificationListenerService {
    private static final String TAG = NotificationListener.class.getSimpleName();
    private HashMap<String, Boolean> savedImageMap = new HashMap<>();

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Log.i(TAG, "onNotificationPosted");
        String packageName = sbn.getPackageName();
        Log.i(TAG, "package : " + packageName);
        ArrayList<String> packageList = new ArrayList<String>() {
            {
                add("com.sds.mysinglesquare");
                add("com.whatsapp");
                add("com.facebook.katana");
            }
        };
        if (packageList.contains(packageName)) {
            Notification notification = sbn.getNotification();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Bundle extras = notification.extras;
                if (extras != null) {
                    String title = extras.getString("android.title");
                    String text = extras.getString("android.text");
                    Intent msgrcv = new Intent("Msg");
                    msgrcv.putExtra("package", packageName);
                    msgrcv.putExtra("title", title);
                    msgrcv.putExtra("text", text);
                    Icon largeIcon = notification.getLargeIcon();
                    if (largeIcon != null) {
                        Drawable largeIconDrawable = largeIcon.loadDrawable(getApplicationContext());
                        Bitmap largeIconBitmap = ((BitmapDrawable) largeIconDrawable).getBitmap();
                        Log.i(TAG, "largeIconBitmap : " + largeIconBitmap);
                        if (largeIconBitmap != null) {
                            saveBitmapToFile(largeIconBitmap, title, packageName);
                            msgrcv.putExtra("largeIcon", largeIconBitmap);
                        }
                    }
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(msgrcv);
                }
            }
        }
    }

    private void saveBitmapToFile(Bitmap bitmap,  String title, String packageName) {
        String tag = title + "_" + packageName;
        if (savedImageMap.containsKey(tag)) {
            Log.i(TAG, "image already saved for " + tag);
            return;
        }
        String fileName = "notification_" + tag + ".jpg";
        File myImagePath = new File(getApplicationContext().getDir(Environment.DIRECTORY_DOWNLOADS, Context.MODE_PRIVATE), fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myImagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            savedImageMap.put(title, true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG, "bitmap saved at " + myImagePath.getAbsolutePath());
    }


    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Log.i(TAG, "onNotificationRemoved");
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.i(TAG, "onListenerConnected");
    }

    @Override
    public void onListenerDisconnected() {
        super.onListenerDisconnected();
        Log.i(TAG, "onListenerDisconnected");
    }
}
