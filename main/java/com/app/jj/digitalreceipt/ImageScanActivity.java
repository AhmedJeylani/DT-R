package com.app.jj.digitalreceipt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ImageScanActivity extends AppCompatActivity {

    private static final int camReqCode = 1;
    private static final int cropReqCode = 2;
    private Uri imageUri;
    private Bitmap bmImage,contrastedBmImage;
    //private ProgressDialog progressDialog;


    private File imageFile, appDir,picDir,dataDir,tessSubDir,engData;
    private ImageView takenImg = null;
    private String pictureName,imageText;
    private TextView imageTextResult;
    private EditText contrastValue,receiptName,companyName;
    private ArrayList<ReceiptData> receiptList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_scan);

        //This displays the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageButton cameraBtn = (ImageButton) findViewById(R.id.cameraBtn_id);
        ImageButton doneBtn = (ImageButton) findViewById(R.id.doneBtn_id);
        ImageButton retryBtn = (ImageButton) findViewById(R.id.retryBtn_id);

        receiptName = (EditText) findViewById(R.id.receiptName_id);
        companyName = (EditText) findViewById(R.id.companyName_id);
        takenImg = (ImageView) findViewById(R.id.takenImage_id);



        //contrastValue = (EditText) findViewById(R.id.contrastValue_id);
        Button scanBtn = (Button) findViewById(R.id.scanBtn_id);

        File root = Environment.getExternalStorageDirectory();
        appDir = new File(root.getAbsolutePath() + "/Digital_Receipt");
        picDir = new File(appDir.getAbsolutePath()+ "/pictures");

        //File() = first arg is where you want to save it and second is what you want to call it
        dataDir = new File(appDir.getAbsolutePath()+"/data");
        tessSubDir = new File(dataDir.getAbsolutePath() + "/tessdata");
        engData = new File(tessSubDir,"eng.traineddata");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(!engData.exists()) {
            builder.setTitle("Information")
                    .setMessage("A File that allows the app to scan your image is going to be saved into your internal storage, are you sure you want to continue?")
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            AlertDialog.Builder warningBuilder = new AlertDialog.Builder(ImageScanActivity.this);
                            warningBuilder.setTitle("Warning")
                                    .setMessage("Without this file, your receipt cannot be scanned!")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(ImageScanActivity.this,MainActivity.class);
                                            ImageScanActivity.this.startActivity(intent);
                                            finish();
                                        }
                                    });

                            warningBuilder.show();
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            boolean  fileCheck = false;
                            mkDirCheck(appDir);
                            mkDirCheck(picDir);
                            mkDirCheck(dataDir);
                            mkDirCheck(tessSubDir);

                            try {
                                fileCheck = engData.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //This gets the assets from Asset the asset folder
                            AssetManager assetManager = getAssets();
                            InputStream inStream = null;

                            try {
                                //this opens the file located in the assets folder
                                inStream = assetManager.open("eng.traineddata");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if(inStream != null && fileCheck) {
                                Toast.makeText(ImageScanActivity.this, "File made at " + engData.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                                copyInputStreamToFile(inStream,engData);
                            }
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }



        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bmImage != null){
                    //imageText = imageToText(bmImage, dataDir);
                    HeavyTask imageScanTask = new HeavyTask(bmImage,dataDir,false);
                    imageScanTask.execute();
//                    int conValue = Integer.parseInt(contrastValue.getText().toString());
//                    contrastedBmImage = createContrast(bmImage,conValue);
//                    takenImg.setImageBitmap(contrastedBmImage);

                }
                else {
                    Toast.makeText(ImageScanActivity.this, "No image to scan", Toast.LENGTH_SHORT).show();
                }
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String imageText = imageToText(bmImage, dataDir);
                Intent doneIntent = new Intent(ImageScanActivity.this,MainActivity.class);
                AlertDialog.Builder builder = new AlertDialog.Builder(ImageScanActivity.this);

                if(bmImage == null) {
                    errorNotification("Please take a picture of the receipt");
                }
                else if(imageText == null) {
                    errorNotification("Please scan the image");
                }
                else if(receiptName.getText().toString().equals("") || companyName.getText().toString().equals("")) {
                    errorNotification("Please fill in the Receipt Name and Company Name blanks");
                }
                else {
                    bitmapSave(contrastedBmImage,imageFile);

                    String receiptNameStr = receiptName.getText().toString();
                    String companyNameStr = companyName.getText().toString();


                    Toast.makeText(ImageScanActivity.this, receiptNameStr + " has been Saved", Toast.LENGTH_SHORT).show();

                    Intent mainIntent = getIntent();
                    ReceiptData data = new ReceiptData(receiptNameStr,companyNameStr,imageFile,imageText);

                    //This gets the Receipt list from the main activity
                    receiptList = (ArrayList<ReceiptData>) mainIntent.getExtras().get("data array");
                    receiptList.add(data);


                    Toast.makeText(ImageScanActivity.this, "Image saved at " + imageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();

                    //Might need the picture name!!! Check ASAP
                    doneIntent.putExtra("picture name",pictureName);
                    doneIntent.putExtra("data array", receiptList);

                    ImageScanActivity.this.startActivity(doneIntent);
                    finish();
                }



            }
        });

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takenImg.setImageBitmap(null);
                receiptName.setText(null);
                companyName.setText(null);
                imageText = null;
                contrastedBmImage = null;
                bmImage = null;
                imageFile.delete();
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                pictureName = getPictureName();
                //String causing not to save
                imageFile = new File(picDir.getAbsolutePath(),pictureName);

                imageUri = Uri.fromFile(imageFile);


                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                //The picture wants URI format not file format and saves it
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                //0 = is the request code that was sent and checks who sent that request
                startActivityForResult(cameraIntent,  camReqCode);

            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {


            switch (requestCode) {
                case camReqCode:


                    bmImage = pictureSetting(imageFile);
                    //contrastedBmImage = createContrast(bmImage,100);
                    HeavyTask contrastTask = new HeavyTask(bmImage,100,true);
                    contrastTask.execute();
//                    String imageText = imageToText(bmImage, dataDir);
//
//                    TextView imageTextResult = (TextView) findViewById(R.id.imageResult_id);
//                    imageTextResult.setText(imageText);

                    break;

                case cropReqCode:
                    //get the returned data
                    Bundle extras = data.getExtras();
                    //get the cropped bitmap
                    Bitmap thePic = extras.getParcelable("data");
                    Toast.makeText(ImageScanActivity.this, "Image saved at " + imageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    takenImg = (ImageView) findViewById(R.id.takenImage_id);

                    //Bitmap bmImage = pictureSetting(imageFile);
                    takenImg.setImageBitmap(thePic);
                    //String imageText = imageToText(bmImage, dataDir);

                    //TextView imageTextResult = (TextView) findViewById(R.id.imageResult_id);
                    //imageTextResult.setText(imageText);
                    break;

                default:

                    break;
            }

        }
        else {
            Toast.makeText(ImageScanActivity.this, "No image saved", Toast.LENGTH_SHORT).show();
        }
    }

    private void errorNotification(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK",null)
                .show();
    }
    private String getPictureName() {

        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd_HH:mm");

        String timeStamp = sdf.format(new Date());

        return "DR" + timeStamp +  ".jpg";
    }

    private Bitmap pictureSetting(File imageFile) {

        /*
        BitmapFactory.Options is used to define some parameters of the image we are loading
        loading full image can have a negative affect on memory consumption
        */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        /*
        settings inSampleSize to anything higher than 1 will return a smaller image which
        saves memory
        */
        bmOptions.inSampleSize = 4;

        //decvodeFile turns file into Bitmap
        return BitmapFactory.decodeFile(imageFile.getAbsolutePath(),bmOptions);
    }


    private String imageToText(Bitmap image, File data){

        TessBaseAPI tess = new TessBaseAPI();
        tess.init(data.getAbsolutePath()+"/","eng");
        tess.setImage(image);
        String imageText = tess.getUTF8Text();
        tess.end();

        return imageText;
    }
    private void copyInputStreamToFile(InputStream in, File file ) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mkDirCheck(File file) {
        if(file.mkdir()) {
            Toast.makeText(ImageScanActivity.this, "Folder made at " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap createContrast(Bitmap src,double value) {


        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        // get contrast value
        double contrast = Math.pow((100 + value) / 100, 2);



        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                //This applies the red color to both green and black therefore neutralising the colours to white and black
                R = Color.red(pixel);
                R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                G = Color.red(pixel);
                G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                B = Color.red(pixel);
                B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        // return final image
        return bmOut;
    }

    private void bitmapSave(Bitmap bitmapImage,File imgFile){
        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(imgFile);
            //This compresses the image and then saves it in the internal storage
            bitmapImage.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            Log.e(getClass().getSimpleName(),"Saving Bitmap Image");
        }

    }

    private class HeavyTask extends AsyncTask<Void,Void,Void> {
        ProgressDialog progressDialog;

        Bitmap _image, _contrastedImage;
        String _imageText;
        boolean _contrast;
        File _imageFile;
        double _contrastValue;



        public HeavyTask(Bitmap image,double contrastValue, Boolean contrast) {
            this._image = image;
            this._contrast = contrast;
            this._contrastValue = contrastValue;

        }

        public HeavyTask(Bitmap contrastedBitmap,File BmImageFile,Boolean contrast) {

            this._imageFile = BmImageFile;
            this._contrastedImage = contrastedBitmap;
            this._contrast = contrast;
        }


        @Override
        protected void onPreExecute() {
            if(this._contrast) {
                progressDialog = ProgressDialog.show(ImageScanActivity.this,"Contrast","Adding a contrast to the image, Please wait...");

            }
            else {
                progressDialog = ProgressDialog.show(ImageScanActivity.this,"Scanning","Scanning the receipt for text, Please Wait...");
            }

        }

        @Override
        protected Void doInBackground(Void... v) {

            if(this._contrast) {
                this._contrastedImage = createContrast(_image,this._contrastValue);
            }
            else {
               this._imageText = imageToText(this._contrastedImage,this._imageFile);
            }

            return null;


        }

        @Override
        protected void onPostExecute(Void v) {

            if(this._contrast) {
                takenImg.setImageBitmap(this._contrastedImage);
                ImageScanActivity.this.contrastedBmImage = this._contrastedImage;
                Toast.makeText(ImageScanActivity.this, "Contrast added", Toast.LENGTH_SHORT).show();


            }
            else {
                ImageScanActivity.this.imageText = this._imageText;
                Toast.makeText(ImageScanActivity.this, "Scan was Successful", Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }



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
                Intent backIntent = new Intent(this, MainActivity.class);
                this.startActivity(backIntent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }



}
