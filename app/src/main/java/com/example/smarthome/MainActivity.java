package com.example.smarthome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Animation topAnim,BottomAnim;
    ImageView logo;
    TextView textView;
    TextView textView1;
    // private FirebaseAuth fAuth;
    SharedPreferences sharedPreferences;
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        // fAuth = FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                sharedPreferences=getSharedPreferences("sharedPref",MODE_PRIVATE);
                boolean isFirstTime = sharedPreferences.getBoolean("firstTime",true);
                if(isFirstTime){
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putBoolean("firstTime",false);
                    editor.commit();
                    Intent intent =new Intent(MainActivity.this,login.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent=new Intent(MainActivity.this,login.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}