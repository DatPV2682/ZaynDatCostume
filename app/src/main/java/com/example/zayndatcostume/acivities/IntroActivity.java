package com.example.zayndatcostume.acivities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zayndatcostume.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Delay trong 2 giây sau đó chuyển sang LoginActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
                finish();
            }
        }, 2000); // 2000 milliseconds = 2 giây
    }

    private void nextActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            // chua login
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            // da login
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
        }

    }
}