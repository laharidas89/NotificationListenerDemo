package com.example.notificationlistenerdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageView mImageView;
    private TextView mTextViewPackage;
    private TextView mTextViewTitle;
    private TextView mTextViewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewPackage = findViewById(R.id.textViewPackage);
        mTextViewTitle = findViewById(R.id.textViewTitle);
        mTextViewText = findViewById(R.id.textViewText);
        mImageView = findViewById(R.id.imageView);

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));
    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive");
            String packageName = intent.getStringExtra("package");
            if (packageName != null) {
                mTextViewPackage.setText(packageName);
            }
            String title = intent.getStringExtra("title");
            if (title != null) {
                mTextViewTitle.setText(title);
            }
            String text = intent.getStringExtra("text");
            if (text != null) {
                mTextViewText.setText(text);
            }
            Bitmap bitmap = intent.getParcelableExtra("largeIcon");
            if (bitmap != null) {
                mImageView.setImageBitmap(bitmap);
            }
        }
    };
}