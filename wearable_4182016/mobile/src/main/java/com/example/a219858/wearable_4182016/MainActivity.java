package com.example.a219858.wearable_4182016;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class MainActivity extends AppCompatActivity {

    private EditText IP, IPPORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IP = (EditText) findViewById(R.id.editText);
        IPPORT = (EditText) findViewById(R.id.editText2);
        Button Connect = (Button) findViewById(R.id.button);

        Button Set = (Button) findViewById(R.id.button2);
        Set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IP.getText() != null && IPPORT.getText() != null) {
                    WearListCallListenerService.HOST = IP.getText().toString();
                    WearListCallListenerService.PORT = IPPORT.getText().toString();
                }
            }
        });

        Connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent WearableService = new Intent(MainActivity.this, WearableListenerService.class);
                startService(WearableService);
                finish();
            }
        });
    }
}
