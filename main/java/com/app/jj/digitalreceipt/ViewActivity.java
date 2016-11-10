package com.app.jj.digitalreceipt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent receiptListIntent = getIntent();

        String viewType = receiptListIntent.getStringExtra("view type");
        ReceiptData receipt = (ReceiptData)receiptListIntent.getExtras().get("receipt data");


        if(viewType.equalsIgnoreCase("image")) {
            setContentView(R.layout.activity_image_view);
            ImageView receiptImageView = (ImageView) findViewById(R.id.receiptImageView_id);
            //settings new width and height
            setFrameToPopUpSize();
            this.setTitle("Receipt As Image");

            if (receipt != null) {
                Bitmap bmImage = BitmapFactory.decodeFile(receipt.getImageFileLocation());
                receiptImageView.setImageBitmap(bmImage);
            }


        }
        else if(viewType.equalsIgnoreCase("text")) {
            setContentView(R.layout.activity_text_view);
            EditText receiptTextView = (EditText) findViewById(R.id.receiptTextView_id);
            //settings new width and height
            setFrameToPopUpSize();
            this.setTitle("Receipt As Text");

            if (receipt != null) {
                receiptTextView.setText(receipt.getImageText());
            }
        }
        else if(viewType.equalsIgnoreCase("password req")) {
            setContentView(R.layout.activity_info_pop_up);
            //settings new width and height
            setFrameToPopUpSize();
            this.setTitle("Invalid Entry");
        }


    }

    //settings new width and height
    private void setFrameToPopUpSize() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width * .8),(int)(height * .8));
    }
}
