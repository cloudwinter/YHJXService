package com.yhjx.yhservice;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yhjx.yhservice.activity.LoginActivity;
import com.yhjx.yhservice.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.btn_to_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

    }
}
