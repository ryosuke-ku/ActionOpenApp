package com.example.actionopenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_PICK_IMAGEFILE = 1001;
    private TextView textView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);

        imageView = findViewById(R.id.image_view);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file browser.
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                // Filter to only show results that can be "opened", such as a
                // file (as opposed to a list of contacts or timezones)
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                // Filter to show only images, using the image MIME data type.
                // it would be "*/*".
                intent.setType("*/*");

                startActivityForResult(intent, RESULT_PICK_IMAGEFILE);
            }
        });
    }

    public static String getNowDate(){
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        final Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }

    public static String getPath(Context context, Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        String[] columns = { MediaStore.Images.Media.DATA };
        Cursor cursor = contentResolver.query(uri, columns, null, null, null);
        cursor.moveToFirst();
        String path = cursor.getString(0);
        cursor.close();
        return path;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.
        if (requestCode == RESULT_PICK_IMAGEFILE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            if(resultData.getData() != null){

                ParcelFileDescriptor pfDescriptor = null;
                try{
                    Uri uri = resultData.getData();

                    String file_path = getPath(MainActivity.this,uri);
                    System.out.println("file path:" + file_path);

                    String file_name = new File(uri.getPath()).getName();
                    System.out.println("file name:" + file_name);



                    CsvReader parser = new CsvReader();
                    parser.reader(getApplicationContext(), uri);

                    String num = String.valueOf(parser.objects.size());
//                    Log.d("読み込みサイズ:",num);
                    System.out.println("読み込みサイズ:" + num);


                    BuppinDBHelper buppin_helper = new BuppinDBHelper(MainActivity.this);
                    SQLiteDatabase db = buppin_helper.getReadableDatabase();

                    for(int i=1; i < parser.objects.size() ; i++){
                        Log.d("debug", String.valueOf(i) + ":" + String.valueOf(parser.objects.get(i).getDenpyoNumber()));
                        ContentValues values = new ContentValues();
                        values.put(BuppinDBHelper.DENPYO_NUMBER, String.valueOf(parser.objects.get(i).getDenpyoNumber()));
                        values.put(BuppinDBHelper.KANRI_TYPE, String.valueOf(parser.objects.get(i).getKariType()));
                        values.put(BuppinDBHelper.KANRI_NUMBER, String.valueOf(parser.objects.get(i).getKanriNumber()));
                        values.put(BuppinDBHelper.USER_NAME, String.valueOf(parser.objects.get(i).getUserName()));
                        values.put(BuppinDBHelper.KANRI_NAME, String.valueOf(parser.objects.get(i).getKanriName()));
                        values.put(BuppinDBHelper.LOCATION, String.valueOf(parser.objects.get(i).getLocation()));
                        values.put(BuppinDBHelper.BUILDING_NAME, String.valueOf(parser.objects.get(i).getBuildingName()));
                        values.put(BuppinDBHelper.DETAIL_LOCATION, String.valueOf(parser.objects.get(i).getDetailLocation()));
                        values.put(BuppinDBHelper.REMARKS, String.valueOf(parser.objects.get(i).getRemarks()));
                        values.put(BuppinDBHelper.KANRI_STATUS, String.valueOf(parser.objects.get(i).getKanriStatus()));
                        values.put(BuppinDBHelper.TYOUSA_RESULT, String.valueOf(parser.objects.get(i).getTyosaResult()));
                        values.put(BuppinDBHelper.TYOUSA_DATE, getNowDate());
                        values.put(BuppinDBHelper.TYOUSA_DID_NAME, String.valueOf(parser.objects.get(i).getTyosaDidName()));
                        values.put(BuppinDBHelper.TYOUSA_DID_NAME_CODE, String.valueOf(parser.objects.get(i).getTyosaDidNameCode()));

                        db.insert(BuppinDBHelper.DB_TABLE, null, values);
                    }

                    // Uriを表示
                    textView.setText(
                            String.format(Locale.US, "Uri:　%s",uri.toString()));

                    pfDescriptor = getContentResolver().openFileDescriptor(uri, "r");
                    if(pfDescriptor != null){
//                        FileDescriptor fileDescriptor = pfDescriptor.getFileDescriptor();
//                        Bitmap bmp = BitmapFactory.decodeFileDescriptor(fileDescriptor);
//                        pfDescriptor.close();
//                        imageView.setImageBitmap(bmp);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try{
                        if(pfDescriptor != null){
                            pfDescriptor.close();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        }
    }

}