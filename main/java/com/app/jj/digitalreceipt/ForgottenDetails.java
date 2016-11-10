package com.app.jj.digitalreceipt;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.app.jj.digitalreceipt.requests.ForgottenReq;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgottenDetails extends AppCompatActivity {

    private EditText un, pass, city, passCopy;
    private AlertDialog.Builder builder;
    private String err;
    private Response.Listener<String> responseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_details);

        //This displays the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        un = (EditText) findViewById(R.id.username_id);
        pass = (EditText) findViewById(R.id.newPass_id);
        passCopy = (EditText) findViewById(R.id.newPassCopy_id);
        city = (EditText) findViewById(R.id.secAns_id);
        builder = new AlertDialog.Builder(this);


        Button submitButton = (Button) findViewById(R.id.submit_id);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String usernameStr = un.getText().toString();
                final String passStr = pass.getText().toString();
                final String cityStr = city.getText().toString();
                final String passCopyStr = passCopy.getText().toString();
                err = "Error";


                if (!passStr.equals(passCopyStr)) {
                    Toast.makeText(ForgottenDetails.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                } else {
                    responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                String serverResponse = jsonResponse.getString("response");

                                switch (serverResponse) {
                                    case "Password has successfully changed!":
//                                        builder.setTitle("Success!")
//                                                .setMessage(serverResponse)
//                                                .setPositiveButton("Ok",null)
//                                                .create()
//                                                .show();
                                        Toast.makeText(ForgottenDetails.this, "Password Changed", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(ForgottenDetails.this, LoginActivity.class);
                                        ForgottenDetails.this.startActivity(intent);
                                        finish();

                                        break;

                                    case "Fill details":
                                        builder.setTitle(err)
                                                .setMessage("Fill in ALL your details")
                                                .setNegativeButton("Okay", null)
                                                .create()
                                                .show();
                                        break;
                                    case "Username/City invalid":
                                        builder.setTitle(err)
                                                .setMessage("Either your username doesn't exist or your security answer was wrong")
                                                .setPositiveButton("Ok", null)
                                                .create()
                                                .show();
                                        break;

                                    case "Password invalid":
                                        Intent popUp = new Intent(ForgottenDetails.this, ViewActivity.class);
                                        popUp.putExtra("view type", "password req");
                                        ForgottenDetails.this.startActivity(popUp);
                                        break;

                                    case "Server is down":
                                        builder.setTitle(err)
                                                .setMessage(serverResponse)
                                                .setNegativeButton("Ok", null)
                                                .create()
                                                .show();
                                        break;
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };


                    ForgottenReq req = new ForgottenReq(usernameStr, passStr, cityStr, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(ForgottenDetails.this);
                    queue.add(req);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            //home = back button
            case android.R.id.home:
                Intent backIntent = new Intent(this, LoginActivity.class);
                this.startActivity(backIntent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
