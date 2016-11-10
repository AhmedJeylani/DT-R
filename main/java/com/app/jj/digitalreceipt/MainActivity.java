package com.app.jj.digitalreceipt;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.googlecode.leptonica.android.Scale;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.util.ArrayList;

public class MainActivity extends Activity {



    //This makes the receiptList that stores the users receipts
    ArrayList<ReceiptData> receiptList = new ArrayList<>();
    Intent scanIntent,viewIntent;
    String viewType;
    ReceiptData receipt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TessBaseAPI tess = new TessBaseAPI();


        scanIntent = getIntent();

        if(scanIntent.getExtras() != null) {

            //This gets the data from the ImageScanActivity
            receiptList = (ArrayList<ReceiptData>) scanIntent.getExtras().get("data array");

            ListAdapter  adapter = new CustomAdapter(MainActivity.this, receiptList);
            ListView listView = (ListView) findViewById(R.id.list_id);
            listView.setAdapter(adapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                    receipt = (ReceiptData)adapterView.getItemAtPosition(position);
                    String receiptName = receipt.getReceiptName();


                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle(receiptName)
                            .setMessage("How do you want to see your receipt?")
                            .setPositiveButton("As Image", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    viewType = "image";
                                    viewIntent = new Intent(MainActivity.this,ViewActivity.class);
                                    viewIntent.putExtra("receipt data",receipt);
                                    viewIntent.putExtra("view type",viewType);
                                    MainActivity.this.startActivity(viewIntent);

                                }
                            })
                            .setNegativeButton("As Text", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    viewType = "text";
                                    viewIntent = new Intent(MainActivity.this,ViewActivity.class);
                                    viewIntent.putExtra("receipt data",receipt);
                                    viewIntent.putExtra("view type",viewType);
                                    MainActivity.this.startActivity(viewIntent);


                                }
                            })
                            .show();

                    Toast.makeText(MainActivity.this, "Clicked " + receiptName, Toast.LENGTH_SHORT).show();
                }
            });
        }



        ImageButton addBtn = (ImageButton) findViewById(R.id.addBtn_id);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ImageScanActivity.class);

                intent.putExtra("data array", receiptList);
                MainActivity.this.startActivity(intent);
                finish();
            }
        });






    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate menu, this adds items to the action bar form the xml file
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        handle action bar item clicks here, the action bar will
        automatically handle clicks on the Home/up button, so long as
        you specify a parent activity in AndroidManifest.xml
         */

        switch(item.getItemId()) {
            case R.id.edit_id:
                Toast.makeText(MainActivity.this, "Edit View!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.change_pass_id:
                Toast.makeText(MainActivity.this, "Change Password", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
