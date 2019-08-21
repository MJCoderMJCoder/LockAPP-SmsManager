package com.smyhvae.lockapp;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    private String target_phone = "18382265479";
    //    private String target_phone = "18382265479"; //Lkm、1216322729@qq.com：18382265479
    private String send_str;

    private IntentFilter sendFilter; //短信发送
    private SendStatusReceiver sendStatusReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText num_edit = (EditText) findViewById(R.id.editText);
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_str = num_edit.getText().toString();
                Log.v("target_phone", target_phone);
                Log.v("send_str", send_str);
                if (send_str == null || send_str.length() <= 0) {
                    Toast.makeText(MainActivity.this, "请输入要发送的信息！", Toast.LENGTH_LONG).show();
                } else {
                    sendSms(target_phone, send_str);
                }
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSms(target_phone, "2222");
            }
        });

        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        //            //SEE NEXT STEP
        //            if (MainActivity.this.checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
        //                //SEE NEXT STEP
        //            } else {
        //            }
        //            //            if (MainActivity.this.shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
        //            //                //show some description about this permission to the user as a dialog, toast or in Snackbar
        //            //            } else {
        //            //                //request for the permission
        //            //            }
        //        }
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.SEND_SMS, Manifest.permission.BROADCAST_SMS};
            ActivityCompat.requestPermissions(this, mPermissionList, 6003);
        }
        //       注册监听短信发送的广播
        sendFilter = new IntentFilter();
        sendFilter.addAction("SENT_SMS_ACTION");
        sendStatusReceiver = new SendStatusReceiver();
        registerReceiver(sendStatusReceiver, sendFilter);
    }

    public void sendSms(String phone, String msg) {
        SmsManager smsManager = SmsManager.getDefault();
        Intent sentIntent = new Intent("SENT_SMS_ACTION");
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, sentIntent, 0);
        //根据号码和内容发送短信（需要权限）
        smsManager.sendTextMessage(phone, null, msg, pi, null);
    }

    //监听广播是否发送成功的广播
    class SendStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getResultCode() == RESULT_OK) {
                Toast.makeText(context, "发送成功！", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "发送失败！", Toast.LENGTH_LONG).show();
            }
        }
    }
}