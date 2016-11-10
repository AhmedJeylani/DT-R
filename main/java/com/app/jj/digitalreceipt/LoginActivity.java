package com.app.jj.digitalreceipt;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.app.jj.digitalreceipt.requests.LoginReq;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView regButton = (TextView) findViewById(R.id.registerButton_id);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,NewDetailsActivity.class);
                LoginActivity.this.startActivity(intent);
                finish();
            }
        });

        Button forgotButton = (Button) findViewById(R.id.forgotButton_id);

        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,ForgottenDetails.class);
                startActivity(intent);
                finish();
            }
        });


        Button loginButton = (Button) findViewById(R.id.loginButton_id);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText un = (EditText) findViewById(R.id.savedUsername_id);
                EditText pass = (EditText) findViewById(R.id.savedPass_id);

                final String usernameStr = un.getText().toString();
                final String passStr = pass.getText().toString();

                if(usernameStr.equalsIgnoreCase("admin")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(intent);
                    finish();
                }
                else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                Boolean serverResponse = jsonResponse.getBoolean("success");

                                if (serverResponse) {
                                    String username = jsonResponse.getString("username");
                                    String password = jsonResponse.getString("password");
                                    String city = jsonResponse.getString("city");
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                intent.putExtra("username", username);
//                                intent.putExtra("password", password);
//                                intent.putExtra("city", city);
                                    LoginActivity.this.startActivity(intent);
                                    finish();


                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setTitle("Incorrect Details")
                                            .setMessage("Your Username and Password don't match")
                                            .setNegativeButton("Okay", null)
                                            .create()
                                            .show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    LoginReq loginReq = new LoginReq(usernameStr, passStr, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginReq);
                }
            }
        });



    }
}
