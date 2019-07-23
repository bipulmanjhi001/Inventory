package com.broadwaybazar.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.broadwaybazar.R;
import com.broadwaybazar.api.URL;
import com.broadwaybazar.model.ConnectivityReceiver;
import com.broadwaybazar.model.VolleySingleton;
import com.broadwaybazar.pref.SharedPrefManager;
import com.broadwaybazar.pref.User;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private static final String SHARED_PREF_NAME = "Inventorypref";
    private static final String KEY_ID = "keyid";
    String username, password;

    String tokens,useridss="blank",passwordss="";
    private EditText UserView;
    ProgressBar login_progress;
    private EditText mPasswordView;
    CheckBox remember;

    public static final String REMEMBER_DATA = "remember_data" ;
    public static final String userid = "userid";
    public static final String passwords = "passwords";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserView = findViewById(R.id.user_id);
        mPasswordView = findViewById(R.id.password);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        tokens = prefs.getString(KEY_ID, null);

        SharedPreferences prefs2 = getSharedPreferences(REMEMBER_DATA, MODE_PRIVATE);
        useridss = prefs2.getString(userid, null);
        passwordss = prefs2.getString(passwords, null);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        login_progress=(ProgressBar)findViewById(R.id.login_progress);
        Button SignInButton = findViewById(R.id.sign_in_button);
        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
            }
        });

        remember=(CheckBox)findViewById(R.id.remember);
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                        if (useridss != null && useridss.equalsIgnoreCase("blank")) {
                            sharedpreferences =  getSharedPreferences(REMEMBER_DATA, MODE_PRIVATE);
                            useridss=UserView.getText().toString();
                            passwordss=mPasswordView.getText().toString();
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(userid, useridss);
                            editor.putString(passwords, passwordss);
                            editor.apply();

                    }else {
                            UserView.setText(useridss);
                            mPasswordView.setText(passwordss);
                    }
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            attemptLogin();
        } else {
            message = "connect your internet.";
            color = Color.RED;
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }
    }

    private void attemptLogin() {
        UserView.setError(null);
        mPasswordView.setError(null);
        username = UserView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            UserView.setError(getString(R.string.error_field_required));
            focusView = UserView;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();

        } else {
            Authenticate();
        }
    }

    public void Authenticate() {
        login_progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("status")) {

                                JSONObject userJson = obj.getJSONObject("user");
                                String token = userJson.getString("token");
                                String username = userJson.getString("username");
                                String role = userJson.getString("role");
                                String sales_id = userJson.getString("sales_id");

                                User user = new User(
                                        userJson.getString("token"),
                                        userJson.getString("username"),
                                        userJson.getString("role"),
                                        userJson.getString("sales_id"));

                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                finish();

                                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                                intent.putExtra("name", username);
                                intent.putExtra("token", token);
                                intent.putExtra("role", role);
                                intent.putExtra("sales_id",sales_id);
                                startActivity(intent);
                                finish();

                            } else if (!obj.getBoolean("status")) {

                                String error = obj.getString("error");
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "Connection error..", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onBackPressed() {
        backButtonHandler();
        return;
    }

    public void backButtonHandler() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Login.this);
        alertDialog.setTitle("Leave application?");
        alertDialog.setMessage("Are you sure you want to leave the application?");
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}


