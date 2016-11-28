
package com.example.administrator.easyshop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.easyshop.commons.ActivityUtils;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    private ActivityUtils activityUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        activityUtils = new ActivityUtils(this);
        // TODO: 2016/11/17 0017 登录账号冲突
        // TODO: 2016/11/17 0017 判断用户是否登录

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                activityUtils.startActivity(MainActivity.class);
                finish();
            }
        },1500);

    }
}
