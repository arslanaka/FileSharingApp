package com.arslan.textapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private final String s = "This is a sample text";
    private static final String TAG = "MainActivity";
    private Context context;

    Button btn, btnLoad;
    TextView tv;

    String filePath = "MyFiles";
    String fileName = "arslan.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);
        btnLoad =  findViewById(R.id.btnLoad);
        tv = findViewById(R.id.tv);
        actionViews();
    }

    private void actionViews() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        context = this;
//        generateNoteOnSD(context,"Arslan",s);
        btn.setOnClickListener(v->{
//            writeData();
            test();
        });

        btnLoad.setOnClickListener(v->{
            FileReader fr = null;
            File myExternalFile = new File(getExternalFilesDir(filePath),fileName);
            StringBuilder stringBuilder = new StringBuilder();
            try{
                fr = new FileReader(myExternalFile);
                BufferedReader br = new BufferedReader(fr);
                String line = br.readLine();
                while (line !=null){
                    stringBuilder.append(line).append('\n');
                    line = br.readLine();
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                String fileContents = stringBuilder.toString();
                tv.setText(fileContents);
            }

            Log.d(TAG, "actionViews: sharing file");
            shareFile();
        });

    }


    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "generateNoteOnSD: Exception :: "+e.getLocalizedMessage());
        }
    }


    public void writeData (){
        String fileName = "arslan.txt";


        try
        {
            File root = new File(Environment.getExternalStorageDirectory()+File.separator+"Downloads");
            Log.d(TAG, "writeData: root "+root.getAbsolutePath());
            if (!root.exists())
            {
                root.mkdirs();
                Log.d(TAG, "writeData: creating folder");
            }
            File gpxfile = new File(root, fileName);


            FileWriter writer = new FileWriter(gpxfile,true);
            writer.append(s+"\n\n");
            writer.flush();
            writer.close();
            Log.d(TAG, "writeData: doneee");
            Toast.makeText(this, "Data has been written to Report File", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            Log.d(TAG, "writeData: exception :: "+e.getLocalizedMessage() );

        }
    }


    public void test(){
        File externalFile = new File(getExternalFilesDir(filePath),fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(externalFile);
            fos.write(s.getBytes());
            Log.d(TAG, "test: writing");
        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shareFile(){
        try {
            File file = new File(getExternalFilesDir(filePath) + "/" + "arslan.txt");
            Log.d(TAG, "shareFile: file path :: "+file);
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            Uri uri = Uri.parse("file://" + file.getAbsolutePath());
//            Uri uri = Uri.parse("content://" + file.getAbsolutePath());
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);


            //
            List<ResolveInfo> resolvedInfoActivities = getPackageManager().queryIntentActivities(sharingIntent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo ri : resolvedInfoActivities) {
                context.grantUriPermission(ri.activityInfo.packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Log.d(TAG, "shareFile: uri permission : "+ri.activityInfo.packageName);
            }

            startActivity(Intent.createChooser(sharingIntent, "share file with"));



        }catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "shareFile: Exception :: "+e.getLocalizedMessage());
        }





    }
}