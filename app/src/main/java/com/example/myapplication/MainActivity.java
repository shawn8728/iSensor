package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    public static int TIME_OUT = 2000;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //firebase
    public String temp0;
    public String hum0;
    public String dust0;
    public String temp1;
    public String hum1;
    public String dust1;
    public float temp2;
    public float hum2;
    public float dust2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        getSupportFragmentManager().beginTransaction().replace(R.id.container,new FragmentHome()).commit();
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.my_color));


        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        selectedFragment = new FragmentHome();
                        break;
                    case R.id.navigation_dashboard:
                        selectedFragment = new FragmentSearch();
                        break;
                }
               getSupportFragmentManager().beginTransaction().replace(R.id.container,selectedFragment).commit();
                return true;
            }
        });




        databaseReference = FirebaseDatabase.getInstance().getReference().child("DHT11");


        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // 建立群組
        createNotificationGroup(GROUP_ID, GROUP_NAME);
        // 建立頻道
        createNotificationChannel(CHANNEL0_ID, CHANNEL0_NAME, CHANNEL0_DESCRIPTION);
        createNotificationChannel(CHANNEL1_ID, CHANNEL1_NAME, CHANNEL1_DESCRIPTION);
        createNotificationChannel(CHANNEL2_ID, CHANNEL2_NAME, CHANNEL2_DESCRIPTION);



        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                temp0 = dataSnapshot.child("realtime").child("Temperature").getValue().toString();
                hum0 = dataSnapshot.child("realtime").child("Humidity").getValue().toString();
                dust0 = dataSnapshot.child("realtime").child("DustDensity").getValue().toString();
                temp1 = temp0.substring(0,2);
                hum1 = hum0.substring(0,2);
                dust1 = dust0.substring(0,4);
                temp2 = Float.parseFloat(temp1);
                hum2 = Float.parseFloat(hum1);
                dust2 = Float.parseFloat(dust1);
                if (temp2>=30)
                    sendNotification("temp", "Temperature Sensor", "溫度過高 高於30°C", CHANNEL0_ID, 1);
                if (hum2>=80)
                    sendNotification("hum", "Humidity Sensor", "濕度過高 高於80%", CHANNEL1_ID, 2);
                if (dust2>=35 && dust2<=53)
                    sendNotification("dust", "Dust Density Sensor", "環境對敏感族群不健康", CHANNEL2_ID, 3);
                if (dust2>=54 && dust2<=149)
                    sendNotification("dust", "Dust Density Sensor", "環境對所有族群不健康", CHANNEL2_ID, 3);
                if (dust2>=150 && dust2<=249)
                    sendNotification("dust", "Dust Density Sensor", "非常不健康", CHANNEL2_ID, 3);
                if (dust2>=250)
                    sendNotification("dust", "Dust Density Sensor", "危害嚴重", CHANNEL2_ID, 3);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });








    }



    //notifications

    public static final String GROUP_ID = "100";
    public static final String GROUP_NAME = "Sensor Notification";
    // 頻道(ID必須唯一)
    public static final String CHANNEL0_ID = "101";
    public static final String CHANNEL0_NAME = "Temperature Notifications";
    public static final String CHANNEL0_DESCRIPTION = "";

    public static final String CHANNEL1_ID = "102";
    public static final String CHANNEL1_NAME = "Humidity Notifications";
    public static final String CHANNEL1_DESCRIPTION = "";

    public static final String CHANNEL2_ID = "103";
    public static final String CHANNEL2_NAME = "Dust density Notifications";
    public static final String CHANNEL2_DESCRIPTION = "";

    private static NotificationManager manager = null;

    public void createNotificationGroup(String groupId, String groupName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannelGroup group = new NotificationChannelGroup(groupId, groupName);
            manager.createNotificationChannelGroup(group);

        }
    }


    public void createNotificationChannel(String channelId, String channelName, String channelDescription) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            // 描述
            channel.setDescription(channelDescription);
            // 開啟指示燈
            channel.enableLights(true);
            // 設置指示燈顏色
            channel.setLightColor(ContextCompat.getColor(this, R.color.colorPrimary));
            // 是否在久按桌面圖示時顯示此管道的通知
            channel.setShowBadge(true);
            // 設置是否應在鎖定螢幕上顯示此頻道的通知
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PRIVATE);
            // 設置繞過免打擾模式
            channel.setBypassDnd(true);
            // 設定群組
            channel.setGroup(GROUP_ID);
            //
            manager.createNotificationChannel(channel);
        }
    }


    public void sendNotification(String ticker, String title, String content, String channelId, int notifyId) {
        /// 建立通知
        NotificationCompat.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(this, channelId);
        } else {
            builder = new NotificationCompat.Builder(this);
        }
        //
        builder.setTicker(ticker);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.notificationicon);
        builder.setWhen(System.currentTimeMillis());
        // 超時銷毀
        builder.setTimeoutAfter(10 * 1000);
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setOngoing(false);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notifyId, intent, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);
        //送出通知
        manager.notify(notifyId, builder.build());
    }


    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }



}
