package com.nic.HousingWorkMonitoringSystemWithGeoFensing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;

   /* public static final String HOTLINE_APP_ID = "cedda3bc-c628-4bac-8f65-9b408d437614";
    public static final String HOTLINE_APP_KEY = "3ef7a41f-9419-4bbd-ba18-437f8eb83341";*/

    private String screenType = "Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        showSignInScreen();
//        RunAnimation();




    }

    //Show sign in screen
    private void showSignInScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(SplashScreen.this, LoginScreen.class);
                i.putExtra("screenType", screenType);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        }, SPLASH_TIME_OUT);
    }

    private void RunAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.text_anim);

        TextView tv = (TextView) findViewById(R.id.headtext);

        tv.startAnimation(a);
    }

}
