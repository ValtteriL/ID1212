package com.example.vleh.hw5.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import com.example.vleh.hw5.R;
import com.example.vleh.hw5.common.Constants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupActivity();
    }

    // Setup activity
    private void setupActivity() {

        // set listener for connectButton
        final Button connectButton = (Button) findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean hasErrors = false;

                // check hostname is not empty
                final EditText hostnameField = (EditText) findViewById(R.id.hostnameField);
                if(TextUtils.isEmpty(hostnameField.getText())) {
                    hostnameField.setError("Please enter a hostname");
                    hasErrors = true;
                }

                // check port is not empty
                final TextView portField = (TextView) findViewById(R.id.portField);
                if(TextUtils.isEmpty(portField.getText())) {
                    portField.setError("Please specify a port");
                    hasErrors = true;
                }

                if (hasErrors) {
                    return;
                }

                System.out.println("Hostname = " + hostnameField.getText().toString() + ", Port = " + portField.getText().toString());

                // if no errors, start new activity
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra(Constants.EXTRA_HOSTNAME, hostnameField.getText().toString());
                intent.putExtra(Constants.EXTRA_PORT, Integer.parseInt(portField.getText().toString()));
                startActivity(intent);

            }
        });
    }
}
