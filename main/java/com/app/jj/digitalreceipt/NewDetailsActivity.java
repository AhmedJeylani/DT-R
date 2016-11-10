package com.app.jj.digitalreceipt;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.app.jj.digitalreceipt.requests.RegisterReq;

import org.json.JSONException;
import org.json.JSONObject;

public class NewDetailsActivity extends AppCompatActivity {

    private EditText un,pass,city,passCopy;
    private AlertDialog.Builder builder;
    private Response.Listener<String> responseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_details);

        //This displays the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        un = (EditText) findViewById(R.id.username_id);
        pass = (EditText) findViewById(R.id.newPass_id);
        city = (EditText) findViewById(R.id.secAns_id);
        passCopy = (EditText) findViewById(R.id.newPassCopy_id);

        Button submitB = (Button) findViewById(R.id.submitButton_id);
        builder = new AlertDialog.Builder(this);


        submitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usernameStr = un.getText().toString();
                final String passStr = pass.getText().toString();
                final String cityStr = city.getText().toString();
                final String passCopyStr = passCopy.getText().toString();

                if(!passStr.equals(passCopyStr)){
                    Toast.makeText(NewDetailsActivity.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                }
                else {
                    responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //this gets the response from the server and stores it in jsonResponse
                                JSONObject jsonResponse = new JSONObject(response);
                                /*
                                this gets the String response and stores it in serverResponse which
                                which is checked using the switch statement to see whether the server
                                has registered or an error occurred
                                 */

                                String serverResponse = jsonResponse.getString("response");
                                switch (serverResponse) {
                                    case "Successfully Registered":
                                        Intent intent = new Intent(NewDetailsActivity.this, LoginActivity.class);
                                        NewDetailsActivity.this.startActivity(intent);
                                        finish();
                                        break;

                                    case "Server is Down!":
                                        builder.setMessage(serverResponse)
                                                .setNegativeButton("Retry", null)
                                                .create()
                                                .show();
                                        break;

                                    case "Username exists":
                                        builder.setMessage("Username already exists")
                                                .setNegativeButton("Okay", null)
                                                .create()
                                                .show();
                                        break;

                                    case "Password/Username invalid":
                                        Intent popUp = new Intent(NewDetailsActivity.this,ViewActivity.class);
                                        popUp.putExtra("view type","password req");
                                        NewDetailsActivity.this.startActivity(popUp);

                                        break;

                                    case "Fill details":
                                        builder.setMessage("Fill in ALL your details")
                                                .setNegativeButton("Okay", null)
                                                .create()
                                                .show();
                                        break;

                                    default:
                                        Toast.makeText(NewDetailsActivity.this,"ERROR!",Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    /*
                    Listener = when volley has finished the request its going to
                    inform the listener in the parameters aka "responseListener"
                    */

                    RegisterReq registerRequest = new RegisterReq(usernameStr, passStr, cityStr, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(NewDetailsActivity.this);
                    queue.add(registerRequest);
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
