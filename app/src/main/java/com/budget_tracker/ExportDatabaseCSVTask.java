package com.budget_tracker;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExportDatabaseCSVTask extends AsyncTask<String ,String, String> {
    private Context context;
    String[] columnsArray;
    List<String> dateList ;
    List<String> amountList;
    List<String> sourceList;

    private final ProgressDialog dialog;


String fileName="";


    public ExportDatabaseCSVTask(Context context,
                                 String[] columnsArray,
                                 List<String> dateList,
                                 List<String> amountList,
                                 List<String> sourceList) {
        this.context = context;
        this.columnsArray = columnsArray;
        this.dateList = dateList;
        this.amountList = amountList;
        this.sourceList = sourceList;
        dialog = new ProgressDialog(context);
    }


    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Exporting database...");
        this.dialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
//        File dbFile = getDatabasePath("myDatabase.db");
//        System.out.println(dbFile);  // displays the data base path in your logcat
        File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "");

        if (!exportDir.exists()) { exportDir.mkdirs(); }
        fileName = System.currentTimeMillis()+".xlsx";

        File file = new File(exportDir, fileName);
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

                csvWrite.writeNext(columnsArray);
                List<String[]> dateDataList = new ArrayList<>();

                for(int m=0; m<dateList.size(); m++){
                    dateDataList.add(new String[]{"Date", dateList.get(m)});

                }
                csvWrite.writeAll(dateDataList);



            List<String[]> sourceDataList = new ArrayList<>();
            for(int m=0; m<sourceList.size(); m++){
                sourceDataList.add(new String[]{"Source", sourceList.get(m)});
            }
            csvWrite.writeAll(sourceDataList);
            List<String[]> amountDataList = new ArrayList<>();
            for(int m=0; m<amountList.size(); m++){
                amountDataList.add(new String[]{"Amount", amountList.get(m)});
            }
            csvWrite.writeAll(amountDataList);

            csvWrite.close();

            return String.valueOf(true);
        }
        catch (IOException e) {
            Log.e("MainActivity", e.getMessage(), e);
            return String.valueOf(false);
        }
    }


    @SuppressLint("NewApi")
    @Override
    protected void onPostExecute(final String success) {
        if (this.dialog.isShowing()) {
            this.dialog.dismiss();
        }
        if (success.equalsIgnoreCase("true")) {
            Toast.makeText(context, "Export successful!", Toast.LENGTH_SHORT).show();
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "");
            File fileWithinMyDir = new File(exportDir, fileName);


            if (fileWithinMyDir.exists()) {
                String mimeType = "application/xlsx";
                String[] mimeTypeArray = new String[]{mimeType};

                intentShareFile.setType(mimeType);
                Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", fileWithinMyDir);

                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pdfOpenintent.setDataAndType(uri, "application/xlsx");
                pdfOpenintent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try {
                    context.startActivity(pdfOpenintent);
                } catch (ActivityNotFoundException e) {

                    Toast.makeText(context,"No Application Available to View Excel Files ", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(context, "Export failed", Toast.LENGTH_SHORT).show();
            }
        }
        else{

        }
    }
}