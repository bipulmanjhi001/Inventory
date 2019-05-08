package com.inventory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    EditText user_id,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        password=(EditText)findViewById(R.id.password);
        user_id=(EditText)findViewById(R.id.user_id);


    }
}
